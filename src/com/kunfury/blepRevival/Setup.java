package com.kunfury.blepRevival;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Setup extends JavaPlugin {
	 private static Plugin plugin;
	 public static File dataFolder;
	 public static FileConfiguration config;
	 
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	plugin = this;
    	dataFolder = getDataFolder();
    	
    	config = this.getConfig();
    	
    	File configFile;
    	configFile = new File(getDataFolder(), "config.yml");
    	 
    	if(!configFile.exists()){
            ConfigCreate();
	        }
    	saveConfig();
    	
    	getServer().getPluginManager().registerEvents(new Revive(), this);
    	getServer().getPluginManager().registerEvents(new Death(), this);
    	
    	this.getCommand("blep").setExecutor(new Commands());
    	
    	FileHandler.LoadFallen();
    	Revive.CreateToken();
    }
       
    public static Plugin getPlugin() {
        return plugin;
    }
    
    private void ConfigCreate() {
    	config.set("Crafting.Revival Token.Top Left", "NETHERITE_BLOCK");
    	config.set("Crafting.Revival Token.Top Center", "TOTEM_OF_UNDYING");
    	config.set("Crafting.Revival Token.Top Right", "NETHERITE_BLOCK");
    	
    	config.set("Crafting.Revival Token.Middle Left", "HEART_OF_THE_SEA");
    	config.set("Crafting.Revival Token.Middle Center", "NETHER_STAR");
    	config.set("Crafting.Revival Token.Middle Right", "SHULKER_BOX");
    	
    	config.set("Crafting.Revival Token.Bottom Left", "NETHERITE_BLOCK");
    	config.set("Crafting.Revival Token.Bottom Center", "END_CRYSTAL");
    	config.set("Crafting.Revival Token.Bottom Right", "NETHERITE_BLOCK");
    }
    
    
    
}
