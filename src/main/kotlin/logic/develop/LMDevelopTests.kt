package logic.develop

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.Game
import screeps.api.Memory
import screeps.api.get
import screeps.api.set
import screeps.api.structures.StructureExtension
import screeps.api.structures.StructureLab

class LMDevelopTests(val mc: MainContext) {
    val lmDevelopTestsCacheCarrier: LMDevelopTestsCacheCarrier = LMDevelopTestsCacheCarrier(mc)

    fun oldModeControllerShow() {
        val cpUse = LMDevelopCPUUse()
        var cpu = cpUse.cutoff(0.0,"Old Start")

        for (room in mc.mainRoomCollector.rooms.values)
            for (lab in room.structureController.values){
                val tid = lab.id
            }

        cpu = cpUse.cutoff(cpu,"Old Stop")
    }

    fun oldModeExtShow() {
        val cpUse = LMDevelopCPUUse()
        var cpu = cpUse.cutoff(0.0,"Old Start")

        for (room in mc.mainRoomCollector.rooms.values)
            for (lab in room.structureExtension.values){
                val tid = lab.id
            }

        cpu = cpUse.cutoff(cpu,"Old Stop")
    }

    fun oldModeLabShow() {
        val cpUse = LMDevelopCPUUse()
        var cpu = cpUse.cutoff(0.0,"Old Start")

        for (room in mc.mainRoomCollector.rooms.values)
            for (lab in room.structureLab.values){
                val tid = lab.id
            }

        cpu = cpUse.cutoff(cpu,"Old Stop")
    }

    fun oldModeSortedLabShow() {
        val cpUse = LMDevelopCPUUse()
        var cpu = cpUse.cutoff(0.0,"Old Start")

        for (room in mc.mainRoomCollector.rooms.values)
            for (lab in room.structureLabSort.values){
                val tid = lab.id
            }

        cpu = cpUse.cutoff(cpu,"Old Stop")
    }

    fun saveInCash() {
        val cpUse = LMDevelopCPUUse()
        var cpu = cpUse.cutoff(0.0,"saveInCash Start")

        val result: dynamic = object {}
        for (room in mc.mainRoomCollector.rooms.values) {
            result[room.name] = object {}
            result[room.name]["lab"] = Array(room.structureLabSort.size) {room.structureLabSort[it]?.id ?: "nf"}
        }
        Memory["cash"] = result

        cpu = cpUse.cutoff(cpu,"saveInCash Stop")
    }

    fun loadFromCash() {
        val cpUse = LMDevelopCPUUse()
        var cpu = cpUse.cutoff(0.0,"loadFromCash Start")

        var tests = true
        for (room in mc.mainRoomCollector.rooms.values) {
            val cash: dynamic = Memory["cash"][room.name]["lab"] ?: continue
            val testLab:MutableMap<Int,StructureLab> = mutableMapOf()
            var index = 0
            for (tid in cash) {
                val lab: StructureLab = Game.getObjectById<StructureLab>(tid.unsafeCast< String>()) ?: continue
                testLab[index] = lab
                index++
            }

            for (lab in room.structureLabSort) {
                if (lab.value.id != testLab[lab.key]?.id) tests = false
            }

            println("${room.name} test is: $tests")
        }

        cpu = cpUse.cutoff(cpu,"loadFromCash Stop")
    }

    //Test
    fun saveToCash() {
        val result: dynamic = object {}
        for (room in mc.mainRoomCollector.rooms.values) {
            result[room.name] = object {}
            result[room.name]["lab"] = room.structureLab.keys.toTypedArray()
            result[room.name]["ext"] = room.structureExtension.keys.toTypedArray()
        }
        Memory["cash"] = result
    }

    fun getFromCashLab(mr: MainRoom): Map<String,StructureLab>? {
        return null
        val cash: dynamic = Memory["cash"][mr.name]["lab"] ?: return null

        val result:MutableMap<String,StructureLab> = mutableMapOf()
        for (tid in cash) {
            val lab: StructureLab = Game.getObjectById<StructureLab>(tid.unsafeCast< String>()) ?: return null
            result[tid.unsafeCast< String>()] = lab
        }
        return result
    }

    fun getFromCashExt(mr: MainRoom): Map<String,StructureExtension>? {
        return null
        val cash: dynamic = Memory["cash"][mr.name]["ext"] ?: return null

        val result:MutableMap<String,StructureExtension> = mutableMapOf()
        for (tid in cash) {
            val ext: StructureExtension = Game.getObjectById<StructureExtension>(tid.unsafeCast< String>()) ?: return null
            result[tid.unsafeCast< String>()] = ext
        }
        return result
    }
}