package indi.goldenwater.mcfunthings.listener

import indi.goldenwater.mcfunthings.instance
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryInteractEvent

object OnInventoryEvent : Listener {
    @EventHandler
    fun onInventoryEvent(event: InventoryInteractEvent) {
        instance.logger.info(event.toString())
    }
}