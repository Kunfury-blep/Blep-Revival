package com.kunfury.blepRevival;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Death implements Listener {

    String filePath = Setup.dataFolder + "/Dead Players.data";


    @EventHandler
    public void onDeath(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Location spawn = player.getBedSpawnLocation();
        if (spawn == null)
            spawn = player.getWorld().getSpawnLocation();

        EntityDamageEvent ede = player.getLastDamageCause();
        DamageCause dc = ede.getCause();


        //Iterates through deadplayers, and removes all duplicates
        List<PlayerObj> duplicates = new ArrayList<PlayerObj>();
        Revive.DeadPlayers.forEach(p -> {
            if (p.GetUUID().equals(player.getUniqueId()))
                duplicates.add(p);
        });
        Revive.DeadPlayers.removeAll(duplicates);

        Revive.DeadPlayers.add(new PlayerObj(player.getUniqueId(), new Date(), dc));

        FileHandler.SaveFallen();
    }

}
