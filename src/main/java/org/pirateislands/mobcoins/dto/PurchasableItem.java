package org.pirateislands.mobcoins.dto;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PurchasableItem {
    private final GooseItem gooseItem;
    private final int cost;

    public PurchasableItem(final GooseItem gooseItem, final int cost) {
        this.gooseItem = gooseItem;
        this.cost = cost;
    }

    public GooseItem getGooseItem() {
        return gooseItem;
    }

    public ItemStack toStoreIcon() {
        ItemStack itemStack = getGooseItem().toItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Coins:");
        lore.add(ChatColor.GRAY + " \u00BB " + ChatColor.WHITE + cost);
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getCost() {
        return cost;
    }
}
