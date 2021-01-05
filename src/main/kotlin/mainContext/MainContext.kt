package mainContext

import mainContext.tasks.Tasks
import battleGroup.BattleGroupContainer
import mainContext.constants.Constants
import logic.LogicMain
import mainContext.dataclass.MineralDataRecord
import mainContext.mainRoomCollecror.MainRoomCollector
import screeps.api.*
import kotlin.random.Random

class MainContext {
    val lm: LogicMain = LogicMain(this)
    var flags:List<Flag> = listOf()
    val messengerMap: MutableMap<String, String> = mutableMapOf()
    val mineralData: MutableMap<ResourceConstant, MineralDataRecord> = mutableMapOf()

    val constants: Constants = Constants(this)
    val tasks: Tasks = Tasks(this)
    var mainRoomCollector: MainRoomCollector = MainRoomCollector(this, arrayOf())

    val battleGroupContainer: BattleGroupContainer = BattleGroupContainer(this)

    init {
        //lm.production.lmMarket.showSellOrdersRealPrice(RESOURCE_ENERGY)//"XLH2O".unsafeCast<ResourceConstant>())
        //lm.production.lmMarket.showBuyOrdersRealPrice(RESOURCE_ENERGY)//"XLH2O".unsafeCast<ResourceConstant>())
    }

    fun run() {
        flags = Game.flags.values.toList()

        this.mainRoomCollector = MainRoomCollector(this, this.constants.mainRoomsInit)



        lm.gcl.calculate()

        lm.production.mineralFillCash.fill()
        lm.nuker.lmNukerNeedMineral.fill()

        lm.production.lmMineralFillData.fill()
        lm.production.lmMineralFillData.fillPrices()

        lm.production.lmMineralFillProduction.fill()

        this.constants.accountInit.initTuning(this)




        this.mainRoomCollector.creepsCalculate()
        lm.balanceUpgrader.setNeedUpgrader()
        lm.balanceEnergyBuilder.setNeedBuilderOrEnergySell()



        this.mainRoomCollector.creepsCalculateProfit()

        for (room in this.mainRoomCollector.rooms.values) {
            try {
                room.runInStartOfTick()
            } catch (e: Exception) {
                this.lm.messenger.log("ERROR", "Room in start of tick", room.name, COLOR_RED)
            }
        }




        this.battleGroupContainer.runInStartOfTick()

        //Not every tick
        this.mainRoomCollector.runNotEveryTick()

        if (Game.time % 10 == 0) {
            lm.production.lmMarket.sellBuy()
        }

        if (this.setNextTickRun()) {

            this.tasks.deleteTaskDiedCreep()
            this.battleGroupContainer.runNotEveryTick()
            lm.production.lmMarket.deleteEmptyOffers()
        }

        lm.production.labBalancing.balancing()

        lm.lmDirectControl.runs()
        this.lm.production.lmLabMainRoomRun.run()
        this.battleGroupContainer.runInEndOfTick()
        this.mainRoomCollector.runInEndOfTick()
        lm.terminal.transactions()

        //this.lm.defence.lmMainRoomDefenceArea.clearAllDefArea(this)
        //this.lm.defence.lmMainRoomDefenceArea.calculateAllDefArea(this)

        this.lm.cash.mr.saveToCash()
        this.tasks.toMemory()
        this.constants.toMemory()

        lm.messenger.show()
    }

    private fun setNextTickRun(): Boolean {
        if (this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext > Game.time) return false
        this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext = Game.time + Random.nextInt(this.constants.globalConstant.roomRunNotEveryTickTicksPauseMin,
                this.constants.globalConstant.roomRunNotEveryTickTicksPauseMax)
        this.lm.messenger.log("TEST", "Main context", "Main room not every tick run. Next tick: ${this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext}", COLOR_GREEN)
        return true
    }

    fun getNumRoomWithTerminal():Int {
        return this.mainRoomCollector.rooms.values.filter {
            Game.rooms[it.name] != null
                    && it.structureTerminal[0] != null }.size
    }
}