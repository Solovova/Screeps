package logic.production

import logic.production.lab.*
import logic.production.market.LMMarket
import logic.production.mineral.*
import mainContext.MainContext

class LMProduction(val mc: MainContext) {
    val lmLabMainRoomRun: LMLabMainRoomRun = LMLabMainRoomRun(mc)
    val lmLabMainRoomGetLabSorted: LMLabMainRoomGetLabSorted = LMLabMainRoomGetLabSorted()

    val lmMineralSetGlobalConstant: LMMineralSetGlobalConstant = LMMineralSetGlobalConstant()
    val lmMineralFillData: LMMineralFillData = LMMineralFillData(mc)
    val lmMineralFillProduction: LMMineralFillProduction = LMMineralFillProduction(mc)

    val lmMarket: LMMarket = LMMarket(mc)

    val labFunc: LMLabFunc = LMLabFunc(mc)

    val labBalancing: LMLabReactionBalance = LMLabReactionBalance(mc)
    val mineralHarvest: LMMineralHarvest = LMMineralHarvest(mc)

    val mineralFillCash: LMMineralFillCash = LMMineralFillCash(mc)
}