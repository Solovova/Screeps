package mainContext.constants.path

import mainContext.MainContext
import mainContext.dataclass.SlaveRoomType
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import screeps.api.*
import screeps.api.structures.Structure
import kotlin.math.min
import kotlin.math.roundToInt

class ContainerCacheCarrier(val mc: MainContext) { //globCacheCarrier in Memory
    private val dataCacheCarrier: MutableMap<String, CacheCarrier> = mutableMapOf()

    private fun getByKeyUseCache(key: String):CacheCarrier? {
        var result:CacheCarrier? = dataCacheCarrier[key]
        if (result == null) {
            result = CacheCarrier.initFromCache(key)
            if (result !=null) {
                dataCacheCarrier[key] = result
            }
        }
        return dataCacheCarrier[key]
    }

    fun gets(type: String, mainRoom: MainRoom, slaveRoom: SlaveRoom? = null, recalculate: Boolean = false, inSwampCost: Int = 10, inPlainCost: Int = 2, doNotCalculateRoads: Boolean = false, safeMove: Boolean = false) : CacheCarrier? {
        var objectTo : Structure? = null

        when(type) {
            "mainContainer0" -> objectTo = mainRoom.structureContainerNearSource[0]
            "mainContainer1" -> objectTo = mainRoom.structureContainerNearSource[1]
            "slaveContainer0" -> if (slaveRoom != null)  objectTo = slaveRoom.structureContainerNearSource[0]
            "slaveContainer1" -> if (slaveRoom != null)  objectTo = slaveRoom.structureContainerNearSource[1]
            "slaveContainer2" -> if (slaveRoom != null)  objectTo = slaveRoom.structureContainerNearSource[2]
        }

        if (objectTo == null) return null

        val objectFrom : Structure = mainRoom.structureStorage[0] ?: return null

        val keyRecord : String = objectFrom.id + objectTo.id

        var carrierAuto: CacheCarrier? = getByKeyUseCache(keyRecord)

        if (recalculate || carrierAuto == null || carrierAuto.default || (carrierAuto.tickRecalculate + 1000) < Game.time){
            val ret = mc.lm.lmHarvestGetWayFromPosToPos.gets(objectFrom.pos, objectTo.pos, inSwampCost = inSwampCost, inPlainCost = inPlainCost)
            mc.lm.messenger.log("TEST", mainRoom.name, "Recalculate ways: $type ${!ret.incomplete}", COLOR_YELLOW)
            if (!ret.incomplete) {
                carrierAuto = this.getNewCarrierAuto(ret, mainRoom, slaveRoom = slaveRoom, doNotCalculateRoads = doNotCalculateRoads, safeMove = safeMove)
                this.dataCacheCarrier[keyRecord] = carrierAuto
                carrierAuto.saveToCache(keyRecord)
            }
        }
        return this.dataCacheCarrier[keyRecord]
    }

    private fun getNewCarrierAuto (ret: PathFinder.Path, mainRoom: MainRoom, slaveRoom: SlaveRoom?, doNotCalculateRoads: Boolean = false, safeMove:Boolean = false): CacheCarrier {
        val weight: Int
        val fMaxCapacity: Int
        val needCarriers: Int
        var needCapacity: Int
        val timeForDeath: Int
        var fBody : Array<BodyPartConstant>
        val pathSize:Int = if (doNotCalculateRoads) ret.cost else ret.path.size
        if (slaveRoom == null) {
            weight = (((SOURCE_ENERGY_CAPACITY +300)*pathSize*2).toDouble() / ENERGY_REGEN_TIME).roundToInt()
            fMaxCapacity = min(mainRoom.room.energyCapacityAvailable / 150  *100,1600)
            needCarriers  = weight / fMaxCapacity + 1
            needCapacity = weight / needCarriers / 100 * 100 + 100
            timeForDeath = pathSize*2 + 20
            fBody = arrayOf()
            for (i in 0 until (needCapacity/100)) fBody += arrayOf(CARRY, CARRY, MOVE)
        }else{
            weight = if (slaveRoom.constant.model == SlaveRoomType.Dangeon) (((SOURCE_ENERGY_KEEPER_CAPACITY + 1500)*pathSize*2).toDouble() / ENERGY_REGEN_TIME).roundToInt()
            else (((SOURCE_ENERGY_CAPACITY +500)*pathSize*2).toDouble() / ENERGY_REGEN_TIME).roundToInt()
            fMaxCapacity = min((mainRoom.room.energyCapacityAvailable - 200) / 150 * 100 + 50,1550)
            needCarriers  = weight / fMaxCapacity + 1

            needCapacity = weight / needCarriers / 50 * 50 + 50
            if  (needCapacity / 100 * 100 == needCapacity) needCapacity += 50
            if ( needCapacity>1550) needCapacity = 1550
            timeForDeath = pathSize*2 + 20
            fBody = arrayOf(WORK, CARRY, MOVE)
            for (i in 0 until (needCapacity/100)) fBody += arrayOf(CARRY, CARRY, MOVE)
        }
        return CacheCarrier(default = false, tickRecalculate = Game.time, needCarriers = needCarriers, timeForDeath = timeForDeath, needBody = fBody, mPath = ret.path)
    }
}