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
    val needBody: Array<BodyPartConstant> = arrayOf(
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
        CARRY
    ),
    var mPath: Array<RoomPosition> = arrayOf()
) {


    private fun pathToDynamic(path: Array<RoomPosition>): dynamic {
        return path
    }


    fun pathToStringShort(path: Array<RoomPosition>): String {
        fun twoRoomPositionToDiff(prevRoomPosition: RoomPosition?, actualRoomPosition: RoomPosition): String {
            fun codeDDToInt(dx: Int, dy: Int): Int {
                return (if (dx == -1) {
                    2
                } else {
                    dx
                }) * 4 + if (dy == -1) {
                    2
                } else {
                    dy
                }
            }

            return if (prevRoomPosition == null || prevRoomPosition.roomName != actualRoomPosition.roomName) {
                "${actualRoomPosition.roomName},${actualRoomPosition.x},${actualRoomPosition.y};"
            } else {
                "${codeDDToInt(actualRoomPosition.x - prevRoomPosition.x, actualRoomPosition.y - prevRoomPosition.y)};"
            }
        }

        val result = StringBuilder()

        for (ind in path.indices) {
            result.append(
                twoRoomPositionToDiff(
                    if (ind == 0) {
                        null
                    } else {
                        path[ind - 1]
                    }, path[ind]
                )
            )
        }

        return result.toString()
    }

    private fun bodyToDynamic(body: Array<BodyPartConstant>): dynamic {
        return body
    }

    fun bodyToStringShort(body: Array<BodyPartConstant>): String {
        val result = StringBuilder()
        for (part in body) {
            result.append((constBodyParts[part] ?: 0).toString())
        }
        return result.toString()
    }

    fun toDynamic(): dynamic {
        val d: dynamic = object {}
        d["1"] = this.default
        d["2"] = this.needCarriers
        d["3"] = this.timeForDeath
        d["4"] = this.tickRecalculate
        d["5"] = this.bodyToDynamic(this.needBody)
        if (this.mPath.isNotEmpty()) d["6"] = pathToDynamic(this.mPath)
        return d
    }

    companion object {
        private val constBodyParts: Map<BodyPartConstant, Int> = mapOf(
            MOVE to 1,
            CARRY to 2,
            WORK to 3,
            ATTACK to 4,
            RANGED_ATTACK to 5,
            TOUGH to 6,
            HEAL to 7,
            CLAIM to 8
        )

        private val constBodyPartsRev: Map<Int, BodyPartConstant> =  constBodyParts.entries.associate { (key, value) -> value to key }

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

        fun pathFromStringShort(str: String?): Array<RoomPosition> {
            if (str == null || str == "") {
                return arrayOf()
            }

            fun getRoomPositionFromPrevRoomPositionAndString(
                prevRoomPosition: RoomPosition?,
                diff: String
            ): RoomPosition {
                val strDiff = diff.split(",")
                return if (strDiff.size > 1) {
                    RoomPosition(strDiff[1].toInt(), strDiff[2].toInt(), strDiff[0])
                } else {
                    val dd: Int = strDiff[0].toInt()
                    var dx: Int = dd / 4
                    if (dx == 2) {
                        dx = -1
                    }
                    var dy: Int = dd % 4
                    if (dy == 2) {
                        dy = -1
                    }
                    RoomPosition(
                        (prevRoomPosition?.x ?: 0) + dx,
                        (prevRoomPosition?.y ?: 0) + dy,
                        prevRoomPosition?.roomName ?: ""
                    )
                }
            }

            val result: MutableList<RoomPosition> = mutableListOf()
            val strPoint: List<String> = str.split(";")
            for (ind in 0..strPoint.size - 2) {
                result.add(
                    getRoomPositionFromPrevRoomPositionAndString(
                        if (ind == 0) {
                            null
                        } else {
                            result[result.size - 1]
                        }, strPoint[ind]
                    )
                )
            }

            return result.toTypedArray()
        }

        fun bodyFromStringShort(str: String?): Array<BodyPartConstant> {
            if (str == null || str == "") {
                return arrayOf()
            }

            val result: MutableList<BodyPartConstant> = mutableListOf()
            for (letter in str) {
                val part: BodyPartConstant? = constBodyPartsRev[letter.toString().toInt()]
                if (part!= null) {result.add(part)}
            }
            return result.toTypedArray()
        }

        private fun bodyFromDynamic(d: dynamic): Array<BodyPartConstant> {
            return if (d != null) {
                d as Array<BodyPartConstant>
            } else {
                arrayOf(
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
                    CARRY
                )
            }
        }

        fun initFromDynamic(d: dynamic): CacheCarrier {
            val default: Boolean = if (d["1"] != null) d["1"] as Boolean else true
            val needCarriers: Int = if (d["2"] != null) d["2"] as Int else 1
            val timeForDeath: Int = if (d["3"] != null) d["3"] as Int else 0
            val tickRecalculate: Int = if (d["4"] != null) d["4"] as Int else 0
            val needBody: Array<BodyPartConstant> = this.bodyFromDynamic(d["5"])
            val mPath: Array<RoomPosition> = this.pathFromDynamic(d["6"])
            return CacheCarrier(
                default = default,
                needCarriers = needCarriers,
                timeForDeath = timeForDeath,
                tickRecalculate = tickRecalculate,
                needBody = needBody,
                mPath = mPath
            )
        }
    }
}
