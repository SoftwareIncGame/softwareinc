package com.glatinis.basecraft.economy.currency.commands;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.enums.Currency;
import com.glatinis.basecraft.economy.currency.managers.EconomyManager;
import com.glatinis.basecraft.economy.currency.managers.CurrencyItemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GemWithdrawCommand implements CommandExecutor {

    EconomyManager econ = BaseCraft.getPlugin().getEconomyManager();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may run this command");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage: /gemwithdraw <gems>");
            return true;
        }

        if (!args[0].matches("\\d+")) {
            sender.sendMessage(ChatColor.RED + "Invalid usage: /gemwithdraw <gems>");
            return true;
        }

        Player plr = (Player) sender;
        int gemsToWithdraw = Integer.parseInt(args[0]);
        int gemBalance = econ.getCurrency(plr, Currency.GEMS);

        if (gemsToWithdraw < 1) {
            sender.sendMessage(ChatColor.RED + "Please enter a positive number of gems");
            return true;
        }

        if (gemBalance - gemsToWithdraw < 0) {
            sender.sendMessage(ChatColor.RED + "You do not have enough gems");
            return true;
        }

        econ.removeCurrency(plr, Currency.GEMS, gemsToWithdraw);
        ItemStack gemItem = CurrencyItemManager.gemItem;
        gemItem.setAmount(gemsToWithdraw);
        plr.getInventory().addItem(gemItem);

        plr.sendMessage(ChatColor.GREEN + "Withdrawn " + gemsToWithdraw + " gems");

        return true;
    }
}
