package com.glatinis.basecraft.economy.currency.listeners;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.managers.EconomyManager;
import com.glatinis.basecraft.economy.currency.managers.CurrencyItemManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static com.glatinis.basecraft.utils.MathUtils.chance;

public class TokenKillListener implements Listener {

    EconomyManager econ = BaseCraft.getPlugin().getEconomyManager();

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        LivingEntity entity = e.getEntity();
        FileConfiguration config = BaseCraft.getPlugin().getConfig();

        if (entity instanceof Monster || entity instanceof Flying) {
            if (chance(config.getDouble("hostile-token-chance"))) {
                entity.getWorld().dropItemNaturally(entity.getLocation(), CurrencyItemManager.tokenItem);
            }
        }
        else {
            if (chance(config.getDouble("non-hostile-token-chance"))) {
                entity.getWorld().dropItemNaturally(entity.getLocation(), CurrencyItemManager.tokenItem);
            }
        }
    }
}
