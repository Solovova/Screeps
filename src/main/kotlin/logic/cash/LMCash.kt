package logic.cash

import mainContext.MainContext

class LMCash(val mc: MainContext) {
    val mr: LMCashMainRoom = LMCashMainRoom(mc)
}