package com.glatinis.basecraft.economy.currency.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class CurrencyItemManager {

    public static ItemStack gemItem;
    public static ItemStack tokenItem;

    public static void init() {
        createGemToken();
        createToken();
    }

    private static void createGemToken() {
        gemItem = new ItemStack(Material.LIGHT_BLUE_DYE);
        gemItem.addUnsafeEnchantment(Enchantment.LUCK, 0);
        ItemMeta meta = gemItem.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lGem"));

        meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&6&lRight Click &7to redeem")));
        // Just hide everything so it looks nice
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        gemItem.setItemMeta(meta);
    }

    private static void createToken() {
        tokenItem = new ItemStack(Material.ORANGE_DYE);
        ItemMeta meta = tokenItem.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lToken"));
        meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&6&lRight Click &7to redeem")));
        // Just hide everything so it looks nice
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        tokenItem.setItemMeta(meta);
    }
}
