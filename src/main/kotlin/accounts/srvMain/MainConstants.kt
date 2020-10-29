package accounts.srvMain

import mainContext.constants.Constants
import mainContext.dataclass.SlaveRoomType
import screeps.api.*

fun AccountInitMain.initHeadOut(const: Constants) {
           //M0        M1        M2        M3        M4        M5        M6        M7        M8        M9
    const.initMainRoomConstantContainer(arrayOf(
            "E54N37", "E59N36", "E52N38", "E52N37", "E54N39", "E51N39", "E53N38", "E51N37", "E59N38", "E58N37",
            "E52N36", "E58N39", "E57N39", "E57N37", "E53N39", "E49N39", "E47N39", "E51N41", "E52N35", "E51N35",
            "E54N41", "E53N35", "E58N43", "E58N44", "E58N45", "E59N46", "E57N51", "E55N51", "E56N53", "E55N53",
            "E59N52", "E57N55", "E54N53", "E54N43", "E53N34", "E57N35", "E55N43", "E57N34"))

    //Colonization E51N41
    const.getMainRoomConstant("E54N37").initSlaveRoomConstantContainer(arrayOf("E53N37", "E54N36"))                      //M0
    const.getMainRoomConstant("E59N36").initSlaveRoomConstantContainer(arrayOf("E58N36","E57N34"))              //M1
    const.getMainRoomConstant("E52N38").initSlaveRoomConstantContainer(arrayOf())                                        //M2
    const.getMainRoomConstant("E52N37").initSlaveRoomConstantContainer(arrayOf())                                        //M3
    const.getMainRoomConstant("E54N39").initSlaveRoomConstantContainer(arrayOf("E54N38"))                                //M4
    const.getMainRoomConstant("E51N39").initSlaveRoomConstantContainer(arrayOf("E51N38"))                                //M5
    const.getMainRoomConstant("E53N38").initSlaveRoomConstantContainer(arrayOf())                                        //M6
    const.getMainRoomConstant("E51N37").initSlaveRoomConstantContainer(arrayOf())                                        //M7
    const.getMainRoomConstant("E59N38").initSlaveRoomConstantContainer(arrayOf("E59N37", "E59N39"))                      //M8
    const.getMainRoomConstant("E58N37").initSlaveRoomConstantContainer(arrayOf("E58N38"))                                //M9
    const.getMainRoomConstant("E52N36").initSlaveRoomConstantContainer(arrayOf("E51N36", "E53N36"))                      //M10
    const.getMainRoomConstant("E58N39").initSlaveRoomConstantContainer(arrayOf())                                        //M11
    const.getMainRoomConstant("E57N39").initSlaveRoomConstantContainer(arrayOf("E56N39", "E57N38", "E56N38"))            //M12
    const.getMainRoomConstant("E57N37").initSlaveRoomConstantContainer(arrayOf("E57N36"))                                //M13
    const.getMainRoomConstant("E53N39").initSlaveRoomConstantContainer(arrayOf("E52N39"))                                //M14
    const.getMainRoomConstant("E49N39").initSlaveRoomConstantContainer(arrayOf("E49N38"))                                //M15
    const.getMainRoomConstant("E47N39").initSlaveRoomConstantContainer(arrayOf("E46N39", "E47N38"))                      //M16
    const.getMainRoomConstant("E51N41").initSlaveRoomConstantContainer(arrayOf("E51N42", "E52N41"))                      //M17
    const.getMainRoomConstant("E52N35").initSlaveRoomConstantContainer(arrayOf())                                        //M18
    const.getMainRoomConstant("E51N35").initSlaveRoomConstantContainer(arrayOf("E47N34"))                                //M19
    const.getMainRoomConstant("E54N41").initSlaveRoomConstantContainer(arrayOf("E55N41"))                                //M20
    const.getMainRoomConstant("E53N35").initSlaveRoomConstantContainer(arrayOf("E54N35"))                                //M21
    const.getMainRoomConstant("E58N43").initSlaveRoomConstantContainer(arrayOf("E57N43", "E59N43"))                      //M22
    const.getMainRoomConstant("E58N44").initSlaveRoomConstantContainer(arrayOf("E57N44"))                                //M23
    const.getMainRoomConstant("E58N45").initSlaveRoomConstantContainer(arrayOf("E59N45"))                                //M24
    const.getMainRoomConstant("E59N46").initSlaveRoomConstantContainer(arrayOf())                                        //M25
    const.getMainRoomConstant("E57N51").initSlaveRoomConstantContainer(arrayOf("E57N52", "E58N51"))                      //M26
    const.getMainRoomConstant("E55N51").initSlaveRoomConstantContainer(arrayOf("E56N51"))                                //M27
    const.getMainRoomConstant("E56N53").initSlaveRoomConstantContainer(arrayOf("E56N52", "E57N53", "E56N54"))            //M28
    const.getMainRoomConstant("E55N53").initSlaveRoomConstantContainer(arrayOf("E55N52", "E55N54"))                      //M29
    const.getMainRoomConstant("E59N52").initSlaveRoomConstantContainer(arrayOf())                                        //M30
    const.getMainRoomConstant("E57N55").initSlaveRoomConstantContainer(arrayOf("E56N55"))                                //M31
    const.getMainRoomConstant("E54N53").initSlaveRoomConstantContainer(arrayOf("E54N52","E54N54"))                       //M32
    const.getMainRoomConstant("E54N43").initSlaveRoomConstantContainer(arrayOf("E53N43","E54N44"))                       //M33
    const.getMainRoomConstant("E53N34").initSlaveRoomConstantContainer(arrayOf())                                        //M34
    const.getMainRoomConstant("E57N35").initSlaveRoomConstantContainer(arrayOf())                                        //M35
    const.getMainRoomConstant("E55N43").initSlaveRoomConstantContainer(arrayOf())                                        //M36
    const.getMainRoomConstant("E57N34").initSlaveRoomConstantContainer(arrayOf())                                        //M37
}

