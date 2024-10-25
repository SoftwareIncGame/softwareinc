package com.glatinis.basecraft.economy.bank.npc;

import com.glatinis.basecraft.BaseCraft;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

@TraitName("bank")
public class BankTrait extends Trait {

    BaseCraft plugin;
    public BankTrait() {
        super("bank");
        plugin = BaseCraft.getPlugin();
    }

    @EventHandler
    public void onClick(NPCRightClickEvent e) {
        if (e.getNPC() != this.getNPC())
            return;

        Player plr = e.getClicker();
        BaseCraft.getPlugin().getBankGUI().openInventory(plr);
    }


}
