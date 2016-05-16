/*
Copyright (c) 2016 4a2e532e

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package boosterBoots;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleManager extends BukkitRunnable{

	private int particleAmount = 2;
	@SuppressWarnings("unused")
	private Plugin plugin;

	public ParticleManager(Plugin p) {
		plugin = p;
	}

	@Override
	public void run() {
		@SuppressWarnings("unchecked")
		List<Player> onlinePlayers = (List<Player>) Bukkit.getServer().getOnlinePlayers();


		for(Player p: onlinePlayers){

			if(p.getInventory().getBoots()!=null){
				ItemStack boots = p.getInventory().getBoots();

				if(boots.getItemMeta().getLore()!=null && boots.getItemMeta().getLore().size()>1){
					String lore = boots.getItemMeta().getLore().get(1);

					if((p.isGliding() || (p.isSneaking() && BBMain.boolVerticalTakeoffEnabled)) && (boots.getEnchantmentLevel(Enchantment.PROTECTION_FALL)>=6 && boots.getItemMeta().getDisplayName().contains("Booster"))){
						
						if(lore.contains(BBMain.orange)){
							playParticles(p, onlinePlayers, "orange", Particle.FLAME, particleAmount);

						}else if(lore.contains(BBMain.red)){
							playParticles(p, onlinePlayers, "red", Particle.LAVA, particleAmount);

						}else if(lore.contains(BBMain.blue)){
							playParticles(p, onlinePlayers, "blue", Particle.WATER_SPLASH, 10);

						}else if(lore.contains(BBMain.yellow)){
							playParticles(p, onlinePlayers, "yellow", Particle.VILLAGER_ANGRY, particleAmount);

						}else if(lore.contains(BBMain.green)){
							playParticles(p, onlinePlayers, "green", Particle.VILLAGER_HAPPY, particleAmount);
						}
					}
				}else if(boots.getItemMeta().getLore()!=null){
					boots.getItemMeta().getLore().add(null);
				}
			}
		}
	}
	
	public void playParticles(Player p, List<Player> onlinePlayers, String color, Particle particle, int amount){
		if(BBMain.checkPerms(p, "bb.particles."+color, true)){
			for(Player players: onlinePlayers){
				players.spawnParticle(particle, p.getLocation(), amount);
			}
		}
	}
}
