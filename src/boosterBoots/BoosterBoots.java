/*
Copyright (c) 2016 4a2e532e

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package boosterBoots;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;


public class BoosterBoots implements Listener{

	double dSpeed;
	double iSpeed;
	double gSpeed;
	double lSpeed;
	double cSpeed;

	boolean enableParticles;
	boolean warnElytra;

	double particleAmount;

	static Material fuel;

	private Plugin plugin;

	BoosterBoots(Plugin p){
		plugin = p;
	}

	public void spawnFirework(Player p, Type t, Color c, double amount){
		for(int i = 0; i<amount; i++){
			World world = p.getWorld();
			Location location = p.getLocation();
			Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK);
			FireworkMeta fm = firework.getFireworkMeta();
			fm.addEffect(FireworkEffect.builder().withColor(c).with(t).withFade(Color.BLACK).build());
			firework.setFireworkMeta(fm);
			firework.detonate();
		}
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event){
		try{
			Player p = event.getPlayer();
			ItemStack boots = p.getInventory().getBoots();
			ItemStack elytra = p.getInventory().getChestplate();
			double speed = 0;

			//check if Player is gliding and wearing Booster Boots
			if(!p.isSneaking() && boots!=null && boots.getEnchantmentLevel(Enchantment.PROTECTION_FALL)>=6 && BBMain.checkPerms(p, "bb.use", false) && boots.getItemMeta().getDisplayName().contains("Booster")){

				//EXPERIMENTAL NCP fix
				p.addAttachment(plugin, "nocheatplus.checks", true, BBMain.nocheatplusExemption);
				
				//check type of Boots and set speed
				if(boots.getType()==Material.DIAMOND_BOOTS){
					speed = dSpeed;
				}else if(boots.getType()==Material.IRON_BOOTS){
					speed = iSpeed;
				}else if(boots.getType()==Material.GOLD_BOOTS){
					speed = gSpeed;
				}else if(boots.getType()==Material.LEATHER_BOOTS){
					speed = lSpeed;
				}else if(boots.getType()==Material.CHAINMAIL_BOOTS){
					speed = cSpeed;
				}

				if(p.isGliding()){
					//warn player before his elytra will break
					if(warnElytra==true && elytra.getDurability()>400){
						p.sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.GOLD+BBMain.elytraAboutToBreak);
					}

					//search for fuel
					if(containsFuel(p)){
						//boost player
						Vector direction = p.getLocation().getDirection();
						p.setVelocity(direction.multiply(new Vector(speed, speed, speed)));

						//spawn particles
						if(enableParticles && boots.getItemMeta().getLore().size()>1){
							if(boots.getItemMeta().getLore().get(1).contains(BBMain.orange) && BBMain.checkPerms(p, "bb.particles.orange", false)){
								spawnFirework(p, Type.BURST, Color.ORANGE, particleAmount);
							}
							if(boots.getItemMeta().getLore().get(1).contains(BBMain.red) && BBMain.checkPerms(p, "bb.particles.red", false)){
								spawnFirework(p, Type.BURST, Color.RED, particleAmount);
							}
							if(boots.getItemMeta().getLore().get(1).contains(BBMain.blue) && BBMain.checkPerms(p, "bb.particles.blue", false)){
								spawnFirework(p, Type.BURST, Color.BLUE, particleAmount);
							}
							if(boots.getItemMeta().getLore().get(1).contains(BBMain.yellow) && BBMain.checkPerms(p, "bb.particles.yellow", false)){
								spawnFirework(p, Type.BURST, Color.YELLOW, particleAmount);
							}
							if(boots.getItemMeta().getLore().get(1).contains(BBMain.green) && BBMain.checkPerms(p, "bb.particles.green", false)){
								spawnFirework(p, Type.BURST, Color.GREEN, particleAmount);
							}
						}

						return;
					}
				}else if(BBMain.boolVerticalTakeoffEnabled && (boots.getItemMeta().getLore()==null || !boots.getItemMeta().getLore().get(0).contains(BBMain.disabled))){
					@SuppressWarnings("unused")
					BukkitTask jetpackTask = new JetpackTask(plugin, p, speed).runTaskTimer(plugin, 0, 1);
					return;
				}
			}
		}catch(NullPointerException e){

		}
	}

	public static boolean containsFuel(Player p){
		for(ItemStack itemStack: p.getInventory()){
			if(itemStack!=null){
				if(itemStack.getType()==fuel){
					if(itemStack.getAmount()>1){
						itemStack.setAmount(itemStack.getAmount()-1);
					}else{
						p.getInventory().remove(itemStack);
					}
					return true;
				}
			}
		}
		p.sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+BBMain.outOfFuel);
		return false;
	}
}
