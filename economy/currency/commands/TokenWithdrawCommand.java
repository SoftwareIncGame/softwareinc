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

public class TokenWithdrawCommand implements CommandExecutor {

    EconomyManager econ = BaseCraft.getPlugin().getEconomyManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may run this command");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid usage: /tokenwithdraw <tokens>");
            return true;
        }

        if (!args[0].matches("\\d+")) {
            sender.sendMessage(ChatColor.RED + "Invalid usage: /tokenwithdraw <tokens>");
            return true;
        }

        Player plr = (Player) sender;
        int tokensToWithdraw = Integer.parseInt(args[0]);
        int tokenBalance = econ.getCurrency(plr, Currency.TOKENS);

        if (tokensToWithdraw < 1) {
            sender.sendMessage(ChatColor.RED + "Please enter a positive number of tokens");
            return true;
        }

        if (tokenBalance - tokensToWithdraw < 0) {
            sender.sendMessage(ChatColor.RED + "You do not have enough tokens");
            return true;
        }

        econ.removeCurrency(plr, Currency.TOKENS, tokensToWithdraw);
        ItemStack tokenItem = CurrencyItemManager.tokenItem;
        tokenItem.setAmount(tokensToWithdraw);
        plr.getInventory().addItem(tokenItem);

        plr.sendMessage(ChatColor.GREEN + "Withdrawn " + tokensToWithdraw + " tokens");

        return true;
    }
}
