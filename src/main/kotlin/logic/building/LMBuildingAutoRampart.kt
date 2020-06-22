package logic.building

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.Structure
import screeps.utils.terrain

class LMBuildingAutoRampart {


    private fun buildRampartInPos(room: Room, pos: RoomPosition, testMode: Boolean):Boolean {
        val terrain: TerrainConstant = room.getTerrain().get(pos.x,pos.y).terrain
        val isRampart:Boolean = (room.lookForAt(LOOK_STRUCTURES,pos.x,pos.y)?.filter { it.structureType == STRUCTURE_RAMPART }?.size ?: 0) > 0

        if (terrain in setOf(TERRAIN_PLAIN, TERRAIN_SWAMP) && !isRampart) {
            if (!testMode) room.createConstructionSite(pos, STRUCTURE_RAMPART)
            return true
        }
        return false
    }

    private fun buildByMap(mainRoom: MainRoom, listOfStructures : Collection<Structure>, testMode: Boolean, radius: Int = 0):Boolean {
        var buildSome = false

        for (struct in listOfStructures) {
            if (radius==0) {
                if (buildRampartInPos(mainRoom.room,struct.pos, testMode)) buildSome = true
            }else{
                for (tx in (struct.pos.x-radius)..(struct.pos.x+radius))
                    for (ty in (struct.pos.y-radius)..(struct.pos.y+radius)) {
                        if (tx !in 0..49 || ty !in 0..49) continue
                        val pos = RoomPosition(tx,ty,struct.pos.roomName)
                        if (struct.pos.getRangeTo(pos) != radius) continue

                        if (buildRampartInPos(mainRoom.room,pos, testMode)) buildSome = true
                    }
            }

        }

        return buildSome
    }

    fun buildAllNeed(mainRoom: MainRoom, testMode: Boolean = true):Boolean {
        var buildSome = false
        if (mainRoom.room.find(FIND_CONSTRUCTION_SITES).isNotEmpty()) return buildSome


        //Controller 1 radius

        if (buildByMap(mainRoom,mainRoom.structureStorage.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structureTerminal.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structureSpawn.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structureTower.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structureNuker.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structureFactory.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structureObserver.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structurePowerSpawn.values, testMode)) buildSome = true
        if (buildByMap(mainRoom,mainRoom.structureController.values, testMode,1)) buildSome = true

        return buildSome
    }
}