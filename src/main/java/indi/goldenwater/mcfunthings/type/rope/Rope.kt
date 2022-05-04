package indi.goldenwater.mcfunthings.type.rope

import org.bukkit.World
import org.bukkit.util.Vector

data class Rope(
    val points: MutableList<Point> = mutableListOf(),
    val sticks: MutableList<Stick> = mutableListOf(),
    val stickIterationTimes: Int = 1
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

            val newPos = it.position.clone()
            // velocity
            newPos.add(
                it.position.clone()
                    .subtract(it.prevPosition)
                    .multiply(0.99) // drag
            )
            // gravity
            newPos.subtract(
                Vector(0.0, 0.02, 0.0)
            )

            it.newPos(newPos, world)
        }

        for (i in 1..stickIterationTimes) {
            sticks.forEach {
                val stickCenterPoint = it.point1.position.getMidpoint(it.point2.position)
                val stickDirection = it.point1.position.clone().subtract(it.point2.position).normalize()
                val stickLength = stickDirection.clone().multiply(it.length / 2)
                if (!it.point1.locked)
                    it.point1.position = stickCenterPoint.clone().add(stickLength)
                if (!it.point2.locked)
                    it.point2.position = stickCenterPoint.clone().subtract(stickLength)
            }
        }
    }
}
