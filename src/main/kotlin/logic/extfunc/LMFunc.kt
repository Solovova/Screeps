package logic.extfunc

import screeps.api.DirectionConstant


class LMFunc {
    val direction = mapOf<Int,Int> (0 to 1)

    fun getDirectionByDxDy(dx: Int, dy: Int): Int {
        return when {
            dx == 0 && dy == 0 -> {screeps.api.TOP.unsafeCast<Int>()}

            else -> {0}
        }
    }
}