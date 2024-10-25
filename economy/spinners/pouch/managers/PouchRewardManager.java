package com.glatinis.basecraft.economy.spinners.pouch.managers;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.spinners.pouch.classes.RewardsInfo;
import com.glatinis.basecraft.economy.spinners.pouch.enums.Rarity;
import com.glatinis.basecraft.economy.spinners.pouch.enums.RewardType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Collectors;

public class PouchRewardManager {

    public static NamespacedKey rewardTypeKey = new NamespacedKey(BaseCraft.getPlugin(), "type");
    public static NamespacedKey rewardValueKey = new NamespacedKey(BaseCraft.getPlugin(), "value");

    // 0 = Normal, 1 = Epic, 2 = Legendary
    public static int getRewardRarity() {
        double rand = Math.random();

        if (rand < 0.1)
            return 2;
        else if (rand < 0.45)
            return 1;
        else
            return 0;
    }

    public static List<List<ItemStack>> getRewards(Rarity pouchRarity) {
        List<List<ItemStack>> rewards = new ArrayList<>();
        FileConfiguration config = BaseCraft.getPlugin().getConfig();
        String[] levels = { "normal", "epic", "legendary" };

        for (String level : levels) {
            String levelPath = "pouches." + pouchRarity.name().toLowerCase() + ".rewards." + level;
            List<ItemStack> levelList = new ArrayList<>();
            List levelRewards = config.getList(levelPath);

            for (Object levelReward : levelRewards) {
                LinkedHashMap<String, Integer> reward = (LinkedHashMap<String, Integer>) levelReward;
                String key = reward.keySet().toArray()[0].toString();
                int value = (int) reward.values().toArray()[0];

                levelList.add(getRewardFromKeyValue(key, value));
            }

            rewards.add(levelList);
        }

        return rewards;
    }

    public static ItemStack getRewardFromKeyValue(String key, int value) {
        if (key.equalsIgnoreCase("money")) {
            ItemStack money = new ItemStack(Material.PAPER);
            ItemMeta meta = money.getItemMeta();
            meta.setDisplayName("$" + value);
            saveRewardData(RewardType.MONEY, value, meta);
            money.setItemMeta(meta);
            return money;
        }

        else if (key.equalsIgnoreCase("tokens")) {
            ItemStack tokens = new ItemStack(Material.ORANGE_DYE);
            ItemMeta meta = tokens.getItemMeta();
            meta.setDisplayName(value + " Tokens");
            saveRewardData(RewardType.TOKENS, value, meta);
            tokens.setItemMeta(meta);
            return tokens;
        }

        else if (key.equalsIgnoreCase("gems")) {
            ItemStack gems = new ItemStack(Material.EMERALD);
            ItemMeta meta = gems.getItemMeta();
            meta.setDisplayName(value + " Gems");
            saveRewardData(RewardType.GEMS, value, meta);

            gems.setItemMeta(meta);
            return gems;
        }

        else if (key.equalsIgnoreCase("rare_item_pouch")) {
            ItemStack pouch = PouchItemManager.rarePouch;
            ItemMeta meta = pouch.getItemMeta();
            saveRewardData(RewardType.OTHER, value, meta);
            pouch.setItemMeta(meta);
            return pouch;
        }

        else if (key.equalsIgnoreCase("epic_item_pouch")) {
            ItemStack pouch = PouchItemManager.epicPouch;
            ItemMeta meta = pouch.getItemMeta();
            saveRewardData(RewardType.OTHER, value, meta);
            pouch.setItemMeta(meta);
            return pouch;
        }

        else if (key.equalsIgnoreCase("legendary_item_pouch")) {
            ItemStack pouch = PouchItemManager.legendaryPouch;
            ItemMeta meta = pouch.getItemMeta();
            saveRewardData(RewardType.OTHER, value, meta);
            pouch.setItemMeta(meta);
            return pouch;
        }

        else {
            Material mat = Material.getMaterial(key.toUpperCase());
            if (mat == null) {
                BaseCraft.getPlugin().getLogger().severe("Invalid material name: " + key);
                return null;
            }

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            saveRewardData(RewardType.OTHER, value, meta);
            item.setAmount(value);
            item.setItemMeta(meta);
            return item;
        }
    }

    private static void saveRewardData(RewardType rewardType, int value, ItemMeta meta) {
        meta.getPersistentDataContainer().set(rewardTypeKey, PersistentDataType.INTEGER, rewardType.ordinal());
        meta.getPersistentDataContainer().set(rewardValueKey, PersistentDataType.INTEGER, value);
    }

    // Create a flat list of rewards, list of rarities corresponding to each reward,
    // and a list of indexes relating to both lists.
    // Get the flat index of the reward, remove it from the index list, shuffle that list
    // and place it at the end of the indexes, to make the spin process easier.
    public static RewardsInfo preprocessRewardList(int rewardRarity, List<List<ItemStack>> rewardList) {
        List<ItemStack> flatList = new ArrayList<>();
        List<Integer> rarities = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();

        int rewardIndex = new Random().nextInt(rewardList.get(rewardRarity).size());
        int flatIndex;

        if (rewardRarity == 0) {
            flatIndex = rewardIndex;
        } else if (rewardRarity == 1) {
            flatIndex = rewardIndex + rewardList.get(0).size();
        } else {
            flatIndex = rewardIndex + rewardList.get(0).size() + rewardList.get(1).size();
        }

        for (int i = 0; i < rewardList.size(); i++) {
            for (int j = 0; j < rewardList.get(i).size(); j++) {
                rarities.add(i);
            }
        }

        for (List<ItemStack> list : rewardList) {
            flatList.addAll(list);
        }

        for (int i = 0; i < flatList.size(); i++) {
            indexes.add(i);
        }

        indexes.remove(flatIndex);
        Collections.shuffle(indexes);
        indexes.add(flatIndex);

        return new RewardsInfo(flatList, rarities, indexes, flatIndex);
    }

    public static RewardType getRewardType(ItemStack item) {
        return RewardType.values()[item.getItemMeta().getPersistentDataContainer().get(rewardTypeKey, PersistentDataType.INTEGER)];
    }

    public static int getRewardValue(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(rewardValueKey, PersistentDataType.INTEGER);
    }
}
