package com.glatinis.basecraft.economy.currency.managers;

import com.glatinis.basecraft.BaseCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Collectors;

public class MoneyManager {

    static FileConfiguration config = BaseCraft.getPlugin().getConfig();
    static NamespacedKey moneyKey = new NamespacedKey(BaseCraft.getPlugin(), "money");
    static HashMap<Material, Integer> prices;

    public static void init() {
        prices = getItemConverstion();
    }

    private static HashMap<Material, Integer> getItemConverstion() {
        HashMap<Material, Integer> prices = new HashMap<>();

        HashMap<Material, Integer> finalPrices = prices;
        config.getConfigurationSection("money").getKeys(false).forEach(key -> {
            finalPrices.put(Material.valueOf(key), config.getInt("money." + key));
        });

        // Sort map from largest to smallest
        prices = finalPrices.entrySet().stream()
                .sorted(Map.Entry.<Material, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // Handle duplicates
                        LinkedHashMap::new // Maintain insertion order after sorting
                ));

        return prices;
    }

    public static List<ItemStack> getCurrencyInItems(int money) {
        if (money <= 0) return Collections.emptyList();

        List<ItemStack> items = new ArrayList<>();
        int remaining = money;

        for (Map.Entry<Material, Integer> entry : prices.entrySet()) {
            Material mat = entry.getKey();
            int price = entry.getValue();

            int amount = remaining / price;

            if (amount == 0) continue;

            remaining = remaining % price;
            items.add(getCurrencyItem(mat, amount, price));
        }

        return items;
    }

    private static ItemStack getCurrencyItem(Material mat, int amount, int moneyValue) {
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "$" + moneyValue);
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.setLore(List.of(ChatColor.GRAY + "Official Currency"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(moneyKey, PersistentDataType.INTEGER, moneyValue);
        item.setItemMeta(meta);

        return item;
    }

    public static int getMoneyFromItem(ItemStack item) {
        if (item == null) return 0;
        if (!item.hasItemMeta()) return 0;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(moneyKey, PersistentDataType.INTEGER)) return 0;

        return container.get(moneyKey, PersistentDataType.INTEGER);

    }

    public static int getInventoryValue(Inventory inv) {
        int bal = 0;
        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;
            bal += MoneyManager.getMoneyFromItem(item) * item.getAmount();
        }

        return bal;
    }

}
