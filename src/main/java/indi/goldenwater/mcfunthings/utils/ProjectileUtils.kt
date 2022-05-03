package indi.goldenwater.mcfunthings.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.util.Vector

@Suppress("EnumEntryName")
enum class ProjectileInfo(
    val gravityAcceleration: Double = 0.08,
    val drag: Double = 0.02,
    val applyDragFirst: Boolean = true
) {
    Player_OtherEntity(0.08, 0.02, false),
    Player_Mob_WithSlowFalling(0.01, 0.02, false),
    Item_FallingBlock_TNT(0.04, 0.02),
    Minecart(0.04, 0.05),
    Boat(0.04, 0.0000001),
    Egg_SnowBall_Potion_EnderPearl(0.03, 0.01),
    ExperienceOrb(0.03, 0.02),
    FishHook(0.03, 0.08),
    LlamaSpit(0.06, 0.01),
    Arrow_Trident(0.05, 0.01),
    Fireball_WitherSkull_DragonFireball(0.10, 0.05, false),
    DangerousWitherSkull(0.10, 0.27, false)
}

fun Vector.calcProjectileTrace(
    initialLocation: Location,
    iterationTimes: Int,
    projectileInfo: ProjectileInfo,
    entityCannotFly: Boolean = false,
    ignoreBlock: Boolean = false,
): MutableList<Location> {
    val result: MutableList<Location> = mutableListOf()

    if (initialLocation.block.type != Material.AIR && !ignoreBlock) return result

    if (iterationTimes == 0) return result

    val newVector: Vector = this.clone()

    val drag = 1 - projectileInfo.drag
    newVector.multiply(Vector(drag, drag, drag))

    val acc = projectileInfo.gravityAcceleration * if (projectileInfo.applyDragFirst) 1.0 else drag
    newVector.subtract(Vector(0.0, acc, 0.0))

    val newLoc: Location = initialLocation.clone().add(newVector)
    result.add(newLoc)

    result.addAll(
        newVector.calcProjectileTrace(
            newLoc,
            iterationTimes - 1,
            projectileInfo,
            entityCannotFly,
            ignoreBlock
        )
    )

    return result
}