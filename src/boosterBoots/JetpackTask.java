/*
Copyright (c) 2016 4a2e532e

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package boosterBoots;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class JetpackTask extends BukkitRunnable{
	
	private Player p;
	private double speed;
	private Plugin plugin;
	private int timerStart = 50;
	private int timer = timerStart;
	
	JetpackTask(Plugin pl, Player player, double s){
		plugin = pl;
		p = player;
		speed = s;
	}

	@Override
	public void run() {
		if(p.isSneaking() && (timer!=timerStart || BoosterBoots.containsFuel(p))){
			
			//EXPERIMENTAL NCP fix
			p.addAttachment(plugin, "nocheatplus.checks", true, BBMain.nocheatplusExemption);
			
			p.setVelocity(p.getVelocity().add(new Vector(0, speed/10, 0)));
			timer--;
			if(timer==0){
				timer = timerStart;
			}
		}else{
			p.setGliding(true);
			this.cancel();
		}
		
	}

}
