package logic.production.mineral

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import mainContext.dataclass.RESOURCES_ALL
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant

class LMMineralFillData (val mc: MainContext) {
    private fun getNeedEnergy():Int {
        val qtyRoom2Lvl = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 2 }.size
        val qtyRoom3Lvl = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 3 }.size
        return qtyRoom2Lvl * 120000 + qtyRoom3Lvl*250000
    }

    private fun getQuantityEnergy():Int {
        return mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() >= 2 }.sumBy { it.getResource(RESOURCE_ENERGY) }
    }

    fun fill() {
        mc.constants.accountInit.initMineral(mc)
        for (res in RESOURCES_ALL) {
            val quantity: Int = if (res == RESOURCE_ENERGY) getQuantityEnergy() else
                    mc.mainRoomCollector.rooms.values.sumBy { it.getResource(res) }
            val need: Int =
                    if (res == RESOURCE_ENERGY) getNeedEnergy() else
                    mc.mainRoomCollector.rooms.values.sumBy { it.needMineral[res] ?: 0 }
            val mineralDataRecord: MineralDataRecord? = mc.mineralData[res]
            if (mineralDataRecord == null) {
                if (quantity != 0 || need != 0)
                    mc.mineralData[res] = MineralDataRecord(quantity = quantity, need = need)
            } else {
                mineralDataRecord.quantity = quantity
                mineralDataRecord.need = need
                mineralDataRecord.quantityDown = 0
                mineralDataRecord.quantityUp = 0
            }
        }
    }

    fun fillPrices() {
        mc.mineralData[RESOURCE_ENERGY]?.avgBuyPrice = mc.lm.production.lmMarket.getAverageBuyPrice(RESOURCE_ENERGY)
        console.log("Test 1: ${mc.lm.production.lmMarket.getEnergyPrice()}")
        for (res in mc.mineralData.keys) {
            if (res == RESOURCE_ENERGY) {
                continue
            }
            mc.mineralData[res]?.avgBuyPrice = mc.lm.production.lmMarket.getAverageBuyPrice(res)
            mc.mineralData[res]?.avgSellPrice = mc.lm.production.lmMarket.getAverageSellPrice(res)
            //console.log("Test 1: $res Buy: ${mc.mineralData[res]?.avgBuyPrice}   Sell: ${mc.mineralData[res]?.avgSellPrice}")
        }
    }
}