package com.kunfury.blepRevival;

import com.kunfury.blepRevival.config.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CreateToken {

    public static NamespacedKey key;
    public static ItemStack token;

    //Handles token and recipe creation, as well as giving the recipe to all players
    public CreateToken() {
        token = new ItemStack(Material.END_CRYSTAL, 1);
        token.addUnsafeEnchantment(Enchantment.LOYALTY, 10);

        ItemMeta meta = token.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        lore.add("Choose a fallen ally to resurrect");
        lore.add("They will appear at your location");
        meta.setLore(lore);
        meta.setDisplayName(Variables.TokenName);
        token.setItemMeta(meta);

        //Recipe Creation
        key = new NamespacedKey(Setup.getPlugin(), "Token");
        ShapedRecipe tokenRecipe = new ShapedRecipe(key, token);
        tokenRecipe.shape("123", "456", "789");
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

        //This doesn't work for some reason
        Bukkit.getScheduler().scheduleSyncDelayedTask(Setup.getPlugin(), () -> {
            for (Player p : Setup.getPlugin().getServer().getOnlinePlayers()) {
                p.discoverRecipe(key);
            }
        }, 1L); // 600L (ticks) is equal to 30 seconds (20 ticks = 1 second)


    }
}
