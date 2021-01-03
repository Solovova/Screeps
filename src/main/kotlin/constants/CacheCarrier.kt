package constants

import screeps.api.BodyPartConstant
import screeps.api.CARRY
import screeps.api.MOVE
import screeps.api.*


class CacheCarrier(
    var default: Boolean = true,
    var tickRecalculate: Int = 0,
    var needCarriers: Int = 1,
    var timeForDeath: Int = 0,
    val needBody: Array<BodyPartConstant> = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY),
    var mPath: Array<RoomPosition> = arrayOf()
) {

    private fun pathToDynamic(path: Array<RoomPosition>): dynamic {
        return path
    }

    private fun bodyToDynamic(body: Array<BodyPartConstant>): dynamic {
        return body
    }

    fun toDynamic():dynamic {
        val d : dynamic = object {}
        d["1"] = this.default
        d["2"] = this.needCarriers
        d["3"] = this.timeForDeath
        d["4"] = this.tickRecalculate
        d["5"] = this.bodyToDynamic(this.needBody)
        if (this.mPath.isNotEmpty()) d["6"] = pathToDynamic(this.mPath)
        return d
    }

    companion object {
        private fun pathFromDynamic(d: dynamic): Array<RoomPosition> {
            var result: Array<RoomPosition> = arrayOf()
            if (d != null) {
                for (ind in 0..1000) {
                    if (d[ind] == null) break
                    result += RoomPosition(d[ind]["x"] as Int, d[ind]["y"] as Int, d[ind]["roomName"] as String)
                }
            }
            return result
        }

        private fun bodyFromDynamic(d: dynamic): Array<BodyPartConstant> {
            return if (d != null) {
                d as Array<BodyPartConstant>
            } else {
                arrayOf(MOVE,MOVE,MOVE,MOVE,MOVE,CARRY,CARRY,CARRY,CARRY,CARRY,CARRY,CARRY,CARRY,CARRY,CARRY)
            }
        }

        fun initFromDynamic(d: dynamic): CacheCarrier {
            val default: Boolean = if (d["1"] != null) d["1"] as Boolean else true
            val needCarriers: Int = if (d["2"] != null) d["2"] as Int else 1
            val timeForDeath: Int = if (d["3"] != null) d["3"] as Int else 0
            val tickRecalculate: Int = if (d["4"] != null) d["4"] as Int else 0
            val needBody: Array<BodyPartConstant> = this.bodyFromDynamic(d["5"])
            val mPath: Array<RoomPosition> = this.pathFromDynamic(d["6"])
            return CacheCarrier(default = default, needCarriers = needCarriers, timeForDeath = timeForDeath, tickRecalculate = tickRecalculate, needBody = needBody, mPath = mPath)
        }
    }
}
