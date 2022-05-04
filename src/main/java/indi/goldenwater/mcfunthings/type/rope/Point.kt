package indi.goldenwater.mcfunthings.type.rope

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.util.Vector

data class Point(
    var position: Vector,
    var prevPosition: Vector = position.clone(),
    var locked: Boolean = false,
) {
    fun newPos(newPos: Vector, world: World? = null) {
        world?.let { w ->
            val loc = newPos.toLocation(w)
            if (loc.block.type != Material.AIR) {
                val y = loc.world.getHighestBlockYAt(loc) + 1
                position.setY(y)
                newPos.setY(y)
            }
        }

        prevPosition = position
        position = newPos
    }
}
