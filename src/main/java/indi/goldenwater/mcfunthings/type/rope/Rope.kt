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
    val entityForces: MutableMap<Entity, Vector> = mutableMapOf(),
    val iterationTimes: Int = 1,
    val gravity: Double = 0.08,
    val drag: Double = 0.02,
    val bounceDrag: Double = 0.05,
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

    fun tick(world: World? = null) {
        this.tickPoints()
        for (i in 1..iterationTimes) {
            this.tickSticks()
            world?.also { tickCollision(it) }
        }
        world?.also { tickCollision(it, calcEntityForce = true) }
        this.tickEntityForce()
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

    private fun tickCollision(world: World, calcEntityForce: Boolean = false) {
        points.forEach {
            try {
                if (it.locked) return@forEach
                var velocity = it.position - it.prevPosition
                if (velocity == Vector3.zero()) return@forEach
                if (abs(velocity.x) > 100) return@forEach
                if (abs(velocity.y) > 100) return@forEach
                if (abs(velocity.z) > 100) return@forEach

                val ray = world.rayTrace(
                    it.prevPosition.toLocation(world),
                    velocity,
                    velocity.length(),
                    FluidCollisionMode.NEVER,
                    true,
                    0.01
                ) { true } ?: return@forEach

                if (!calcEntityForce)
                    ray.hitBlockFace?.let { face ->
                        if (face.modX != 0) velocity.x *= -1.0
                        if (face.modY != 0) velocity.y *= -1.0
                        if (face.modZ != 0) velocity.z *= -1.0

                        if (velocity != it.position - it.prevPosition) velocity *= (1 - bounceDrag)
                    }
                if (calcEntityForce)
                    ray.hitEntity?.let { entity ->
                        velocity += entity.velocity
                        entityForces.put(entity, velocity.clone())
                    }

                it.position = it.prevPosition + velocity
            } catch (_: IllegalArgumentException) {
            }
        }
    }

    private fun tickEntityForce() {
        entityForces.forEach {
            it.key.velocity += it.value
        }
        entityForces.clear()
    }
}
