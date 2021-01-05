package logic.balance

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import screeps.api.Game
import screeps.api.RESOURCE_ENERGY

class LMBalancePrediction(val mc: MainContext) {
    var addedNew: Boolean = false
    fun logShow() {
        var numShow = 10
        var index = mc.constants.globalConstant.balanceNeedEnergy.size - 1
        var result = " "

        while (true) {
            if (index < 0 || numShow < 0) break
            result += "( ${mc.constants.globalConstant.balanceQtyUpgrader[index]} ) ${mc.constants.globalConstant.balanceNeedEnergy[index]} , "
            index--
            numShow--
        }
        mc.lm.messenger.log("INFO", "Glob", result)
    }

    fun logSave(qtyUpgrader: Int, needMineral: Int) {
        if (Game.time % mc.constants.globalConstant.balancePeriod != 0) return
        var addedNew = true

        if (mc.constants.globalConstant.balanceNeedEnergy.size >= mc.constants.globalConstant.balanceMaxSize) {
            mc.constants.globalConstant.balanceNeedEnergy =
                    mc.constants.globalConstant.balanceNeedEnergy
                            .drop(mc.constants.globalConstant.balanceNeedEnergy.size - mc.constants.globalConstant.balanceMaxSize + 1).toTypedArray()
        }

        if (mc.constants.globalConstant.balanceQtyUpgrader.size >= mc.constants.globalConstant.balanceMaxSize) {
            mc.constants.globalConstant.balanceQtyUpgrader =
                    mc.constants.globalConstant.balanceQtyUpgrader
                            .drop(mc.constants.globalConstant.balanceQtyUpgrader.size - mc.constants.globalConstant.balanceMaxSize + 1).toTypedArray()
        }

        val sizeBalanceQtyUpgrader: Int = mc.constants.globalConstant.balanceQtyUpgrader.size
        mc.constants.globalConstant.balanceQtyUpgrader[sizeBalanceQtyUpgrader] = qtyUpgrader

        val sizeBalanceNeedEnergy: Int = mc.constants.globalConstant.balanceNeedEnergy.size
        mc.constants.globalConstant.balanceNeedEnergy[sizeBalanceNeedEnergy] = needMineral
    }

    private fun getUpgraderPrediction(qtyUpgraderMin: Int, qtyUpgraderMax: Int): Int {

        if (mc.constants.globalConstant.balanceNeedEnergy.size >= 2) {
            val oneUpgraderUse = 20500
            val qtyUpgraderNow = mc.constants.globalConstant.balanceQtyUpgraderNow
            val last0Energy = mc.constants.globalConstant.balanceNeedEnergy[mc.constants.globalConstant.balanceNeedEnergy.size - 1]
            val last1Energy = mc.constants.globalConstant.balanceNeedEnergy[mc.constants.globalConstant.balanceNeedEnergy.size - 2]
            var strPrediction = "Prediction "
            val energyDynamic = last1Energy - last0Energy

            //mainContext.constants.globalConstant.balanceQtyUpgraderNow = 32

            strPrediction += "now: $qtyUpgraderNow "
            strPrediction += "min: $qtyUpgraderMin "
            strPrediction += "energy dynamic: $energyDynamic "


            var qtyPrediction = qtyUpgraderNow

            if (qtyPrediction <= qtyUpgraderMin) {
                qtyPrediction = qtyUpgraderMin
            } else {
                if (last0Energy > 0) {
                    if (energyDynamic < 0) {
                        qtyPrediction -= if (-energyDynamic > oneUpgraderUse * 3) {
                            2
                        } else {
                            1
                        }
                    }
                } else {
                    if (energyDynamic > 0) {
                        qtyPrediction += if (energyDynamic > oneUpgraderUse * 3) {
                            2
                        } else {
                            1
                        }
                    }
                }
            }

            if (qtyPrediction > qtyUpgraderMax) qtyPrediction = qtyUpgraderMax

            strPrediction += "prediction: $qtyPrediction "

            mc.lm.messenger.log("INFO", "Glob", strPrediction)

            if (addedNew) {
                addedNew = false
                //ToDo
                //mainContext.constants.globalConstant.balanceQtyUpgraderNow = strPrediction
            }
        }

        var result: Int = mc.constants.globalConstant.balanceQtyUpgraderNow

        if (result == -1) {
            result = mc.constants.globalConstant.balanceQtyUpgraderDefault

        }

        mc.constants.globalConstant.balanceQtyUpgraderNow = result
        return result
    }

    fun getUpgrader(qtyUpgraderMin: Int, qtyUpgraderMax: Int): Int {
        val qtyUpgrader = mc.constants.globalConstant.balanceQtyUpgraderDefault
        val qtyUpgraderPrediction = getUpgraderPrediction (qtyUpgraderMin, qtyUpgraderMax)
//        if (qtyUpgrader == -1) {
//            qtyUpgrader = qtyUpgraderPrediction
//        }
        return qtyUpgrader

    }

    private fun getBuilderPrediction(): Int {
        var qtyBuilderPrediction = mc.getNumRoomWithTerminal() / 4
        if (qtyBuilderPrediction == 0) qtyBuilderPrediction = 1
        return qtyBuilderPrediction
    }



    fun getBuilder() : Int {
        val mineralsNeed = (mc.mineralData[RESOURCE_ENERGY]?.need
                ?: 0) - (mc.mineralData[RESOURCE_ENERGY]?.quantity ?: 0)

        var qtyBuilder = mc.constants.globalConstant.balanceQtyBuilderDefault

        val qtyLvl8Room = mc.mainRoomCollector.rooms.values.filter { it.constant.levelOfRoom == 3 }.size

        if (mineralsNeed < -8000 * qtyLvl8Room && Game.market.credits < 10_000_000) {
            println("Test:  sellDirect $mineralsNeed ${-8000 * qtyLvl8Room}")
            val mineralDataRecord: MineralDataRecord? = mc.mineralData[RESOURCE_ENERGY]
            if (mineralDataRecord != null) {
                mc.lm.production.lmMarket.sellDirect(RESOURCE_ENERGY, mineralDataRecord, 0.1, true)
            }
        }

        if (mineralsNeed < -4000 * qtyLvl8Room && Game.market.credits < 10_000_000) {
            val mineralDataRecord: MineralDataRecord? = mc.mineralData[RESOURCE_ENERGY]
            if (mineralDataRecord != null) {
                mc.lm.production.lmMarket.sellOrderCreate(RESOURCE_ENERGY, mineralDataRecord, true)
            }
        }

        if (mineralsNeed<-40000*qtyLvl8Room) {
            qtyBuilder += 16
        }

//        if (qtyBuilder == -1) {
//            qtyBuilder = getBuilderPrediction()
//        }
        return qtyBuilder
    }
}