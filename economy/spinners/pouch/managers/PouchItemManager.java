package com.glatinis.basecraft.economy.spinners.pouch.managers;

import com.glatinis.basecraft.BaseCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PouchItemManager {

    public static ItemStack rarePouch;
    public static ItemStack epicPouch;
    public static ItemStack legendaryPouch;

    public static void init() {
        rarePouch = getPouch("pouches.rare");
        epicPouch = getPouch("pouches.epic");
        legendaryPouch = getPouch("pouches.legendary");
    }

    private static ItemStack getPouch(String pouchPath) {
        FileConfiguration config = BaseCraft.getPlugin().getConfig();
        String name = config.getString(pouchPath + ".name");
        List<String> lore = config.getStringList(pouchPath + ".lore");

        ItemStack pouch = new ItemStack(Material.ENDER_CHEST);
        pouch.addUnsafeEnchantment(Enchantment.LUCK, 0);
        ItemMeta meta = pouch.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(coloredLore);

        // Just hide everything so it looks nice
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pouch.setItemMeta(meta);

        return pouch;
    }

}
