package logic.terminal

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap
import kotlin.math.max
import kotlin.math.min

class LMTerminal(val mainContext: MainContext) {
    private val emergencyMineralQuantity = 30000
    private val sentQuantity = 5000

    fun transactions() {
        this.terminalSentEnergyEmergency()
        this.terminalSentEnergyPriorityRoom()
        this.terminalSentEnergyForBuild()
        this.terminalSentMineral()

        this.terminalSentEnergyExcessSent()
        this.terminalSentEnergyForWallUpgrader()
        this.terminalSentEnergyFrom3To2()
        this.terminalSentEnergyStorageFullSent()

    }

    private fun terminalSentFromTo(mainRoomFrom: MainRoom, mainRoomTo: MainRoom, describe: String) {

        val terminalFrom: StructureTerminal = mainRoomFrom.structureTerminal[0] ?: return
        val terminalTo: StructureTerminal = mainRoomTo.structureTerminal[0] ?: return

        if (terminalFrom.cooldown == 0 && terminalTo.cooldown == 0) {
            //
            val cost = Game.market.calcTransactionCost(sentQuantity, mainRoomFrom.name, mainRoomTo.name)
            if (Memory["transCost"] == null) {
                Memory["transCost"] = 0
                Memory["transCount"] = 0
                Memory["transStartTick"] = Game.time
            }
            Memory["transCost"] = Memory["transCost"] + cost
            Memory["transCount"] = Memory["transCount"] + 1
            //
            val result = terminalFrom.send(RESOURCE_ENERGY, sentQuantity, mainRoomTo.name)
            if (result == OK) {
                mainContext.lm.messenger.log("INFO", mainRoomFrom.name,
                        "Send energy $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}   $describe", COLOR_GREEN)
            } else {
                mainContext.lm.messenger.log("ERROR", mainRoomFrom.name,
                        "Send energy Error: $result cost: $cost quantity: $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}   $describe", COLOR_GREEN)
            }

        }
    }

    private fun terminalSentMineral() {
        for (roomTo in mainContext.mainRoomCollector.rooms.values) {
            val terminalTo = roomTo.structureTerminal[0] ?: continue
            if (terminalTo.cooldown != 0) continue
            for (needResourceRecord in roomTo.needMineral) {
                val needResource = needResourceRecord.key
                var needResourceQuantity = needResourceRecord.value - roomTo.getResource(needResourceRecord.key)

                val canMineralTakeTerminal = roomTo.constant.mineralAllMaxTerminal - (terminalTo.store.toMap().map { it.value }.sum() - roomTo.getResourceInTerminal(RESOURCE_ENERGY))

                needResourceQuantity = min(needResourceQuantity, canMineralTakeTerminal)

                if (needResourceQuantity <= 0) continue
                needResourceQuantity = max(needResourceQuantity, 100)

                //println("Test" + roomTo.name + ":" + needResource +" " +needResourceQuantity)
                val roomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values
                        .filter {
                            it.name != roomTo.name
                                    && it.structureTerminal[0] != null
                                    && it.structureTerminal[0]?.cooldown == 0
                                    && (it.getResource(needResource) - (it.needMineral[needResource]
                                    ?: 0)) > 100
                        }
                        .maxBy {
                            it.getResource(needResource) - (it.needMineral[needResource] ?: 0)
                        }
                        ?: continue


                val haveResourceQuantityInTerminal = roomFrom.getResourceInTerminal(needResource)
                val haveResourceQuantity = roomFrom.getResource(needResource) - (roomFrom.needMineral[needResource]
                        ?: 0)

                val quantityTransfer = min(min(
                        haveResourceQuantity, needResourceQuantity),
                        mainContext.constants.globalConstant.sentMaxMineralQuantity)
                //println("Test<----" + roomFrom.name + " " + quantityTransfer + " have in terminal:"+ haveResourceQuantityInTerminal)
                //wait because not all resource transfer from storage to terminal
                if (haveResourceQuantityInTerminal < quantityTransfer) continue
                if (quantityTransfer < 100) continue
                val terminalFrom: StructureTerminal = roomFrom.structureTerminal[0]
                        ?: continue
                val result = terminalFrom.send(needResource, quantityTransfer, roomTo.name)
                if (result == OK) {
                    mainContext.lm.messenger.log("INFO", roomFrom.name,
                            "Send $needResource $quantityTransfer from ${roomFrom.name} -> ${roomTo.name} ", COLOR_YELLOW)
                    return
                }
            }
        }

    }

