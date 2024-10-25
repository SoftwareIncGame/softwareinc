package com.glatinis.basecraft.papi;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.enums.Currency;
import com.glatinis.basecraft.economy.currency.managers.EconomyManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class BaseCraftExpansion extends PlaceholderExpansion {

    private BaseCraft plugin;
    private EconomyManager econ;

    public BaseCraftExpansion(BaseCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return "glatinis";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "basecraft";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getRequiredPlugin() {
        return "BaseCraft";
    }

    @Override
    public boolean canRegister() { //
        return (plugin = (BaseCraft) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }

    @Override
    public String onRequest(OfflinePlayer plr, @NotNull String params) {
        if (econ == null) {
            econ = plugin.getEconomyManager();
        }

        try {
            int amount = econ.getCurrency(plr.getName(), Currency.valueOf(params.toUpperCase()));
            return String.valueOf(amount);
        }
        catch (IllegalArgumentException e) {
            return null;
        }

    }
}
