package org.pirateislands.mobcoins.listener;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                User user = API.getUserManager().findByUniqueId(player.getUniqueId());
                Profile profile = user.getProfile("mobcoins");
                if (profile == null) {
                    profile = new Profile("mobcoins");
                    profile.set("coins", 0d);
                    user.getAllProfiles().add(profile);
                }
            }
        }.runTaskAsynchronously(API.getPlugin());
    }
}
