package logic.tests

import constants.CacheCarrier
import mainContext.MainContext
import screeps.api.RoomPosition

class LMTestsCacheCarrier(val mc: MainContext) {
    fun runTest() {
        val tPath:Array<RoomPosition> = arrayOf(
            RoomPosition(48,25,"W5N3"),
            RoomPosition(49,25,"W5N3"),
            RoomPosition(0,25,"W4N3"),
            RoomPosition(1,25,"W4N3"),
            RoomPosition(2,26,"W4N3")
        )

        val tCacheCarrier: CacheCarrier = CacheCarrier(mPath = tPath)

        val tDyn = tCacheCarrier.pathToDynamicNew(tPath)
        println(tDyn.toString())
    }

}