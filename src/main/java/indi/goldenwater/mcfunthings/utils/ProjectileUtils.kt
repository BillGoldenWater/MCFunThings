package indi.goldenwater.mcfunthings.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.util.Vector

data class ProjectileInfo(
    val gravityAcceleration: Double = 0.05,
    val resistance: Vector = Vector(0.01, 0.01, 0.01),
)

object ProjectileInfos {
    val Player_OtherEntity_ArmorStand: ProjectileInfo =
        ProjectileInfo(0.08, Vector(0.09, 0.02, 0.09))
    val Item_FallingBlock_TNT: ProjectileInfo =
        ProjectileInfo(0.04, Vector(0.02, 0.02, 0.02))
    val Boat: ProjectileInfo =
        ProjectileInfo(0.04, Vector(0.10, 0.00, 0.10))
    val Minecart: ProjectileInfo =
        ProjectileInfo(0.04, Vector(0.05, 0.05, 0.05))
    val Egg_Snowball_Potion_EnderPearl: ProjectileInfo =
        ProjectileInfo(0.03, Vector(0.01, 0.01, 0.01))
    val Arrow_Trident: ProjectileInfo =
        ProjectileInfo(0.05, Vector(0.01, 0.01, 0.01))
    val FireBall_WitherSkull_DragonFireBall_ShulkerBullet: ProjectileInfo =
        ProjectileInfo(0.00, Vector(0.05, 0.05, 0.05))
    val FishHook: ProjectileInfo =
        ProjectileInfo(0.03, Vector(0.08, 0.08, 0.08))
    val LlamaSpit: ProjectileInfo =
        ProjectileInfo(0.06, Vector(0.01, 0.01, 0.01))
}

fun Vector.calcProjectileTrace(
    initialLocation: Location,
    iterationTimes: Int,
    gravityAcceleration: Double = 0.05,
    resistance: Vector = Vector(0.01, 0.01, 0.01),
    ignoreBlock: Boolean = false,
): MutableList<Location> {
    val result: MutableList<Location> = mutableListOf()
    if (initialLocation.block.type != Material.AIR && !ignoreBlock) return result

    if (iterationTimes == 0) return result

    val newVector: Vector = this.clone()
        .subtract(Vector(0.0, gravityAcceleration, 0.0))
        .multiply(Vector(1, 1, 1).subtract(resistance))
    val newLoc: Location = initialLocation.clone().add(newVector)

    result.add(newLoc)
    result.addAll(
        newVector.calcProjectileTrace(newLoc, iterationTimes - 1, gravityAcceleration, resistance)
    )

    return result
}