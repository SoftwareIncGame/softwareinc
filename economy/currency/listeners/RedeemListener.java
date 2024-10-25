package com.glatinis.basecraft.economy.currency.listeners;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.enums.Currency;
import com.glatinis.basecraft.economy.currency.managers.EconomyManager;
import com.glatinis.basecraft.economy.currency.managers.CurrencyItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RedeemListener implements Listener {

    EconomyManager econ = BaseCraft.getPlugin().getEconomyManager();

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (e.getItem() == null)
            return;

        Player plr = e.getPlayer();

        if (e.getItem().isSimilar(CurrencyItemManager.gemItem)) {
            if (plr.isSneaking()) {
                ItemStack itemInHand = e.getItem();
                int gemAmount = e.getItem().getAmount();
                itemInHand.setAmount(0);
                econ.addCurrency(plr, Currency.GEMS, gemAmount);

                plr.sendMessage(ChatColor.GREEN + "Added " + gemAmount + " gems to balance");
            }
            else {
                ItemStack itemInHand = e.getItem();
                itemInHand.setAmount(itemInHand.getAmount() - 1);
                econ.addCurrency(plr, Currency.GEMS, 1);

                plr.sendMessage(ChatColor.GREEN + "Added gem to balance");
            }
        }
        else if (e.getItem().isSimilar(CurrencyItemManager.tokenItem)) {
            if (plr.isSneaking()) {
                ItemStack itemInHand = e.getItem();
                int tokenAmount = e.getItem().getAmount();
                itemInHand.setAmount(0);
                econ.addCurrency(plr, Currency.TOKENS, tokenAmount);

                plr.sendMessage(ChatColor.GREEN + "Added " + tokenAmount + " tokens to balance");
            }
            else {
                ItemStack itemInHand = e.getItem();
                itemInHand.setAmount(itemInHand.getAmount() - 1);
                econ.addCurrency(plr, Currency.TOKENS, 1);

                plr.sendMessage(ChatColor.GREEN + "Added token to balance");
            }
        }

    }
}
