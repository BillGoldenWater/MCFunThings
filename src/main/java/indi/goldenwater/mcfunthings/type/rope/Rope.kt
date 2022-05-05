package indi.goldenwater.mcfunthings.type.rope

import indi.goldenwater.mcfunthings.utils.Vector3
import indi.goldenwater.mcfunthings.utils.minus
import indi.goldenwater.mcfunthings.utils.plus
import indi.goldenwater.mcfunthings.utils.times
import org.bukkit.World

data class Rope(
    val points: MutableList<Point> = mutableListOf(),
    val sticks: MutableList<Stick> = mutableListOf(),
    val stickIterationTimes: Int = 1,
    val gravity: Double = 0.02,
    val drag: Double = 0.05,
    val dragOnGround: Double = 0.5,
) {
    fun addPoint(point: Point) {
        points.add(point)
    }

    fun addStick(point1: Point, point2: Point) {
        sticks.add(Stick(point1, point2))
    }

    fun attachAPoint(prevPoint: Point, point: Point) {
        addPoint(point)
        addStick(prevPoint, point)
    }

    fun tick(world: World? = null) {
        points.forEach {
            if (it.locked) return@forEach

            val velocity = it.position - it.prevPosition

            velocity
                .multiply(1 - drag)
                .add(Vector3.down() * gravity)

            it.moveTo(it.position + velocity, world)
        }

        for (i in 1..stickIterationTimes) {
            sticks.forEach {
                val stickCenter = it.point1.position.getMidpoint(it.point2.position)
                val stickDirection = (it.point1.position - it.point2.position).normalize()
                val stickLength = stickDirection * (it.length / 2)
                if (!it.point1.locked)
                    it.point1.moveTo(stickCenter + stickLength, world, byStickUpdate = true)
                if (!it.point2.locked)
                    it.point2.moveTo(stickCenter - stickLength, world, byStickUpdate = true)
            }
        }
    }
}
