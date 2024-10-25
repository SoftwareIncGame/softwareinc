package com.glatinis.basecraft.economy.spinners.pouch.ui;

import com.glatinis.basecraft.economy.spinners.pouch.enums.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PouchInventory {

    public static Inventory getPouchInventory(Rarity rarity) {
        // Capatilize first letter
        String cap = rarity.name().substring(0, 1).toUpperCase() + rarity.name().substring(1).toLowerCase();
        Inventory inv = Bukkit.createInventory(null, 27, cap + " Pouch");

        // Glass pane colors
        ItemStack blackPane = getEmptyNameItem(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack whitePane = getEmptyNameItem(Material.WHITE_STAINED_GLASS_PANE);
        ItemStack purplePane = getEmptyNameItem(Material.PURPLE_STAINED_GLASS_PANE);
        ItemStack bluePane = getEmptyNameItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemStack yellowPane = getEmptyNameItem(Material.YELLOW_STAINED_GLASS_PANE);
        ItemStack redPane = getEmptyNameItem(Material.RED_STAINED_GLASS_PANE);
        ItemStack limePane = getEmptyNameItem(Material.LIME_STAINED_GLASS_PANE);


        // Set the glass panes in the specified pattern
        inv.setItem(0, redPane);
        inv.setItem(1, blackPane);
        inv.setItem(2, null);
        inv.setItem(3, blackPane);
        inv.setItem(4, null);
        inv.setItem(5, blackPane);
        inv.setItem(6, null);
        inv.setItem(7, blackPane);
        inv.setItem(8, redPane);

        inv.setItem(9, blackPane);
        inv.setItem(10, purplePane);
        inv.setItem(11, null);
        inv.setItem(12, yellowPane);
        inv.setItem(13, null);
        inv.setItem(14, limePane);
        inv.setItem(15, null);
        inv.setItem(16, bluePane);
        inv.setItem(17, blackPane);

        inv.setItem(18, redPane);
        inv.setItem(19, blackPane);
        inv.setItem(20, null);
        inv.setItem(21, blackPane);
        inv.setItem(22, null);
        inv.setItem(23, blackPane);
        inv.setItem(24, null);
        inv.setItem(25, blackPane);
        inv.setItem(26, redPane);

        return inv;
    }

    public static ItemStack getEmptyNameItem(Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}
