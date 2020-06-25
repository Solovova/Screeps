package logic.production.mineral

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.COLOR_RED
import screeps.api.structures.StructureStorage
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap

class LMMineralFillCash(val mc: MainContext) {
    private fun fillRoom(mr: MainRoom) {
        val store: StructureStorage? = mr.structureStorage[0]
        if (store != null)
            for (record in store.store.toMap()) mr.resStorage[record.key] = (mr.resStorage[record.key]
                    ?: 0) + record.value

        val terminal: StructureTerminal? = mr.structureTerminal[0]
        if (terminal != null)
            for (record in terminal.store.toMap()) mr.resTerminal[record.key] = (mr.resTerminal[record.key]
                    ?: 0) + record.value
    }

    fun fill() {
        for (mr in mc.mainRoomCollector.rooms.values) {
            try {
                fillRoom(mr)
            } catch (e: Exception) {
                mc.lm.messenger.log("ERROR", "Room in start of tick", mr.name, COLOR_RED)
            }
        }
    }
}