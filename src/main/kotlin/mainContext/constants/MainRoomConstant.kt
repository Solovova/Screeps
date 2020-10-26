package mainContext.constants

import screeps.api.*

class MainRoomConstant(val parent: Constants) {
    var slaveRooms : Array<String> = arrayOf() //simple
    val slaveRoomConstantContainer: MutableMap<String, SlaveRoomConstant> = mutableMapOf() //cashed
    //Energy management
    var energyBuilder : Int = 60000 //simple //how much energy must be in storage for start building
    var energyUpgradeLvl8Controller: Int = 100000 //simple //how much energy must be in storage for start mainContext.dataclass.getUpgrade controller
    var energyUpgradeDefence : Int = 150000
    var energyExcessSent: Int = 250000
    var energyUpgradeLow : Int = 100000 //simple //how much energy must be in storage for start mainContext.dataclass.getUpgrade controller
    var energyUpgradeForce: Int = 110000 //simple //how much energy must be in storage for start mainContext.dataclass.getUpgrade controller

    var roomHostile: Boolean = false
    var roomHostileType: Int = 0 // 1 -  mile, 2 - ranged, 3 - invader
    var roomHostileNum: Int = 0



    var note : String = ""


    //service
    var towerLastTarget: String = ""        //cashed


    //Creep commands
    var creepSpawn: Boolean = true
    var creepUseUpgraderAndBuilderFromMainRoom: Boolean = false
    var needCleaner: Boolean = false //cashed
    var creepIdOfBigBuilder: String = "" //simple
    var creepUseBigBuilder: Boolean = true

    //Room algorithm
    var roomRunNotEveryTickNextTickRun: Int = 0
    var levelOfRoom: Int = 0 //cashed

    var sentEnergyToRoom: String = ""       //simple
    var energyMinStorage: Int = 30000
    var energyMaxStorage: Int = 500000
    var energyMinTerminal: Int = 10000
    var energyMaxTerminal: Int = 60000
    var mineralMinTerminal: Int = 10000
    var mineralAllMaxTerminal: Int = 150000

    var useCash: Boolean = true


    //old
    var marketBuyEnergy: Boolean = false

    //Mineral
    var mineralMaxInRoom: Int = 250000
    var mineralAllMaxInStorage: Int = 500000


    //Reaction control set in ProductionController
    var reactionActive: String = "" //cashed
    var reactionActiveArr: Array<String> = arrayOf()
    var reactionActiveBalancingType: Int = 2

    //Manual defence
    var manualDefenceRoomMainColorFlag: ColorConstant = COLOR_PURPLE
    var manualDefenceGroupPos: RoomPosition? = null //manualDefenceRoomMainColorFlag
    var manualDefenceTargetCreep: Creep? = null

    //Balancing upgrade
    var defenceLimitUpgrade: Int = 0
    var defenceMinHits: Int = 0 //cashed auto calculating
    var needUpgrader:Boolean = false //cashed
    var needBuilder:Boolean = false //cashed



    //Auto defence
    var autoDefenceAreaMatrix: Array<Array<Int>> = arrayOf()
    var autoDefenceArea: Int = 0

    val creepUpgradeRole: MutableMap<Int, Boolean> = mutableMapOf()

    private fun getSlaveRoomConstant(slaveRoomName: String) : SlaveRoomConstant {
        val slaveRoomConstant: SlaveRoomConstant? = this.slaveRoomConstantContainer[slaveRoomName]
        return if (slaveRoomConstant == null) {
            parent.mainContext.lm.messenger.log("ERROR", slaveRoomName, "initialization don't see SlaveRoomConstant", COLOR_RED)
            SlaveRoomConstant()
        }else slaveRoomConstant
    }

    fun s(index: Int) : SlaveRoomConstant {
        if (index >= this.slaveRooms.size) {
            parent.mainContext.lm.messenger.log("ERROR", "$index", "initialization S out of range slave room", COLOR_RED)
            return SlaveRoomConstant()
        }
        return this.getSlaveRoomConstant(this.slaveRooms[index])
    }

    fun initSlaveRoomConstantContainer(names: Array<String>) {
        var resultSlaveRooms: Array<String> = arrayOf()
        for (name in names){
            slaveRoomConstantContainer[name] = SlaveRoomConstant()
            resultSlaveRooms += name
        }
        this.slaveRooms = resultSlaveRooms
    }

    fun toDynamic(): dynamic {
        val result: dynamic = object {}
        result["towerLastTarget"] = this.towerLastTarget
        result["roomRunNotEveryTickNextTickRun"] = this.roomRunNotEveryTickNextTickRun
        result["levelOfRoom"] = this.levelOfRoom
        result["needCleaner"] = this.needCleaner

        result["reactionActive"] = this.reactionActive

        result["needUpgrader"] = this.needUpgrader
        result["needBuilder"] = this.needBuilder
        result["defenceMinHits"] = this.defenceMinHits

        result["autoDefenceArea"] = this.autoDefenceArea
        result["autoDefenceAreaMatrix"] = this.autoDefenceAreaMatrix

        if (this.slaveRooms.isNotEmpty()) {
            result["slaveRoomConstantContainer"] = object {}
            for (record in this.slaveRoomConstantContainer)
                    result["slaveRoomConstantContainer"][record.key] = record.value.toDynamic()
        }

        return result
    }

    fun fromDynamic(d: dynamic) {
        if (d == null) return
        if (d["towerLastTarget"] != null) this.towerLastTarget = d["towerLastTarget"] as String
        if (d["roomRunNotEveryTickNextTickRun"] != null) this.roomRunNotEveryTickNextTickRun = d["roomRunNotEveryTickNextTickRun"] as Int
        if (d["levelOfRoom"] != null) this.levelOfRoom = d["levelOfRoom"] as Int
        if (d["needCleaner"] != null) this.needCleaner = d["needCleaner"] as Boolean

        if (d["reactionActive"] != null) this.reactionActive = d["reactionActive"] as String


        if (d["autoDefenceArea"] != null) this.autoDefenceArea = d["autoDefenceArea"] as Int
        if (d["autoDefenceAreaMatrix"] != null) this.autoDefenceAreaMatrix = d["autoDefenceAreaMatrix"] as Array<Array<Int>>



        if (d["needUpgrader"] != null) this.needUpgrader = d["needUpgrader"] as Boolean
        if (d["needBuilder"] != null) this.needBuilder = d["needBuilder"] as Boolean
        if (d["defenceMinHits"] != null) this.defenceMinHits = d["defenceMinHits"] as Int

        if (d["slaveRoomConstantContainer"] != null)
            for (record in slaveRoomConstantContainer)
                record.value.fromDynamic(d["slaveRoomConstantContainer"][record.key])
    }
}