package indi.goldenwater.mcfunthings.type.rope

import indi.goldenwater.mcfunthings.utils.Vector3
import indi.goldenwater.mcfunthings.utils.minus
import indi.goldenwater.mcfunthings.utils.toClosest
import org.bukkit.FluidCollisionMode
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.util.Vector
import kotlin.math.abs

data class Point(
    var position: Vector,
    var prevPosition: Vector = position.clone(),
    var locked: Boolean = false,
) {
    fun moveTo(newPos: Vector, world: World? = null, byStickUpdate: Boolean = false) {
        try {
            @Suppress("NAME_SHADOWING")
            world?.let { world ->
                val velocity = newPos - position
                if (velocity == Vector3.zero()) return@let
                val cLoc = position.toLocation(world)

                val rayTraceResult =
                    world.rayTraceBlocks(
                        cLoc,
                        velocity,
                        velocity.distance(Vector3.zero()).coerceAtLeast(0.1),
                        FluidCollisionMode.ALWAYS,
                        false
                    )
                if (rayTraceResult != null && rayTraceResult.hitBlock != null && rayTraceResult.hitBlockFace != null) {
                    val hitPos = rayTraceResult.hitPosition
                    val hitBlock = rayTraceResult.hitBlock!!

                    when (rayTraceResult.hitBlockFace!!) {
                        BlockFace.EAST, BlockFace.WEST -> newPos.x =
                            hitBlock.x + abs(hitBlock.x - hitPos.x).toClosest(arrayOf(-0.1, 1.1))
                        BlockFace.UP, BlockFace.DOWN -> newPos.y =
                            hitBlock.y + abs(hitBlock.y - hitPos.y).toClosest(arrayOf(-0.1, 1.1))
                        BlockFace.SOUTH, BlockFace.NORTH -> newPos.z =
                            hitBlock.z + abs(hitBlock.z - hitPos.z).toClosest(arrayOf(-0.1, 1.1))
                        else -> {}
                    }
                }
            }
        } catch (_:IllegalArgumentException){

        }

        if (!byStickUpdate) prevPosition = position
        position = newPos
    }
}
