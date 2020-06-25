package logic.cash

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.Game
import screeps.api.Memory
import screeps.api.get
import screeps.api.set
import screeps.api.structures.StructureContainer
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
            result[room.name]["cnt"] = room.structureContainer.keys.toTypedArray()
            result[room.name]["cntc"] = room.structureContainerNearController.values.map { it.id }.toTypedArray()
            result[room.name]["cntm"] = room.structureContainerNearMineral.values.map { it.id }.toTypedArray()
            result[room.name]["cnts"] = room.structureContainerNearSource.values.map { it.id }.toTypedArray()
        }
        Memory["cash"] = result
    }

    private fun checkCash(mr: MainRoom, name: String): Boolean {
        if (!mr.constant.useCash) return false
        if (Memory["cash"] == null
                || Memory["cash"][mr.name] == null
                || Memory["cash"][mr.name][name] == null) return false
        return true
    }

    fun getFromCashLab(mr: MainRoom): Map<String, StructureLab>? {
        if (!checkCash(mr,"lab")) return null

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
        if (!checkCash(mr,"ext")) return null

        val cash: dynamic = Memory["cash"][mr.name]["ext"] ?: return null

        val result:MutableMap<String, StructureExtension> = mutableMapOf()
        for (record in cash) {
            val ext: StructureExtension = Game.getObjectById<StructureExtension>(record.unsafeCast< String>()) ?: return null
            result[record.unsafeCast< String>()] = ext
        }
        if (result.size!=60) return null
        return result
    }

    fun getFromCashCnt(mr: MainRoom): Map<String, StructureContainer>? {
        if (!checkCash(mr,"cnt")) return null

        val cash: dynamic = Memory["cash"][mr.name]["cnt"] ?: return null

        val result:MutableMap<String, StructureContainer> = mutableMapOf()
        for (record in cash) {
            val cnt: StructureContainer = Game.getObjectById<StructureContainer>(record.unsafeCast< String>()) ?: return null
            result[record.unsafeCast< String>()] = cnt
        }
        if (result.size!=1) return null
        return result
    }

    fun getFromCashCntC(mr: MainRoom): Map<Int, StructureContainer>? {
        if (!checkCash(mr,"cntc")) return null

        val cash: dynamic = Memory["cash"][mr.name]["cntc"] ?: return null

        val result:MutableMap<Int, StructureContainer> = mutableMapOf()
        var index = 0
        for (record in cash) {
            val cnt: StructureContainer = Game.getObjectById<StructureContainer>(record.unsafeCast< String>()) ?: return null
            result[index] = cnt
            index++
        }
        if (result.isNotEmpty()) return null
        return result
    }

    fun getFromCashCntM(mr: MainRoom): Map<Int, StructureContainer>? {
        if (!checkCash(mr,"cntm")) return null

        val cash: dynamic = Memory["cash"][mr.name]["cntm"] ?: return null

        val result:MutableMap<Int, StructureContainer> = mutableMapOf()
        var index = 0
        for (record in cash) {
            val cnt: StructureContainer = Game.getObjectById<StructureContainer>(record.unsafeCast< String>()) ?: return null
            result[index] = cnt
            index++
        }
        if (result.size!=1) return null
        return result
    }

    fun getFromCashCntS(mr: MainRoom): Map<Int, StructureContainer>? {
        if (!checkCash(mr,"cnts")) return null

        val cash: dynamic = Memory["cash"][mr.name]["cnts"] ?: return null

        val result:MutableMap<Int, StructureContainer> = mutableMapOf()
        var index = 0
        for (record in cash) {
            val cnt: StructureContainer = Game.getObjectById<StructureContainer>(record.unsafeCast< String>()) ?: return null
            result[index] = cnt
            index++
        }
        if (result.isNotEmpty()) return null
        return result
    }
}