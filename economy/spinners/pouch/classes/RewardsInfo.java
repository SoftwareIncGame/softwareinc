package com.glatinis.basecraft.economy.spinners.pouch.classes;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RewardsInfo {

    List<ItemStack> rewards = new ArrayList<>();
    List<Integer> rarities = new ArrayList<>();
    List<Integer> indexes = new ArrayList<>();
    int rewardIndex;

    public RewardsInfo(List<ItemStack> rewards, List<Integer> rarities, List<Integer> indexes, int rewardIndex) {
        this.rewards = rewards;
        this.rarities = rarities;
        this.indexes = indexes;
        this.rewardIndex = rewardIndex;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public List<Integer> getRarities() {
        return rarities;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public int getRewardIndex() {
        return rewardIndex;
    }
}
