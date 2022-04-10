package de.helixdevs.entitytraveller;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collection;

public class EntityTravellerPlugin extends JavaPlugin implements Listener {

    private static final int RESOURCE_ID = 1;
    private static final int BSTATS_ID = 1;
    private String newestVersion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        if(getConfig().getBoolean("update-checker", true)) {
            UpdateChecker updateChecker = new UpdateChecker(this, RESOURCE_ID);
            updateChecker.getVersion(version -> {
                if (getDescription().getVersion().equals(version))
                    return;
                this.newestVersion = version;
                getLogger().warning("New version " + version + " is out. You are still running " + getDescription().getVersion());
                getLogger().warning("Update the plugin at https://www.spigotmc.org/resources/death-chest.101066/");
            });
        }

        getServer().getPluginManager().registerEvents(this, this);

        Metrics metrics = new Metrics(this, BSTATS_ID);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (event.getTo() == null)
            return;


        if(!player.hasPermission("entity.transport"))
            return;

        Collection<Entity> nearbyEntities = world.getNearbyEntities(player.getLocation(), 7, 7, 7, entity -> entity instanceof LivingEntity e && e.isLeashed());

        nearbyEntities.stream()
                .map(entity -> (LivingEntity) entity)
                .filter(livingEntity -> player.equals(livingEntity.getLeashHolder()))
                .forEach(livingEntity -> {
                    Vector entityVector = livingEntity.getLocation().toVector();
                    Vector fromVector = event.getFrom().toVector();

                    Vector diff = fromVector.subtract(entityVector);
                    Location subtract = event.getTo().clone().subtract(diff);
                    if(subtract.getBlock().getType() != Material.AIR)
                        subtract = event.getTo();
                    livingEntity.teleport(subtract);
                });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(newestVersion == null)
            return;
        if(!player.hasPermission("entitytraveller.update"))
            return;

        player.sendMessage("§8[§cEntity Traveller§8] §cA new version " + newestVersion + " is out.");
        player.sendMessage("§8[§cEntity Traveller§8] §cPlease update the plugin at https://www.spigotmc.org/resources/death-chest.101066/");
    }
}
