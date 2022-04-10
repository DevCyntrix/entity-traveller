package de.helixdevs.entitytraveller;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collection;

public class EntityTravellerPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
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
}
