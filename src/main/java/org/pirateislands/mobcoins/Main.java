package org.pirateislands.mobcoins;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.module.PluginModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.mobcoins.command.MobCoinsCommand;
import org.pirateislands.mobcoins.dto.GooseItem;
import org.pirateislands.mobcoins.dto.PurchasableItem;
import org.pirateislands.mobcoins.handler.PurchasableItemHandler;
import org.pirateislands.mobcoins.listener.EntityDeathListener;
import org.pirateislands.mobcoins.listener.InventoryClickListener;
import org.pirateislands.mobcoins.listener.PlayerJoinListener;

public class Main extends PluginModule {
    private static Main instance;

    private PurchasableItemHandler purchasableItemHandler;

    @Override
    public void onEnable() {
        getModuleDir().toFile().mkdir();
        setInstance(this);
        this.purchasableItemHandler = new PurchasableItemHandler();
        this.purchasableItemHandler.load();

        if (this.purchasableItemHandler.getPurchasableItems().isEmpty()) {
            ItemStack itemStack = new ItemStack(Material.GRASS);
            PurchasableItem purchasableItem = new PurchasableItem(GooseItem.fromItemStack(itemStack), 10);
            this.purchasableItemHandler.getPurchasableItems().add(purchasableItem);
            this.purchasableItemHandler.save();
        }

        if (this.purchasableItemHandler.getMobCoins().isEmpty()) {
            this.purchasableItemHandler.getMobCoins().put(EntityType.SKELETON, 1);
            this.purchasableItemHandler.save();
        }

        registerCommand("mobcoins", new MobCoinsCommand());
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), API.getPlugin());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), API.getPlugin());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), API.getPlugin());
    }

    public static Main getInstance() {
        return instance;
    }

    private static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public PurchasableItemHandler getPurchasableItemHandler() {
        return purchasableItemHandler;
    }
}
