package logic.mineral

import REACTIONS
import constants.GlobalConstant
import mainContext.MainContext
import screeps.api.ResourceConstant

class LogicMineral(val mainContext: MainContext) {
    fun runInStartOfTick() {
        this.mineralDataFill(mainContext)
        this.mineralProductionFill(mainContext)
    }

    fun setGlobalConstants(globalConstant: GlobalConstant) {
        for (key0 in js("Object").keys(REACTIONS).unsafeCast<Array<ResourceConstant>>())
            for (key1 in js("Object").keys(REACTIONS[key0]).unsafeCast<Array<ResourceConstant>>())
                globalConstant.labReactionComponent[REACTIONS[key0][key1].unsafeCast<ResourceConstant>()] = arrayOf(key0, key1)
    }
}