package com.kunfury.blepRevival;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Help message for blep");
            return true;
        }

        if (args.length <= 2 && args[0].equalsIgnoreCase("token")) {
            if (sender.hasPermission("blep.deathToken")) {
                if (args.length == 2) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p != null)
                        return CreateToken(Bukkit.getPlayer(args[1]));
                    else {
                        sender.sendMessage("Player could not be found.");
                        return true;
                    }
                } else {
                    if (sender instanceof Player)
                        return CreateToken((Player) sender);
                }
            } else
                return (NoPermission(sender));
        }


        sender.sendMessage("That command is not recognized.");
        return true;

    }

    private boolean CreateToken(Player p) {
        p.getInventory().addItem(CreateToken.token);
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "say Blep ");
        p.sendMessage("You have received a Revival Token!");
        return true;
    }


    private boolean NoPermission(CommandSender sender) {
        sender.sendMessage("Sorry, you don't have permission to do that.");
        return true;
    }


}
