package com.glatinis.basecraft.economy.commands;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.enums.Currency;
import com.glatinis.basecraft.economy.currency.managers.EconomyManager;
import com.glatinis.basecraft.economy.spinners.pouch.managers.PouchItemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BCAdmin implements CommandExecutor {
    EconomyManager econ = BaseCraft.getPlugin().getEconomyManager();
    String usage = ChatColor.RED + "Invalid usage: /bcadmin <add | set | give | get>";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Only OPs may run this command");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(usage);
            return true;
        }

        if (args.length == 3) {
            args = new String[]{args[0], args[1], args[2], "1"};
        }

        if (!args[3].matches("\\d+")) {
            sender.sendMessage(usage);
            return true;
        }

        String cmd = args[0];
        String plrName = args[1];

        if (cmd.equalsIgnoreCase("add") || cmd.equalsIgnoreCase("set")) {
            Currency currency;
            int amount = Integer.parseInt(args[3]);

            try {
                currency = Currency.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(usage);
                return true;
            }

            if (cmd.equalsIgnoreCase("add"))
                econ.addCurrency(plrName, currency, amount);
            else if (cmd.equalsIgnoreCase("set"))
                econ.setCurrency(plrName, currency, amount);

            if (cmd.equalsIgnoreCase("set"))
                sender.sendMessage(ChatColor.GREEN + "Set " + plrName + "'s " + currency.name().toLowerCase() + " to " + amount);
            else
                sender.sendMessage(ChatColor.GREEN + "Added " + amount + " " + currency.name().toLowerCase() + " to " + plrName);
        }
        else if (cmd.equalsIgnoreCase("give")) {
            String itemName = args[2];
            int amount = Integer.parseInt(args[3]);

            if (BaseCraft.getPlugin().getServer().getPlayer(plrName) == null) {
                sender.sendMessage(ChatColor.RED + "Player " + plrName + " not found");
                return true;
            }

            Player plr = BaseCraft.getPlugin().getServer().getPlayer(plrName);

            for (int i = 0; i < amount; i++) {
                switch (itemName) {
                    case "rare_item_pouch":
                        plr.getInventory().addItem(PouchItemManager.rarePouch);
                        break;
                    case "epic_item_pouch":
                        plr.getInventory().addItem(PouchItemManager.epicPouch);
                        break;
                    case "legendary_item_pouch":
                        plr.getInventory().addItem(PouchItemManager.legendaryPouch);
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Invalid item name");
                        return true;
                }
            }
        }
        else if (cmd.equalsIgnoreCase("get")) {
            Currency currency;

            try {
                currency = Currency.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(usage);
                return true;
            }

            sender.sendMessage(ChatColor.GREEN + plrName + " has " + econ.getCurrency(plrName, currency) + " " + currency.name().toLowerCase());
        }
        else {
            sender.sendMessage(usage);
            return true;
        }

        return true;
    }
}
