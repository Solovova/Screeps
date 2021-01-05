package logic.balance

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.Structure

class LMBalanceEnergyBuilder(val mc: MainContext) {
    private fun getMinHits(mainRoom: MainRoom): Int {
        val structure: Structure = mainRoom.room.find(FIND_STRUCTURES).filter {
            (it.structureType == STRUCTURE_RAMPART || it.structureType == STRUCTURE_WALL)
        }.minBy { it.hits } ?: return 0
        return structure.hits
    }

    private fun getNormalizedHits(mainRoom: MainRoom): Double {
        return mainRoom.constant.defenceMinHits.toDouble()
    }

    private fun refreshDefenceMinHits() {
        for (room in mc.mainRoomCollector.rooms.values)
            room.constant.defenceMinHits = getMinHits(room)
    }

    private fun getDefenceLimitUpgrade(mainRoom: MainRoom): Int {
        return if (mainRoom.constant.defenceLimitUpgrade == 0) {
            mc.constants.globalConstant.defenceLimitUpgrade
        }else{
            mainRoom.constant.defenceLimitUpgrade
        }
    }

    fun setNeedBuilderOrEnergySell() {
        val qtyBuilder = mc.lm.balancePrediction.getBuilder()

        var counter = mc.mainRoomCollector.rooms.values.filter { it.have[10] > 0 || it.have[8] > 0}.size

        mc.lm.messenger.log("INFO", "Glob", "Builder have: $counter Target:$qtyBuilder Deficit: ${qtyBuilder - counter}")

        if (Game.time % 11 != 0) return
        for (room in mc.mainRoomCollector.rooms.values) room.constant.needBuilder = false

        refreshDefenceMinHits()


        val rooms = mc.mainRoomCollector.rooms.values.filter {
            it.constant.defenceMinHits != 0
                    && it.constant.defenceMinHits < getDefenceLimitUpgrade(it)
                    && it.have[10] == 0
                    && it.have[8] == 0
        }.sortedBy { this.getNormalizedHits(it) }

        for (room in rooms) {
            counter++
            room.constant.needBuilder = (counter <= qtyBuilder)
        }
    }
}