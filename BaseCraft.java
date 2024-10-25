package com.glatinis.basecraft;

import com.glatinis.basecraft.economy.bank.commands.CreateBankCommand;
import com.glatinis.basecraft.economy.bank.gui.BankGUI;
import com.glatinis.basecraft.economy.bank.npc.BankTrait;
import com.glatinis.basecraft.economy.commands.BCAdmin;
import com.glatinis.basecraft.economy.completers.BCAdminCompleter;
import com.glatinis.basecraft.economy.currency.commands.GemWithdrawCommand;
import com.glatinis.basecraft.economy.currency.commands.TokenWithdrawCommand;
import com.glatinis.basecraft.economy.currency.listeners.RedeemListener;
import com.glatinis.basecraft.economy.currency.listeners.TokenKillListener;
import com.glatinis.basecraft.economy.currency.managers.EconomyManager;
import com.glatinis.basecraft.economy.currency.managers.CurrencyItemManager;
import com.glatinis.basecraft.economy.currency.managers.MoneyManager;
import com.glatinis.basecraft.economy.spinners.pouch.managers.PouchItemManager;
import com.glatinis.basecraft.economy.spinners.pouch.listeners.PouchListener;
import com.glatinis.basecraft.papi.BaseCraftExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class BaseCraft extends JavaPlugin {

    private static BaseCraft instance = null;
    private EconomyManager econ = null;
    private File dataConfigFile;
    private FileConfiguration dataConfig;
    private BankGUI bankGUI;

    @Override
    public void onEnable() {
        if (instance == null)
            instance = this;

        if (!handleDependencies())
            return;

        createDataConfig();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        econ = new EconomyManager();
        CurrencyItemManager.init();
        MoneyManager.init();
        PouchItemManager.init();

        getServer().getPluginManager().registerEvents(new RedeemListener(), this);
        getServer().getPluginManager().registerEvents(new TokenKillListener(), this);
        getServer().getPluginManager().registerEvents(new PouchListener(), this);

        getCommand("gemwithdraw").setExecutor(new GemWithdrawCommand());
        getCommand("tokenwithdraw").setExecutor(new TokenWithdrawCommand());
        getCommand("bcadmin").setExecutor(new BCAdmin());
        getCommand("createbank").setExecutor(new CreateBankCommand());

        getCommand("bcadmin").setTabCompleter(new BCAdminCompleter());

        new BaseCraftExpansion(this).register();

        bankGUI = new BankGUI();

        getLogger().info("Enabled plugin!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled plugin!");
    }

    public static BaseCraft getPlugin() {
        return instance;
    }

    public FileConfiguration getDataConfig() {
        return this.dataConfig;
    }
    public File getDataConfigFile() {
        return this.dataConfigFile;
    }

    public EconomyManager getEconomyManager() {
        return econ;
    }

    private void createDataConfig() {
        dataConfigFile = new File(getDataFolder(), "data.yml");
        if (!dataConfigFile.exists()) {
            dataConfigFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }

        dataConfig = new YamlConfiguration();
        try {
            dataConfig.load(dataConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private boolean handleDependencies() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        if (getServer().getPluginManager().getPlugin("Citizens") == null
                || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        net.citizensnpcs.api.CitizensAPI.getTraitFactory()
                .registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(BankTrait.class));

        return true;
    }

    public BankGUI getBankGUI() {
        return bankGUI;
    }
}
