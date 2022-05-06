package indi.goldenwater.mcfunthings.listener

import indi.goldenwater.mcfunthings.Loop
import indi.goldenwater.mcfunthings.utils.minus
import indi.goldenwater.mcfunthings.utils.plus
import indi.goldenwater.mcfunthings.utils.times
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

object OnEntityMoveEvent : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onEntityMoveEvent(event: EntityMoveEvent) {
        this.onMove(event)
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerMoveEvent(event: PlayerMoveEvent) {
        this.onMove(EntityMoveEvent(event.player, event.from, event.to))
    }

    private fun onMove(event: EntityMoveEvent) {
        if (Loop.rope == null) return
        val rope = Loop.rope!!
        if (rope.points.isEmpty()) return
        if (rope.world.name != event.to.world.name) return
        if (rope.points[0].position.distance(event.to.toVector()) > 100) return

        val entity: LivingEntity = event.entity
        val bb: BoundingBox = entity.boundingBox
        val velocity: Vector = (event.to.toVector() - event.from) * 1.2
        if (velocity.y < 0) velocity.multiply(Vector(1.0, 0.0, 1.0))
        bb.expand(velocity.length())
        var moved = false
        rope.points.forEach {
            if (it.locked) return@forEach
            val pPos = it.position
            if (bb.contains(pPos)) {
                it.position += velocity
                moved = true
            }
        }
        if (moved) rope.tickCollision()
    }
}