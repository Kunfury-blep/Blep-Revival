package com.kunfury.blepRevival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Revive implements Listener {

	public static List<PlayerObj> DeadPlayers = new ArrayList<>();
	List<PlayerObj> trimmedPlayers = new ArrayList<>();

	public static ItemStack token;
	Inventory inv;
	public static NamespacedKey key;
	ItemStack hItem;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {	
		
		Player player = e.getPlayer();	
		hItem = e.getItem();		
		
		if((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && e.hasItem() && hItem.equals(token)) {
			inv = Bukkit.createInventory(null, 54, "Fallen Heroes");
			
			trimmedPlayers.clear();
			DeadPlayers.forEach(p -> {
				if(Bukkit.getPlayer(p.GetUUID()) != null && !trimmedPlayers.contains(p)) {
					trimmedPlayers.add(p);
					inv.addItem(GetPlayer(p));
				}
			});
			player.openInventory(inv);
		}
		
	}
    
	@EventHandler()
    public void clickEvent(InventoryClickEvent e) { //Handles the revival of the player
		if(e.getInventory().equals(inv) && e.getClickedInventory() != null && e.getRawSlot() < trimmedPlayers.size()) {			
			e.setCancelled(true);
			if(trimmedPlayers.get(e.getRawSlot()) != null) {
				PlayerObj cPlayer = trimmedPlayers.get(e.getRawSlot());
				
				if(Bukkit.getPlayer(cPlayer.GetUUID()) != null){
					Player p = Bukkit.getPlayer(cPlayer.GetUUID());
					
					Location spawnLoc = e.getWhoClicked().getLocation().add(e.getWhoClicked().getLocation().getDirection().multiply(2));
					spawnLoc.setY(spawnLoc.getY() + 1);
					
					Vector dir = spawnLoc.clone().subtract(e.getWhoClicked().getEyeLocation()).toVector();
					
					spawnLoc.setDirection(dir);
					RevivePlayer(p, spawnLoc, e.getWhoClicked().getWorld());
					
					DeadPlayers.remove(cPlayer);
					hItem.setAmount(hItem.getAmount() - 1);
					e.getWhoClicked().closeInventory();
				}else {
					e.getWhoClicked().sendMessage("Can't find that player");
				}
				
				
			}

		}
	}
	
	
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
		e.getPlayer().discoverRecipe(key);
    }
	
	private void RevivePlayer(Player p, Location loc, World world) {
		world.strikeLightningEffect(loc);
		
		p.teleport(loc);
		p.setGameMode(GameMode.SURVIVAL);
		p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(1200, 0));
		p.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(1200, 0));
		p.addPotionEffect(PotionEffectType.REGENERATION.createEffect(1200, 4));
		p.addPotionEffect(PotionEffectType.UNLUCK.createEffect(1200, 0));
		p.addPotionEffect(PotionEffectType.GLOWING.createEffect(60, 0));
		FileHandler.SaveFallen();
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
		
		
	}
	
    private ItemStack GetPlayer(PlayerObj pObj) {
    	Player player = Bukkit.getPlayer(pObj.GetUUID());
    	
    	final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(player.getName());
        
        meta.setOwningPlayer(player);

        ArrayList<String> lore = new ArrayList<String>();
        
        long secs = (new Date().getTime() - pObj.GetDate().getTime()) / 1000;
        int days = (int) secs / 86400; 
        secs = secs - (days * 86400);
        int hours = (int) secs / 3600; 
        secs = secs - (hours * 3600);
        int mins = (int) secs / 60;
        lore.add("Died " + days + "d, " + hours + "h, " + mins + "m ago");
        lore.add("Cause of Death: " + pObj.DC);
        lore.add("Death Count: " + player.getStatistic(Statistic.DEATHS));
        
        // Set the lore of the item
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }
    
    //Handles token and recipe creation, as well as giving the recipe to all players
    public static void CreateToken() {
    	token = new ItemStack(Material.END_CRYSTAL, 1);
    	token.addUnsafeEnchantment(Enchantment.LOYALTY, 10);
    	
    	ItemMeta meta = token.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
    	
		lore.add("Choose a fallen ally to ressurect");
		lore.add("They will appear at your location");
		meta.setLore(lore);
		meta.setDisplayName("Revival Token");
		token.setItemMeta(meta);		
		
		//Recipe Creation
		key = new NamespacedKey(Setup.getPlugin(), "Token");
		ShapedRecipe tokenRecipe = new ShapedRecipe(key, token);
		tokenRecipe.shape("123","456","789");
		tokenRecipe.setIngredient('1', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Top Left")));
		tokenRecipe.setIngredient('2', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Top Center")));
		tokenRecipe.setIngredient('3', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Top Right")));
		tokenRecipe.setIngredient('4', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Middle Left")));
		tokenRecipe.setIngredient('5', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Middle Center")));
		tokenRecipe.setIngredient('6', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Middle Right")));
		tokenRecipe.setIngredient('7', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Bottom Left")));
		tokenRecipe.setIngredient('8', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Bottom Center")));
		tokenRecipe.setIngredient('9', Material.getMaterial(Setup.config.getString("Crafting.Revival Token.Bottom Right")));
	    Bukkit.addRecipe(tokenRecipe);
	    
	    Bukkit.getScheduler().scheduleSyncDelayedTask(Setup.getPlugin(), new Runnable() { //This doesn't work for some reason
            public void run() {
            	for(Player p : Setup.getPlugin().getServer().getOnlinePlayers()) {
        	    	p.discoverRecipe(key);
        	    }
            }
        }, 1L); // 600L (ticks) is equal to 30 seconds (20 ticks = 1 second)
	    
	    
	    
    }
    

}
