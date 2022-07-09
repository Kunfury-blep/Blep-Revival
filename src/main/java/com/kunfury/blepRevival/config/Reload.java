package com.kunfury.blepRevival.config;

import com.kunfury.blepRevival.Setup;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class Reload {

    public void ReloadPlugin(CommandSender s) {
        Setup.setup.reloadConfig();
        Setup.config = Setup.setup.getConfig();

        if (Setup.config.getKeys(false).size() == 0) {
            Bukkit.getLogger().warning("No/Empty Config for Blep Revival! Blep Revival has been disabled.");
            Setup.getPlugin().getServer().getPluginManager().disablePlugin(Setup.getPlugin());
            return;
        }

        Set<String> existCheck = Setup.config.getConfigurationSection("").getKeys(false); //Gets all the config-

        Variables.TokenName = Setup.config.getString("Token Name");

    }


}
