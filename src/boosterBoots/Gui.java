/*
Copyright (c) 2016 4a2e532e

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package boosterBoots;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui implements Listener {

	ItemStack orange = new ItemStack(Material.WOOL, 1, (short) 1);
	ItemStack red = new ItemStack(Material.WOOL, 1, (short) 14);
	ItemStack blue = new ItemStack(Material.WOOL, 1, (short) 11);
	ItemStack yellow = new ItemStack(Material.WOOL, 1, (short) 4);
	ItemStack green = new ItemStack(Material.WOOL, 1, (short) 13);
	ItemStack none = new ItemStack(Material.BARRIER);
	ItemStack elytra = new ItemStack(Material.ELYTRA);
	ItemStack rocket = new ItemStack(Material.FIREWORK);

	private ArrayList<String> lore = new ArrayList<String>();

	@EventHandler
	public void onPrepareAnvilEvent(PrepareAnvilEvent event){
		try{
			String name = event.getResult().getItemMeta().getDisplayName();
			if(name.contains("Booster")){
				event.getView().close();
				event.getView().getPlayer().sendMessage(ChatColor.AQUA+"[Booster Boots] " +ChatColor.RED+BBMain.enchantmentError);
			}
		}catch(Exception e){

		}
	}

	@EventHandler
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent event){
		try{
			Player p = (Player) event.getView().getPlayer();
			if(event.getRecipe().getResult().getItemMeta().getDisplayName().contains("Booster Boots")){
				if(!BBMain.checkPerms(p, "bb.craft", false)){
					event.getView().close();
				}
			}
		}catch(NullPointerException e){

		}
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		try{

			if(event.getItem()==null && event.getPlayer().getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_FALL)>=6 && event.getAction() != Action.PHYSICAL){

				//Open hopper inventory with colored wool blocks
				Inventory inventory = Bukkit.getServer().createInventory(null, InventoryType.DISPENSER, ChatColor.AQUA+BBMain.guiTitle);

				Player p = event.getPlayer();

				setGuiItemName(orange, ChatColor.GOLD, BBMain.orange, "orange", p);
				setGuiItemName(red, ChatColor.RED, BBMain.red, "red", p);
				setGuiItemName(blue, ChatColor.BLUE, BBMain.blue, "blue", p);
				setGuiItemName(yellow, ChatColor.YELLOW, BBMain.yellow, "yellow", p);
				setGuiItemName(green, ChatColor.GREEN, BBMain.green, "green", p);

				ItemMeta nm = none.getItemMeta();
				nm.setDisplayName(BBMain.disableParticles);
				none.setItemMeta(nm);

				ItemMeta em = elytra.getItemMeta();
				em.setDisplayName(BBMain.disableVTO);
				elytra.setItemMeta(em);

				ItemMeta rocketMeta = rocket.getItemMeta();
				rocketMeta.setDisplayName(BBMain.enableVTO);
				rocket.setItemMeta(rocketMeta);

				ItemStack[] items = new ItemStack[9];
				items[0] = orange;
				items[1] = red;
				items[2] = blue;
				items[3] = yellow;
				items[4] = green;
				items[5] = none;
				items[6] = elytra;
				items[8] = rocket;

				//add the wool blocks and open the inventory
				inventory.setContents(items);
				event.getPlayer().openInventory(inventory);
			}
		}catch(Exception e){

		}
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event){
		try{

			//if the player clicks one of the wool blocks while holding booster boots
			if(event.getInventory().getName().equals(ChatColor.AQUA+BBMain.guiTitle) && event.getView().getPlayer().getInventory().getBoots()!=null){
				ItemStack boots = event.getView().getPlayer().getInventory().getBoots();
				ItemMeta meta = boots.getItemMeta();

				if(boots.getEnchantmentLevel(Enchantment.PROTECTION_FALL)>=6 && meta.getDisplayName().contains("Booster")){

					if(lore.size()<1){
						lore.add(null);
						lore.add(null);
					}

					Player p = (Player) event.getView().getPlayer();

					//reset the displayname
					meta.setDisplayName("Booster Boots");

					//and add the particle color to the displayname
					if(event.getCurrentItem().equals(orange)){
						setBootsLore(p, ChatColor.GOLD, BBMain.orange, "orange");

					}else if(event.getCurrentItem().equals(red)){
						setBootsLore(p, ChatColor.RED, BBMain.red, "red");

					}else if(event.getCurrentItem().equals(blue)){
						setBootsLore(p, ChatColor.BLUE, BBMain.blue, "blue");

					}else if(event.getCurrentItem().equals(yellow)){
						setBootsLore(p, ChatColor.YELLOW, BBMain.yellow, "yellow");

					}else if(event.getCurrentItem().equals(green)){
						setBootsLore(p, ChatColor.GREEN, BBMain.green, "green");

					}else if(event.getCurrentItem().equals(none)){
						lore.set(1, BBMain.particlesDisabled);
						meta.setDisplayName(ChatColor.WHITE+meta.getDisplayName());
						p.sendMessage(ChatColor.AQUA+"[Booster Boots] "+ ChatColor.WHITE+BBMain.particlesDisabled);
					}

					else if(event.getCurrentItem().equals(elytra)){
						lore.set(0, BBMain.vto+": "+ChatColor.RED+BBMain.disabled);
						p.sendMessage(ChatColor.AQUA+"[Booster Boots] "+ ChatColor.WHITE+BBMain.vtoDisabled);
					}

					else if(event.getCurrentItem().equals(rocket)){
						lore.set(0, BBMain.vto+": "+ChatColor.GREEN+BBMain.enabled);
						p.sendMessage(ChatColor.AQUA+"[Booster Boots] "+ ChatColor.WHITE+BBMain.vtoEnabled);
					}

					meta.setLore(lore);
					boots.setItemMeta(meta);
				}

				//make sure the inventory is closed after clicking a wool block
				event.setCancelled(true);
			}

		}catch(NullPointerException e){

		}
	}

	public void setGuiItemName(ItemStack item,ChatColor chatColor, String color, String permission, Player p){
		ArrayList<String> lore = new ArrayList<String>();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE+BBMain.particleOption1+chatColor+color+ChatColor.WHITE+" "+BBMain.particleOption2);
		if(BBMain.checkPerms(p, "bb.particles."+permission, true)){
			lore.add(ChatColor.GREEN+BBMain.unlocked);
		}else{
			lore.add(ChatColor.RED+BBMain.locked);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public void setBootsLore(Player p, ChatColor chatColor, String color, String permission){
		if(BBMain.checkPerms(p, "bb.particles."+permission, false)){
			lore.set(1, BBMain.particles+": "+chatColor+color);
			p.sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.WHITE+BBMain.changedParticles1+" "+chatColor+color+ChatColor.WHITE+BBMain.changedParticles2);
		}
	}

}
