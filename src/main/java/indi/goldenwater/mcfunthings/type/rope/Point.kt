package indi.goldenwater.mcfunthings.type.rope

import org.bukkit.util.Vector

data class Point(
    var position: Vector,
    var prevPosition: Vector = position.clone(),
    var locked: Boolean = false,
)
