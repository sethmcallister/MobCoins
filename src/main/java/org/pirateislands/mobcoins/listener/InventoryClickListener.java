package org.pirateislands.mobcoins.listener;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.pirateislands.mobcoins.Main;
import org.pirateislands.mobcoins.dto.PurchasableItem;

import java.util.Optional;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!ChatColor.stripColor(event.getInventory().getTitle()).equalsIgnoreCase("Mob Coins Store")){
            return;
        }

        int slot = event.getRawSlot();
        ItemStack itemStack = event.getInventory().getItem(slot);
        if (itemStack == null)
            return;

        if (itemStack.getType().equals(Material.AIR))
            return;

        Optional<PurchasableItem> purchasableItemOptional = Main.getInstance().getPurchasableItemHandler().findByItemStack(itemStack);
        if (!purchasableItemOptional.isPresent())
            return;

        Player player = (Player)event.getWhoClicked();
        player.closeInventory();
        event.setCancelled(true);

        PurchasableItem purchasableItem = purchasableItemOptional.get();

        Profile profile = API.getUserManager().findByUniqueId(event.getWhoClicked().getUniqueId()).getProfile("mobcoins");
        int coins = profile.getDouble("coins").intValue();
        if (purchasableItem.getCost() > coins) {
            player.sendMessage(ChatColor.RED + "You do not have enough mob coins to purchase this.");
            return;
        }

        player.getInventory().addItem(purchasableItem.getGooseItem().toItemStack());

        int left = coins - purchasableItem.getCost();
        profile.set("coins", (double) left);
        player.sendMessage(ChatColor.YELLOW + String.format("You have purchased %s for %s mob coins.", purchasableItem.getGooseItem().getName(), purchasableItem.getCost()));
        player.sendMessage(ChatColor.GRAY + " \u00BB " + ChatColor.YELLOW + String.format("You have %s mob coins left.", left));
    }
}
