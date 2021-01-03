package logic.develop

import mainContext.constants.path.CacheCarrier
import mainContext.MainContext
import screeps.api.*

class LMDevelopTestsCacheCarrier(val mc: MainContext) {
    fun runTest() {
        val tPath:Array<RoomPosition> = arrayOf(
            RoomPosition(48,25,"W5N3"),
            RoomPosition(49,25,"W5N3"),
            RoomPosition(0,25,"W4N3"),
            RoomPosition(1,25,"W4N3"),
            RoomPosition(2,26,"W4N3")
        )

        val tCacheCarrier: CacheCarrier = CacheCarrier(mPath = tPath)

        val tDyn = tCacheCarrier.pathToStringShort(tPath)
        val restorePath = CacheCarrier.pathFromStringShort(tDyn)
        println("Test CacheCarrier path Original: $tPath")
        println("Test CacheCarrier path  Dynamic: $tDyn")
        println("Test CacheCarrier path Restored: $restorePath")


        val tBody:Array<BodyPartConstant> = arrayOf(
            MOVE,
            MOVE,
            MOVE,
            MOVE,
            MOVE,
            CARRY,
            CARRY,
            CARRY,
            CARRY,
            CARRY,
            CARRY,
            CARRY,
            CARRY,
            CARRY,
            CARRY,
            WORK,
            ATTACK,
            RANGED_ATTACK,
            TOUGH,
            HEAL,
            CLAIM
        )

        val tDynBody = tCacheCarrier.bodyToStringShort(tBody)
        val restoreBody = CacheCarrier.bodyFromStringShort(tDynBody)
        println("Test CacheCarrier body Original: $tBody")
        println("Test CacheCarrier body  Dynamic: $tDynBody")
        println("Test CacheCarrier body Restored: $restoreBody")
    }

}