package com.kunfury.blepRevival;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Death implements Listener {

	String filePath = Setup.dataFolder + "/Dead Players.data";
	
	
	
	@EventHandler
	public void onDeath(PlayerRespawnEvent  e) {	
			Player player = e.getPlayer();
			Location spawn = player.getBedSpawnLocation();
			if(spawn == null)
				spawn = player.getWorld().getSpawnLocation();
			
				EntityDamageEvent ede = player.getLastDamageCause();
    			DamageCause dc = ede.getCause();
    			
    			
    			//Iterates through deadplayers, and removes all duplicates
    			List<PlayerObj> duplicates = new ArrayList<PlayerObj>();
    			Revive.DeadPlayers.forEach(p ->{
    				if(p.GetUUID().equals(player.getUniqueId())) 
    					duplicates.add(p);
    			});
    			Revive.DeadPlayers.removeAll(duplicates);
    			
    			Revive.DeadPlayers.add(new PlayerObj(player.getUniqueId(), new Date(), dc));
                
    			FileHandler.SaveFallen();			
		}
		
	
	//Possibly Unneeded
	/*
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void forceRespawn(Player player) {
        try {

            String bukkitVersion = Bukkit.getServer().getClass()
                    .getPackage().getName().substring(23);

            Class<?> cp = Class.forName("org.bukkit.craftbukkit."
                    + bukkitVersion + ".entity.CraftPlayer");
            Class<?> clientCmd = Class.forName("net.minecraft.server."
                    + bukkitVersion + ".PacketPlayInClientCommand");
            Class enumClientCMD = Class.forName("net.minecraft.server."
                    + bukkitVersion + ".PacketPlayInClientCommand$EnumClientCommand");

            Method handle = cp.getDeclaredMethod("getHandle");

            Object entityPlayer = handle.invoke(player);

            Constructor<?> packetConstr = clientCmd
                    .getDeclaredConstructor(enumClientCMD);
            Enum<?> num = Enum.valueOf(enumClientCMD, "PERFORM_RESPAWN");

            Object packet = packetConstr.newInstance(num);

            Object playerConnection = entityPlayer.getClass()
                    .getDeclaredField("playerConnection").get(entityPlayer);
            Method send = playerConnection.getClass().getDeclaredMethod("a",
                    clientCmd);

            send.invoke(playerConnection, packet);

        } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                NoSuchMethodException | ClassNotFoundException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }

    }
	*/
 
}
