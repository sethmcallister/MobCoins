package org.pirateislands.mobcoins.command;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pirateislands.mobcoins.Main;

public class MobCoinsCommand extends BukkitCommand {
    public MobCoinsCommand() {
        super("mobcoins");
        setAliases(Lists.newArrayList("mc", "coins", "mobcoin", "mcs"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return true;
        }

        Player player = (Player)sender;

        Profile profile = API.getUserManager().findByUniqueId(player.getUniqueId()).getProfile("mobcoins");

        Inventory inventory = createInventory(profile);
        player.openInventory(inventory);
        return true;
    }

    private Inventory createInventory(final Profile profile) {
        Inventory inventory = Bukkit.createInventory(null, getInventorySize(Main.getInstance().getPurchasableItemHandler().getPurchasableItems().size()) * 3, ChatColor.translateAlternateColorCodes('&', "&eMob Coins Store"));

        for (int i = 0; i < 9; i++)
            inventory.setItem(i, getGrayGlass());

        Main.getInstance().getPurchasableItemHandler().getPurchasableItems().forEach(item -> inventory.addItem(item.toStoreIcon()));

        for (int i = (inventory.getSize() - 9); i < inventory.getSize(); i++)
            inventory.setItem(i, getGrayGlass());

        inventory.setItem(inventory.getSize() - 5, getCoinsItem(profile));
        return inventory;
    }

    public ItemStack getGrayGlass() {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
    }

    public ItemStack getCoinsItem(final Profile profile) {
        ItemStack itemStack = new ItemStack(175);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.YELLOW + "Coins: " + ChatColor.WHITE + profile.getDouble("coins").intValue());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private int getInventorySize(int max) {
        if (max < 1)
            return 9;
        if (max > 54)
            return 54;
        max += 8;
        return max - (max % 9);
    }
}
