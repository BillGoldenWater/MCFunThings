package indi.goldenwater.mcfunthings.listener

import indi.goldenwater.mcfunthings.utils.Vector3
import indi.goldenwater.mcfunthings.utils.plus
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object HandleChair : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        val player = event.player
        if (event.interactionPoint == null
            || event.action != Action.RIGHT_CLICK_BLOCK
            || player.inventory.itemInMainHand.type != Material.AIR
            || player.inventory.itemInOffHand.type != Material.AIR
        ) return

        val eLoc = player.eyeLocation
        val chair = eLoc.world.spawnEntity(
            event.interactionPoint!!,
            EntityType.ARMOR_STAND
        )

        chair.addScoreboardTag("chair")
        if (chair !is ArmorStand) return

        chair.isInvulnerable = true
        chair.isInvisible = true
        chair.isSilent = true
        chair.isSmall = true
        chair.setGravity(false)

        chair.teleport(chair.location.subtract(0.0, chair.boundingBox.height, 0.0))
        chair.setRotation(eLoc.yaw, eLoc.pitch)

        chair.addPassenger(event.player)
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent) {
        val targetEntity: LivingEntity = (event.entity.takeIf { it is LivingEntity } ?: return) as LivingEntity
        val player: Player = (event.damager.takeIf { it is Player } ?: return) as Player
//        player.inventory.itemInMainHand.editMeta {
//            if (it.getAttributeModifiers(Attribute.GENERIC_ATTACK_SPEED) == null)
//                it.addAttributeModifier(
//                    Attribute.GENERIC_ATTACK_SPEED,
//                    AttributeModifier("generic.attack_speed", 255.0, AttributeModifier.Operation.ADD_NUMBER)
//                )
//        }
//        targetEntity.damage(event.damage)
//        targetEntity.velocity += player.eyeLocation.direction.multiply(0.075).add(Vector3.up().multiply(0.2))
        player.velocity += Vector3.up()
        targetEntity.velocity += Vector3.up()
        event.isCancelled = true
    }
}