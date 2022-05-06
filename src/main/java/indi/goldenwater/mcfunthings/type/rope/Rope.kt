package indi.goldenwater.mcfunthings.type.rope

import indi.goldenwater.mcfunthings.utils.Vector3
import indi.goldenwater.mcfunthings.utils.minus
import indi.goldenwater.mcfunthings.utils.plus
import indi.goldenwater.mcfunthings.utils.times
import org.bukkit.FluidCollisionMode
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.abs

data class Rope(
    val points: MutableList<Point> = mutableListOf(),
    val sticks: MutableList<Stick> = mutableListOf(),
    val world: World,
    val entityForces: MutableMap<Entity, Vector> = mutableMapOf(),
    val iterationTimes: Int = 1,
    val gravity: Double = 0.04,
    val drag: Double = 0.01,
    val bounceDrag: Double = 0.25,
) {
    fun addPoint(point: Point) {
        points.add(point)
    }

    fun addStick(point1: Point, point2: Point, length: Double = defaultStickLength) {
        sticks.add(Stick(point1, point2, length))
    }

    fun attachAPoint(prevPoint: Point, point: Point) {
        addPoint(point)
        addStick(prevPoint, point)
    }

    fun tick() {
        this.tickPoints()
        for (i in 1..iterationTimes) {
            this.tickSticks()
            tickCollision()
        }
    }

    private fun tickPoints() {
        points.forEach {
            if (it.locked) return@forEach

            val velocity = it.position - it.prevPosition

            velocity
                .multiply(1 - drag)
                .add(Vector3.down() * gravity)

            it.prevPosition = it.position
            it.position += velocity
        }
    }

    private fun tickSticks() {
        sticks.forEach {
            val stickCenter = it.point1.position.getMidpoint(it.point2.position)
            val stickDirection = (it.point1.position - it.point2.position).normalize()
            val stickHalfLength = stickDirection * (it.length / 2)
            if (!it.point1.locked)
                it.point1.position = stickCenter + stickHalfLength
            if (!it.point2.locked)
                it.point2.position = stickCenter - stickHalfLength
        }
    }

    fun tickCollision() {
        points.forEach {
            try {
                if (it.locked) return@forEach
                val velocity = it.position - it.prevPosition
                if (velocity == Vector3.zero()) velocity.add(Vector3.epsilon())
                if (abs(velocity.x) > 1) return@forEach
                if (abs(velocity.y) > 1) return@forEach
                if (abs(velocity.z) > 1) return@forEach

                val ray = world.rayTrace(
                    it.prevPosition.toLocation(world),
                    velocity,
                    velocity.length().coerceAtLeast(0.3),
                    FluidCollisionMode.NEVER,
                    true,
                    0.01
                ) { true } ?: return@forEach

                ray.hitBlockFace?.let { face ->
                    if (face.modX != 0) velocity.x *= -1
                    if (face.modY != 0) velocity.y *= -1
                    if (face.modZ != 0) velocity.z *= -1

                    velocity.multiply(1 - bounceDrag)
                }

                it.position = it.prevPosition + velocity
            } catch (_: IllegalArgumentException) {
            }
        }
    }
}
