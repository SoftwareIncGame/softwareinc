package com.glatinis.basecraft.economy.currency.managers;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.enums.Currency;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class EconomyManager {

    BaseCraft plugin = BaseCraft.getPlugin();
    FileConfiguration dataConfig = plugin.getDataConfig();

    public int getCurrency(Player player, Currency currency) {
        return getCurrency(player.getName(), currency);
    }

    public void setCurrency(Player player, Currency currency, int amount) {
        setCurrency(player.getName(), currency, amount);
    }

    public void addCurrency(String playerName, Currency currency, int amountToAdd) {
        setCurrency(playerName, currency, getCurrency(playerName, currency) + amountToAdd);
    }

    public void addCurrency(Player player, Currency currency, int amount) {
        addCurrency(player.getName(), currency, amount);
    }

    public void removeCurrency(String playerName, Currency currency, int amountToRemove) {
        setCurrency(playerName, currency, getCurrency(playerName, currency) - amountToRemove);
    }

    public void removeCurrency(Player player, Currency currency, int amount) {
        removeCurrency(player.getName(), currency, amount);
    }

    // Helper methods

    public int getCurrency(String playerName, Currency currency) {
        if (!dataConfig.contains(playerName))
            dataConfig.createSection(playerName);

        String currencyName = currency.name();

        ConfigurationSection playerSec = dataConfig.getConfigurationSection(playerName);
        if (!playerSec.contains(currencyName)) {
            playerSec.set(currencyName, 0);

            try {
                dataConfig.save(plugin.getDataConfigFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return playerSec.getInt(currencyName);
    }

    public void setCurrency(String playerName, Currency currency, int amount) {
        if (!dataConfig.contains(playerName))
            dataConfig.createSection(playerName);

        String currencyName = currency.name().toUpperCase();

        ConfigurationSection playerSec = dataConfig.getConfigurationSection(playerName);
        playerSec.set(currencyName, amount);

        try {
            dataConfig.save(plugin.getDataConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
