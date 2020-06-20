package logic.main

import mainContext.MainContext
import screeps.api.Game

class LMGCL (val mainContext: MainContext){
    private fun show(){
        val gclArray = mainContext.constants.globalConstant.gclArray
        var str = " "
        gclArray.takeLast(15).reversed().forEach { str += "${it/1000}  " }

        val lastGCLDynamic = gclArray[gclArray.size-1]

        str += " progress: ${Game.gcl.progress.toInt()/1000} / ${Game.gcl.progressTotal.toInt()/1000} / ${(Game.gcl.progressTotal - Game.gcl.progress)/1000}"
        str += " hours to up: ${(Game.gcl.progressTotal - Game.gcl.progress) / lastGCLDynamic}"

        mainContext.lm.lmMessenger.log("INFO","GCL",str)
    }

    fun calculate() {
        //Initialization
        if (mainContext.constants.globalConstant.gclFromTick == 0) {
            mainContext.constants.globalConstant.gclFromTick = Game.time
            mainContext.constants.globalConstant.gcl = Game.gcl.progress
        }

        if (Game.time - mainContext.constants.globalConstant.gclFromTick >= mainContext.constants.globalConstant.gclPeriod) {
            if (mainContext.constants.globalConstant.gclArray.size >= mainContext.constants.globalConstant.gclArrayMaxSize){
                mainContext.constants.globalConstant.gclArray =
                        mainContext.constants.globalConstant.gclArray
                                .drop(mainContext.constants.globalConstant.gclArray.size-mainContext.constants.globalConstant.gclArrayMaxSize+1).toTypedArray()
            }

            val sArray:Int = mainContext.constants.globalConstant.gclArray.size
            mainContext.constants.globalConstant.gclArray[sArray] = Game.gcl.progress-mainContext.constants.globalConstant.gcl
            mainContext.constants.globalConstant.gclFromTick = Game.time
            mainContext.constants.globalConstant.gcl = Game.gcl.progress
        }

        show()
    }
}