package com.glatinis.basecraft.economy.spinners.pouch.listeners;

import com.glatinis.basecraft.economy.spinners.pouch.ui.PouchSpinner;
import com.glatinis.basecraft.economy.spinners.pouch.enums.Rarity;
import com.glatinis.basecraft.economy.spinners.pouch.managers.PouchItemManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PouchListener implements Listener {

    @EventHandler
    public void onPouchOpen(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        // Return if item is not bundle
        if (e.getItem() == null || e.getItem().getType() != Material.ENDER_CHEST)
            return;

        Player plr = e.getPlayer();
        // Check whether it is rare, epic, or legendary
        String name = e.getItem().getItemMeta().getDisplayName();
        if (name.equals(PouchItemManager.rarePouch.getItemMeta().getDisplayName()))
            PouchSpinner.openPouch(plr, Rarity.RARE);
        else if (name.equals(PouchItemManager.epicPouch.getItemMeta().getDisplayName()))
            PouchSpinner.openPouch(plr, Rarity.EPIC);
        else if (name.equals(PouchItemManager.legendaryPouch.getItemMeta().getDisplayName()))
            PouchSpinner.openPouch(plr, Rarity.LEGENDARY);

        e.setCancelled(true);
    }

    @EventHandler
    public void onPouchTouch(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Pouch")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPouchDrag(InventoryDragEvent e) {
        if (e.getView().getTitle().contains("Pouch")) {
            e.setCancelled(true);
        }
    }
}
