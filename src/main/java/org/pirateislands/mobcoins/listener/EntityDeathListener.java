package org.pirateislands.mobcoins.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.pirateislands.mobcoins.Main;


public class EntityDeathListener implements Listener {
    private int getCoins(final EntityType type) {
        return Main.getInstance().getPurchasableItemHandler().getMobCoins().get(type);
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;

        final LivingEntity entity = event.getEntity();
        final Player player = entity.getKiller();

        new BukkitRunnable() {
            @Override
            public void run() {
                Profile profile = API.getUserManager().findByUniqueId(player.getUniqueId()).getProfile("mobcoins");
                if (profile == null)
                    return;

                int coins = getCoins(entity.getType());
                if (coins == 0)
                    return;

                profile.set("coins", profile.getDouble("coins") + coins);
            }
        }.runTaskAsynchronously(API.getPlugin());

    }
}
