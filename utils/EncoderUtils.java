package com.glatinis.basecraft.utils;
import com.glatinis.basecraft.BaseCraft;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class EncoderUtils {
    public static void storeItems(List<ItemStack> items, Player p, String keyName){
        PersistentDataContainer data = p.getPersistentDataContainer();

        if (items.isEmpty()) {
            data.set(new NamespacedKey(BaseCraft.getPlugin(), keyName), PersistentDataType.STRING, "");
        } else {

            try{
                ByteArrayOutputStream io = new ByteArrayOutputStream();
                BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);

                os.writeInt(items.size());

                for (int i = 0; i < items.size(); i++){
                    os.writeObject(items.get(i));
                }

                os.flush();

                byte[] rawData = io.toByteArray();
                String encodedData = Base64.getEncoder().encodeToString(rawData);
                data.set(new NamespacedKey(BaseCraft.getPlugin(), keyName), PersistentDataType.STRING, encodedData);

                os.close();
            } catch (IOException ex){
                System.out.println(ex);
            }
        }
    }

    public static ArrayList<ItemStack> getItems(Player p, String keyName) {
        PersistentDataContainer data = p.getPersistentDataContainer();
        ArrayList<ItemStack> items = new ArrayList<>();

        String encodedItems = data.get(new NamespacedKey(BaseCraft.getPlugin(), keyName), PersistentDataType.STRING);

        if (encodedItems != null && !encodedItems.isEmpty()){
            byte[] rawData = Base64.getDecoder().decode(encodedItems);

            try {
                ByteArrayInputStream io = new ByteArrayInputStream(rawData);
                BukkitObjectInputStream in = new BukkitObjectInputStream(io);
                int itemsCount = in.readInt();

                for (int i = 0; i < itemsCount; i++){
                    items.add((ItemStack) in.readObject());
                }

                in.close();

            } catch (IOException | ClassNotFoundException ex){
                System.out.println(ex);
            }
        }

        return items;
    }

}