fun AccountInitMain.initBodyOut(const: Constants) {
    //const.s(32,0).autoBuildRoad = true

    //const.m(32).useCash = false
    //const.m(32).creepUseUpgraderAndBuilderFromMainRoom = true

    //const.s(29,2).model = SlaveRoomType.Colonize
    //const.s(29,2).pathToRoom= arrayOf("E55N53","E55N52","E54N52","E54N53")

    //const.m(33).creepUseUpgraderAndBuilderFromMainRoom = true
    //const.m(33).useCash = false
    //const.s(21,1).model = SlaveRoomType.Colonize
    //const.s(17,2).pathToRoom= arrayOf("E51N41","E51N42","E51N43","E52N43","E53N43","E54N43")

    const.m(34).useCash = false
    const.m(35).useCash = false
    const.m(36).useCash = false
    const.m(37).useCash = false
    const.m(38).useCash = false

    const.s(1,1).model = SlaveRoomType.Colonize //"E57N34"
    const.m(37).creepUseUpgraderAndBuilderFromMainRoom = true

    const.s(19,0).model = SlaveRoomType.Colonize //"E47N34"
    const.s(19,0).pathToRoom = arrayOf("E51N35","E50N35","E50N34","E49N34","E48N34","E47N34")






    const.m(0).reactionActive = "GH2O"
    const.m(1).reactionActive = "GH"
    const.m(2).reactionActive = "GH2O"
    const.m(3).reactionActive = "GH2O"
    //const.m(4).reactionActive = "ZK"
    const.m(4).reactionActiveArr = arrayOf("ZK", "XGH2O")
    const.m(5).reactionActive = "OH"
    const.m(6).reactionActive = "OH"
    //const.m(7).reactionActive = "G"
    const.m(7).reactionActiveArr = arrayOf("G", "XGH2O")
    const.m(8).reactionActive = "OH"
    const.m(9).reactionActive = "GH"
    const.m(10).reactionActive = "OH"
    const.m(11).reactionActive = "OH"
    const.m(12).reactionActive = "OH"
    const.m(13).reactionActive = "ZK"
    const.m(14).reactionActive = "UL"
    const.m(15).reactionActive = "G"
    //const.m(16).reactionActive = "UL"
    const.m(16).reactionActiveArr = arrayOf("UL", "XGH2O")
    //const.m(17).reactionActive = "LH"
    const.m(17).reactionActiveArr = arrayOf("LH", "XLH2O", "XGH2O")
    //const.m(18).reactionActive = "LH2O"
    const.m(18).reactionActiveArr = arrayOf("LH2O", "XLH2O", "XGH2O")
    //const.m(19).reactionActive = "OH"
    const.m(19).reactionActiveArr = arrayOf("OH", "XGH2O")
    //const.m(20).reactionActive = "OH"
    const.m(20).reactionActiveArr = arrayOf("OH", "XGH2O")
    //const.m(21).reactionActive = "OH"
    const.m(21).reactionActiveArr = arrayOf("OH", "XGH2O")
    //const.m(22).reactionActive = "GH"
    const.m(22).reactionActiveArr = arrayOf("OH", "GH", "XGH2O")
    //const.m(23).reactionActive = "GH2O"
    const.m(23).reactionActiveArr = arrayOf("GH2O", "XGH2O")
    const.m(24).reactionActive = "GH2O"
    const.m(25).reactionActive = "ZK"
    const.m(26).reactionActive = "UL"
    const.m(27).reactionActive = "G"
    const.m(28).reactionActive = "GH"
    //const.m(29).reactionActive = "XGH2O"
    const.m(29).reactionActiveArr = arrayOf("ZK", "UL", "XGH2O")
    const.m(30).reactionActive = "XGH2O"
    const.m(31).reactionActive = "XGH2O"
    const.m(32).reactionActive = "XGH2O"
    const.m(33).reactionActive = "XGH2O"


    const.globalConstant.username = "SoloVova"
    const.globalConstant.balanceQtyUpgraderDefault = 36
    const.globalConstant.balanceQtyBuilderDefault = 4


    const.globalConstant.creepUpgradablePartsRange[19] = mapOf<BodyPartConstant, List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(100000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(20000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "GH".unsafeCast<ResourceConstant>())
                    )
    )

    const.globalConstant.creepUpgradablePartsRange[7] = mapOf<BodyPartConstant, List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(50000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(20000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "GH".unsafeCast<ResourceConstant>())
                    )
    )

    const.globalConstant.creepUpgradablePartsRange[101] = mapOf<BodyPartConstant, List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(10000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(10000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "GH".unsafeCast<ResourceConstant>())
                    )
    )


    const.globalConstant.creepUpgradablePartsRange[10] = mutableMapOf<BodyPartConstant, List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(20000, "XLH2O".unsafeCast<ResourceConstant>()),
                            Pair(20000, "LH2O".unsafeCast<ResourceConstant>()),
                            Pair(10000, "LH".unsafeCast<ResourceConstant>())
                    )
    )

}