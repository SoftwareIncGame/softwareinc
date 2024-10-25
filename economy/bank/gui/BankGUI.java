package com.glatinis.basecraft.economy.bank.gui;

import com.glatinis.basecraft.BaseCraft;
import com.glatinis.basecraft.economy.currency.enums.Currency;
import com.glatinis.basecraft.economy.currency.managers.MoneyManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.glatinis.basecraft.economy.currency.managers.MoneyManager.getInventoryValue;

public class BankGUI implements Listener {

    final String keyName = "bank";
    final Material[] mats = {
            Material.RED_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE
    };

    List<UUID> waitingForResponse = new ArrayList<>();
    final Integer[] reds = { 0, 8, 45, 53 };
    final Integer[] yellows = { 10, 12, 16, 37, 39, 43 };
    final Integer[] grays = { 2, 3, 5, 6, 18, 19, 26, 27, 28, 35, 47, 51 };
    final Integer[] blacks = { 1, 4, 7, 9, 11, 17, 36, 38, 44, 46, 48, 50, 52 };
    final Integer[] specials = { 20, 29, 49 };
    final Integer[] usableSlots = { 13, 14, 15, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 40, 41, 42 };
    final Integer[][] panes = { reds, yellows, grays, blacks };

    final Integer[] unusableSlots = Stream.concat(
            Arrays.stream(panes).flatMap(Arrays::stream),
            Arrays.stream(specials)
    ).toArray(Integer[]::new);

    private Inventory bankInv;

    public BankGUI() {
        bankInv = Bukkit.createInventory(null, 54, "Bank");
        BaseCraft.getPlugin().getServer().getPluginManager().registerEvents(this, BaseCraft.getPlugin());
    }

    public void initializeItems(HumanEntity ent) {
        Player plr = (Player) ent;
        bankInv = Bukkit.createInventory(plr, 54, "Bank");

        for (int i = 0; i < panes.length; i++) {
            for (int j = 0; j < panes[i].length; j++) {
                bankInv.setItem(panes[i][j], getSpecialItem(mats[i], " "));
            }
        }

        bankInv.setItem(specials[0], getSpecialItem(Material.IRON_INGOT, "Withdraw"));
        bankInv.setItem(specials[1], getSpecialItem(Material.GOLD_INGOT, "Deposit"));
        bankInv.setItem(specials[2], getSpecialItem(Material.ARROW, "Back"));
    }

    private ItemStack getSpecialItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(HumanEntity ent) {
        initializeItems(ent);
        addCurrency((Player) ent);
        ent.openInventory(bankInv);
    }

    private void addCurrency(Player plr) {
        int playerMoney = BaseCraft.getPlugin().getEconomyManager().getCurrency(plr, Currency.MONEY);
        List<ItemStack> items = MoneyManager.getCurrencyInItems(playerMoney);

        for (int i = 0; i < items.size(); i++) {
            bankInv.addItem(items.get(i));
        }
    }

    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().equals(bankInv)) return;
        // Cancel event if trying to click on any decorative items
        e.setCancelled(Arrays.stream(unusableSlots).anyMatch(i -> i == e.getRawSlot()));
        Player plr = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == bankInv) {
            ItemStack cursor = e.getCursor();

            // 0 = withdraw, 1 = deposit, 2 = back
            if (e.getRawSlot() == specials[0]) {
                doWithdraw(plr);
            }
            else if (e.getRawSlot() == specials[1]) {
                doDespoit(plr);
            }

            else if (e.getRawSlot() == specials[2]) {
                plr.closeInventory();
            }

            else if (cursor.getType() == Material.AIR) return;

            else if (MoneyManager.getMoneyFromItem(cursor) == 0) {
                e.setCancelled(true);
            }
        }
    }

    private void doWithdraw(Player plr) {
        plr.closeInventory();
        int bal = BaseCraft.getPlugin().getEconomyManager().getCurrency(plr, Currency.MONEY);
        plr.sendMessage(BankSpeech.HOWMUCH);
        plr.sendMessage(BankSpeech.TYPEBETWEEN + bal);
        waitingForResponse.add(plr.getUniqueId());
    }

    private void doDespoit(Player plr) {
        Inventory plrInv = plr.getInventory();
        int bal = getInventoryValue(plrInv) + getInventoryValue(bankInv);

        for (int slot = 0; slot < plrInv.getSize(); slot++) {
            ItemStack item = plrInv.getItem(slot);
            if (item == null) continue;
            int itemValue = MoneyManager.getMoneyFromItem(plrInv.getItem(slot));

            if (itemValue > 0)
                plrInv.setItem(slot, new ItemStack(Material.AIR));
        }

        plr.closeInventory();
        BaseCraft.getPlugin().getEconomyManager().setCurrency(plr, Currency.MONEY, bal);

        plr.sendMessage(ChatColor.GREEN + "Deposited money!");
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().equals(bankInv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!e.getInventory().equals(bankInv)) return;
        Player plr = (Player) e.getPlayer();
        int balance = getInventoryValue(bankInv);
        BaseCraft.getPlugin().getEconomyManager().setCurrency(plr, Currency.MONEY, balance);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        Player plr = e.getPlayer();
        if (!waitingForResponse.contains(plr.getUniqueId())) return;
        waitingForResponse.remove(plr.getUniqueId());
        e.setCancelled(true);

        int amount;
        try {
            amount = Integer.parseInt(e.getMessage());
        } catch (NumberFormatException ex) {
            plr.sendMessage(BankSpeech.ENTERNUMBER);
            return;
        }

        int bal = BaseCraft.getPlugin().getEconomyManager().getCurrency(plr, Currency.MONEY);

        if (amount > bal || amount < 1) {
            plr.sendMessage(BankSpeech.NOTENOUGHMONEY);
            return;
        }

        int emptySpaces = getEmptySpaceCount(plr.getInventory());
        Bukkit.broadcastMessage(String.valueOf(emptySpaces));
        List<ItemStack> currencyItems = MoneyManager.getCurrencyInItems(amount);
        int itemSpaceNeeded = currencyItems.size();
        if (emptySpaces < itemSpaceNeeded) {
            plr.sendMessage(BankSpeech.NOSPACE);
            return;
        }

        BaseCraft.getPlugin().getEconomyManager().setCurrency(plr, Currency.MONEY, bal - amount);

        for (int i = 0; i < currencyItems.size(); i++) {
            plr.getInventory().addItem(currencyItems.get(i));
        }

        plr.sendMessage(BankSpeech.WITHDRAWN + amount);
    }

    private int getEmptySpaceCount(Inventory inv) {
        int count = 0;
        for (ItemStack item : inv.getStorageContents()) {
            if (item == null) count++;
        }

        return count;
    }

}
