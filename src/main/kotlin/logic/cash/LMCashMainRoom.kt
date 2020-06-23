package logic.cash

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.Game
import screeps.api.Memory
import screeps.api.get
import screeps.api.set
import screeps.api.structures.StructureExtension
import screeps.api.structures.StructureLab

class LMCashMainRoom(val mc: MainContext) {
    private var needSave: Boolean = false

    fun needSave() {
        needSave = true
    }

    fun saveToCash() {
        if (!needSave) return
        needSave = false
        val result: dynamic = object {}
        for (room in mc.mainRoomCollector.rooms.values) {
            result[room.name] = object {}
            result[room.name]["lab"] = room.structureLab.keys.toTypedArray()
            result[room.name]["ext"] = room.structureExtension.keys.toTypedArray()
        }
        Memory["cash"] = result
    }

    fun getFromCashLab(mr: MainRoom): Map<String, StructureLab>? {
        if (!mr.constant.useCash) return null

        val cash: dynamic = Memory["cash"][mr.name]["lab"] ?: return null
        val result:MutableMap<String, StructureLab> = mutableMapOf()
        for (record in cash) {
            val lab: StructureLab = Game.getObjectById<StructureLab>(record.unsafeCast< String>()) ?: return null
            result[record.unsafeCast< String>()] = lab
        }

        if (result.size!=10) return null

        return result
    }

    fun getFromCashExt(mr: MainRoom): Map<String, StructureExtension>? {
        if (!mr.constant.useCash) return null

        val cash: dynamic = Memory["cash"][mr.name]["ext"] ?: return null

        val result:MutableMap<String, StructureExtension> = mutableMapOf()
        for (record in cash) {
            val ext: StructureExtension = Game.getObjectById<StructureExtension>(record.unsafeCast< String>()) ?: return null
            result[record.unsafeCast< String>()] = ext
        }
        if (result.size!=60) return null
        return result
    }
}