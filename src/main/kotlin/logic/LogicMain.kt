package logic

import logic.balance.LMBalanceBuilderWall
import logic.balance.LMBalancePrediction
import logic.balance.LMBalanceUpgrader
import logic.building.LMBuilding
import logic.cash.LMCash
import logic.creep.LMCreep
import logic.defence.LMDefence
import logic.develop.LMDevelopMatrix
import logic.develop.LMDevelopTests
import logic.directcontrol.LMDirectControl
import logic.extfunc.LMFunc
import logic.harvest.LMHarvestCacheRecordRoom
import logic.harvest.LMHarvestGetCarrierAuto
import logic.harvest.LMHarvestGetWayFromPosToPos
import logic.main.LMGCL
import logic.messenger.LMMessenger
import logic.nuker.LMNuker
import logic.production.LMProduction
import logic.terminal.LMTerminal
import mainContext.MainContext

class LogicMain(val mc: MainContext) {
    val gcl: LMGCL = LMGCL(mc)
    val terminal: LMTerminal = LMTerminal(mc)
    val production: LMProduction = LMProduction(mc)
    val defence: LMDefence = LMDefence(mc)
    val messenger: LMMessenger = LMMessenger(mc)
    val creep: LMCreep = LMCreep(mc)
    val develop: LMDevelopMatrix = LMDevelopMatrix(mc)
    val building: LMBuilding = LMBuilding(mc)
    val lmHarvestCacheRecordRoom: LMHarvestCacheRecordRoom = LMHarvestCacheRecordRoom(mc)
    val lmHarvestGetCarrierAuto: LMHarvestGetCarrierAuto = LMHarvestGetCarrierAuto()
    val lmHarvestGetWayFromPosToPos: LMHarvestGetWayFromPosToPos = LMHarvestGetWayFromPosToPos()
    val lmDirectControl: LMDirectControl = LMDirectControl(mc)
    val nuker:LMNuker = LMNuker(mc)

    val balanceBuilderWall: LMBalanceBuilderWall = LMBalanceBuilderWall(mc)
    val balanceUpgrader: LMBalanceUpgrader = LMBalanceUpgrader(mc)
    val balancePrediction: LMBalancePrediction = LMBalancePrediction(mc)

    val cash: LMCash = LMCash(mc)

    val lmDevelopTests: LMDevelopTests = LMDevelopTests(mc)
    val func: LMFunc = LMFunc()
}