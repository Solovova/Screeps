package mainContext.constants


import constants.CacheCarrier
import screeps.api.*

class GlobalConstant(val constants: Constants) {
    var username: String = ""
    var nukerFill: Boolean = true
    var nukerFilInRooms: Array<String> = arrayOf()

    val dataCacheCarrierAuto: MutableMap<String, CacheCarrier> = mutableMapOf() //cashed
    val roomRunNotEveryTickTicksPauseMin: Int = 300
    val roomRunNotEveryTickTicksPauseMax: Int = 400
    var roomRunNotEveryTickNextTickRunMainContext: Int = 0
    val battleGroupList: MutableList<String> = MutableList(0){""}
    val sentMaxMineralQuantity: Int = 10000

    var gcl: Int = 0  //cashed
    var gclFromTick: Int = 0 //cashed
    var gclArray: Array<Int> = arrayOf() //cashed
    var gclArrayMaxSize: Int = 100
    var gclPeriod: Int = 1000

    var balanceQtyUpgrader: Array<Int> = arrayOf() //cashed
    var balanceNeedEnergy: Array<Int> = arrayOf() //cashed
    var balanceQtyUpgraderNow: Int = -1 //cashed
    var balanceQtyUpgraderDefault: Int = -1 // if -1 use prediction
    var balanceQtyBuilderDefault: Int = -1 // if -1 use prediction

    var balanceMaxSize: Int = 100
    var balancePeriod: Int = 1500




    //Market
    val marketMinCreditForOpenBuyOrder: Double = 10000000.0

    //INFO
    val showProfitWhenLessWhen: Int = 6000

    //CreepUpgrades
    //if in room set it more priority
    val creepUpgradablePartsRange:MutableMap<Int,Map<BodyPartConstant,List<Pair<Int,ResourceConstant>>>> = mutableMapOf()
    val labReactionComponent: MutableMap<ResourceConstant,Array<ResourceConstant>> = mutableMapOf()

    var defenceLimitUpgrade: Int = 17_000_000

    //Terminal
    var terminalPriorityRoom: String = "" //If level of room not 8 sent mineral to this room

    init {
        constants.mainContext.lm.production.lmMineralSetGlobalConstant.setConstant(this)
    }


    fun toDynamic(): dynamic {
        val result: dynamic = object {}
        result["gcl"] = this.gcl
        result["gclFromTick"] = this.gclFromTick
        result["gclArray"] = this.gclArray
        result["roomRunNotEveryTickNextTickRunMainContext"] = this.roomRunNotEveryTickNextTickRunMainContext

        result["balanceQtyUpgrader"] = this.balanceQtyUpgrader
        result["balanceNeedEnergy"] = this.balanceNeedEnergy
        result["balanceQtyUpgraderNow"] = this.balanceQtyUpgraderNow



        //dataCacheCarrierAuto
        result["dataCacheCarrierAuto"] = object {}
        for (record in dataCacheCarrierAuto)
            result["dataCacheCarrierAuto"][record.key] = record.value.toDynamic()


        //--------------------
        return result
    }

    fun fromDynamic(d: dynamic) {
        if (d["roomRunNotEveryTickNextTickRunMainContext"] != null) this.roomRunNotEveryTickNextTickRunMainContext = d["roomRunNotEveryTickNextTickRunMainContext"] as Int

        if (d["gcl"] != null) this.gcl = d["gcl"] as Int
        if (d["gclFromTick"] != null) this.gclFromTick = d["gclFromTick"] as Int
        if (d["gclArray"] != null) this.gclArray = d["gclArray"] as Array<Int>

        if (d["balanceQtyUpgrader"] != null) this.balanceQtyUpgrader = d["balanceQtyUpgrader"] as Array<Int>
        if (d["balanceNeedEnergy"] != null) this.balanceNeedEnergy = d["balanceNeedEnergy"] as Array<Int>
        if (d["balanceQtyUpgraderNow"] != null) this.balanceQtyUpgraderNow = d["balanceQtyUpgraderNow"] as Int


        //dataCacheCarrierAuto
        if (d["dataCacheCarrierAuto"] != null)
            for (recordKey in js("Object").keys(d["dataCacheCarrierAuto"]).unsafeCast<Array<String>>())
                dataCacheCarrierAuto[recordKey] = CacheCarrier.initFromDynamic(d["dataCacheCarrierAuto"][recordKey])
    }
}