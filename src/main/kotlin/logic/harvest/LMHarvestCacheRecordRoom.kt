package logic.harvest

import mainContext.constants.path.CacheCarrier
import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom

class LMHarvestCacheRecordRoom(val mc:MainContext) {
    fun gets(type: String, mainRoom: MainRoom, slaveRoom: SlaveRoom? = null, recalculate: Boolean = false, inSwampCost: Int = 10, inPlainCost: Int = 2, doNotCalculateRoads: Boolean = false, safeMove: Boolean = false) : CacheCarrier? {
        return mc.constants.globalConstant.containerCacheCarrier.gets(type,mainRoom,slaveRoom,recalculate,inSwampCost,inPlainCost,doNotCalculateRoads,safeMove)
    }
}