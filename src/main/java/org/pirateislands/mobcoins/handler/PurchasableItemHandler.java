package org.pirateislands.mobcoins.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.mobcoins.Main;
import org.pirateislands.mobcoins.dto.PurchasableItem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PurchasableItemHandler {
    private final transient String fileName;
    private final transient Gson gson;
    private List<PurchasableItem> purchasableItems;
    private Map<EntityType, Integer> mobCoins;

    public PurchasableItemHandler() {
        this.purchasableItems = new LinkedList<>();
        this.mobCoins = new ConcurrentHashMap<>();
        this.fileName = Main.getInstance().getModuleDir() + File.separator + "store.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Optional<PurchasableItem> findByItemStack(final ItemStack itemStack) {
        return this.purchasableItems.stream().filter(item -> item.toStoreIcon().isSimilar(itemStack)).findFirst();
    }

    public List<PurchasableItem> getPurchasableItems() {
        return purchasableItems;
    }

    public void save() {
        String json = this.gson.toJson(this);
        System.out.println(json);
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(this.fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JsonParser parser = new JsonParser();

            try (FileReader fileReader = new FileReader(this.fileName)) {
                JsonElement element = parser.parse(fileReader);
                PurchasableItemHandler auctionHouseGUI = this.gson.fromJson(element, PurchasableItemHandler.class);
                if (auctionHouseGUI == null) {
                    save();
                    return;
                }
                this.purchasableItems = auctionHouseGUI.purchasableItems;
                this.mobCoins = auctionHouseGUI.mobCoins;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<EntityType, Integer> getMobCoins() {
        return mobCoins;
    }
}