    private fun terminalSentEnergyForBuild() {

        //Build to
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResource() < it.constant.energyBuilder
                    && it.constructionSite.isNotEmpty()
        }.minBy { it.getResource() }
                ?: return

        //Take max room resource, but priority lvl3
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom >= 2
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResource() > (emergencyMineralQuantity + 20000)
                    && it.constructionSite.isEmpty()
                    && it.getResourceInTerminal() > 8000
                    && it.name != mainRoomTo.name
        }.maxBy {
            it.getResource() + if (it.constant.levelOfRoom == 3) {
                1000000
            } else {
                0
            }
        }
                ?: return


        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "For building")
    }

    private fun terminalSentEnergyEmergency() {
        //Emergency to
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResource() < emergencyMineralQuantity
        }.minBy { it.getResource() }
                ?: return

        //Take max room resource, but priority lvl3
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom >= 2
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResource() > emergencyMineralQuantity
                    && it.getResourceInTerminal() > 8000
        }.maxBy {
            it.getResource() + if (it.constant.levelOfRoom == 3) {
                1000000
            } else {
                0
            }
        }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "Emergency")
    }

    private fun terminalSentEnergyFrom3To2() {
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.constant.levelOfRoom == 2
                    && it.getResource() < (it.constant.energyUpgradeForce + 10000)
        }.minBy { it.getResource() }
                ?: return

        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom == 3
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResourceInTerminal() > 8000
                    && it.getResource() > (it.constant.energyUpgradeDefence + 20000)
        }.maxBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "From3To2")
    }

    private fun terminalSentEnergyPriorityRoom() {
        if (mainContext.constants.globalConstant.terminalPriorityRoom == "") {
            return
        }

        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.constant.levelOfRoom == 2
                    && it.name == mainContext.constants.globalConstant.terminalPriorityRoom
                    && it.getResource() < (it.constant.energyUpgradeForce + 30000)
        }.minBy { it.getResource() }
                ?: return

        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom >= 2
                    && it.getResourceInTerminal() > 8000
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.name != mainRoomTo.name
                    && it.getResource() > (it.constant.energyBuilder + 20000)
        }.maxBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "PriorityRoom")
    }

    private fun terminalSentEnergyExcessSent() {
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResourceInTerminal() > 8000
                    && it.getResource() > it.constant.energyExcessSent
        }.maxBy { it.getResource() }
                ?: return

        //Upgrade and less when energyUpgradeDefence
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && ((it.constant.needBuilder && it.getResource() < it.constant.energyUpgradeDefence)
                    || it.getResource() < it.constant.energyUpgradeLvl8Controller)
                    && it.name != mainRoomFrom.name
        }.minBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "ExcessSent")
    }

    private fun terminalSentEnergyStorageFullSent() {
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResourceInTerminal() > 8000
                    && it.getResource() > 500_000
        }.maxBy { it.getResource() }
                ?: return

        //Upgrade and less when energyUpgradeDefence
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResource() < 450_000
                    && it.name != mainRoomFrom.name
        }.minBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "StorageFullSent")
    }

    private fun terminalSentEnergyForWallUpgrader() {
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && ((it.constant.needBuilder && it.getResource() < it.constant.energyUpgradeDefence)
                    || (it.constant.needUpgrader && it.getResource() < it.constant.energyUpgradeLvl8Controller))
                    && it.getLevelOfRoom() == 3
        }.minBy { it.constant.defenceMinHits }
                ?: return

        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.structureTerminal[0]?.cooldown == 0
                    && it.getResource() > it.constant.energyExcessSent
                    && it.getLevelOfRoom() == 3
                    && !((it.constant.needBuilder && it.getResource() < it.constant.energyUpgradeDefence)
                    || (it.constant.needUpgrader && it.getResource() < it.constant.energyUpgradeLvl8Controller))
                    && it.name != mainRoomTo.name
                    && it.getResourceInTerminal() > 8000
        }.maxBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "ForWallUpgrader")
    }
}