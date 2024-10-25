package com.glatinis.basecraft.economy.bank.commands;

import com.glatinis.basecraft.economy.bank.npc.BankTrait;
import com.glatinis.basecraft.economy.bank.npc.NPCSpawner;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateBankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only a player may run this command");
            return true;
        }

        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "Only an operator may run this command");
            return true;
        }

        Player plr = (Player) commandSender;
        Location spawnLoc = plr.getLocation();
        spawnLoc.setPitch(0);
        NPCSpawner.spawnBankTeller(spawnLoc);

        return true;
    }
}
