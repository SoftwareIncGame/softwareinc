package com.glatinis.basecraft.economy.spinners.pouch.ui;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.enums.Currency;
import com.glatinis.basecraft.economy.currency.managers.EconomyManager;
import com.glatinis.basecraft.economy.spinners.pouch.classes.RewardsInfo;
import com.glatinis.basecraft.economy.spinners.pouch.enums.Rarity;
import com.glatinis.basecraft.economy.spinners.pouch.enums.RewardType;
import com.glatinis.basecraft.economy.spinners.pouch.managers.PouchRewardManager;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.glatinis.basecraft.economy.spinners.pouch.managers.PouchRewardManager.preprocessRewardList;
import static com.glatinis.basecraft.economy.spinners.pouch.ui.PouchInventory.getEmptyNameItem;
import static com.glatinis.basecraft.economy.spinners.pouch.ui.PouchInventory.getPouchInventory;
import static com.glatinis.basecraft.utils.GeneralUtils.runAfterDelay;

public class PouchSpinner {

    public static final int rowLength = 9;

    public static void openPouch(Player plr, Rarity pouchRarity) {
        plr.openInventory(getPouchInventory(pouchRarity));
        InventoryView pouchInv = plr.getOpenInventory();
        int rewardRarity = PouchRewardManager.getRewardRarity();

        FileConfiguration config = BaseCraft.getPlugin().getConfig();
        int spinLength = config.getInt("pouch-spin-length");
        int spinTickSpeed = config.getInt("pouch-spin-delay");

        List<List<ItemStack>> pouchRewards = PouchRewardManager.getRewards(pouchRarity);
        RewardsInfo rewardsInfo = preprocessRewardList(rewardRarity, pouchRewards);
        List<Integer> indexes = rewardsInfo.getIndexes();

        for (int i = 0; i < spinLength; i++) {
            int left = indexes.get(Math.floorMod(indexes.size() - spinLength + i - 1, indexes.size()));
            int center = indexes.get(Math.floorMod(indexes.size() - spinLength + i, indexes.size()));
            int right = indexes.get(Math.floorMod(indexes.size() - spinLength + i + 1, indexes.size()));

            ItemStack leftReward = rewardsInfo.getRewards().get(left);
            ItemStack centerReward = rewardsInfo.getRewards().get(center);
            ItemStack rightReward = rewardsInfo.getRewards().get(right);

            int leftRarity = rewardsInfo.getRarities().get(left);
            int centerRarity = rewardsInfo.getRarities().get(center);
            int rightRarity = rewardsInfo.getRarities().get(right);

            int finalI = i;
            runAfterDelay(() -> {
                setLeftReward(pouchInv, leftReward, leftRarity);
                setCenterReward(pouchInv, centerReward, centerRarity);
                setRightReward(pouchInv, rightReward, rightRarity);

                if (finalI == spinLength - 1) {
                    plr.playSound(plr, "minecraft:block.anvil.place", 1, 1);
                    runAfterDelay(() -> giveRewards(plr, Arrays.asList(leftReward, centerReward, rightReward)), 20);
                } else {
                    plr.playSound(plr, "minecraft:block.bamboo.place", 1, 1);
                }
            }, (long) spinTickSpeed * i);
        }
    }

    private static void giveRewards(Player plr, List<ItemStack> rewards) {
        EconomyManager econ = BaseCraft.getPlugin().getEconomyManager();

        for (ItemStack reward : rewards) {
            RewardType rewardType = PouchRewardManager.getRewardType(reward);
            int value = PouchRewardManager.getRewardValue(reward);

            switch (rewardType) {
                case MONEY:
                    econ.addCurrency(plr, Currency.MONEY, value);
                    plr.sendMessage(ChatColor.GREEN + "You got $" + value + "!");
                    break;
                case TOKENS:
                    econ.addCurrency(plr, Currency.TOKENS, value);
                    plr.sendMessage(ChatColor.GREEN + "You got " + value + " tokens!");
                    break;
                case GEMS:
                    econ.addCurrency(plr, Currency.GEMS, value);
                    plr.sendMessage(ChatColor.GREEN + "You got " + value + " gems!");
                    break;
                case OTHER:
                    plr.getInventory().addItem(reward);
                    String suffix = reward.getAmount() > 1 ? "(s)" : "";
                    plr.sendMessage(ChatColor.GREEN + "You got " + reward.getAmount() + " " + getItemName(reward) + suffix + "!");
                    break;
                default:
                    Bukkit.getLogger().severe("Invalid reward type: " + rewardType);
                    break;
            }
        }

        plr.closeInventory();
        ItemStack item = plr.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - 1);
    }

    private static void setReward(InventoryView inv, ItemStack item, int rewardRarity, int slot) {
        inv.setItem(slot, item);
        Material mat;
        if (rewardRarity == 0) {
            mat = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
        }
        else if (rewardRarity == 1) {
            mat = Material.WHITE_STAINED_GLASS_PANE;
        }
        else {
            mat = Material.RED_STAINED_GLASS_PANE;
        }

        ItemStack glass = getEmptyNameItem(mat);

        inv.setItem(slot - rowLength, glass);
        inv.setItem(slot + rowLength, glass);
    }

    private static void setLeftReward(InventoryView inv, ItemStack item, int rewardRarity) {
        setReward(inv, item, rewardRarity, 11);
    }

    private static void setCenterReward(InventoryView inv, ItemStack item, int rewardRarity) {
        setReward(inv, item, rewardRarity, 13);
    }

    private static void setRightReward(InventoryView inv, ItemStack item, int rewardRarity) {
        setReward(inv, item, rewardRarity, 15);
    }

    private static String getItemName(ItemStack item) {
        if (item == null) { return ""; }
        if (item.getItemMeta() == null) { return item.getType().name().toLowerCase(); }
        if (!item.getItemMeta().hasDisplayName()) { return item.getType().name().toLowerCase();}

        return item.getItemMeta().getDisplayName();
    }

}
