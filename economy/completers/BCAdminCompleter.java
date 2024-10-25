package com.glatinis.basecraft.economy.completers;

import com.glatinis.basecraft.BaseCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BCAdminCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("add", "set", "give", "get");
        }

        if (strings.length == 2) {
            Collection<? extends Player> players = BaseCraft.getPlugin().getServer().getOnlinePlayers();

            return players.stream()
                    .map(player -> player.getName())
                    .collect(Collectors.toList());
        }

        if (strings.length == 3 && !strings[0].equalsIgnoreCase("give")) {
            return List.of("gems", "tokens", "money");
        }
        else if (strings.length == 3 && strings[0].equalsIgnoreCase("give")) {
            return List.of("rare_item_pouch", "epic_item_pouch", "legendary_item_pouch");
        }

        if (strings.length == 4 && !strings[0].equalsIgnoreCase("get")) {
            return List.of("<amount>");
        }

        return Collections.emptyList();
    }
}
