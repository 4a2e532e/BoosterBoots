/*
Copyright (c) 2016 4a2e532e

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package boosterBoots;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BBMain extends JavaPlugin{

	static boolean permsRequired;
	static boolean boolVerticalTakeoffEnabled;
	static int nocheatplusExemption;
	static boolean checkVersion;
	static String storeAd;
	static String noPerm1;
	static String noPerm2;
	static String outOfFuel;
	static String elytraAboutToBreak;
	static String enchantmentError;
	static String guiTitle;
	static String orange;
	static String red;
	static String blue;
	static String yellow;
	static String green;
	static String none;
	static String disableParticles;
	static String particleOption1;
	static String particleOption2;
	static String enableVTO;
	static String disableVTO;
	static String changedParticles1;
	static String changedParticles2;
	static String particlesDisabled;
	static String vtoDisabled;
	static String vtoEnabled;
	static String particles;
	static String vto;
	static String enabled;
	static String disabled;
	static String locked;
	static String unlocked;

	private ArrayList<String> defaultDiamondRecipe = new ArrayList<String>();
	private ArrayList<String> defaultIronRecipe = new ArrayList<String>();
	private ArrayList<String> defaultGoldRecipe = new ArrayList<String>();
	private ArrayList<String> defaultLeatherRecipe = new ArrayList<String>();
	private ArrayList<String> defaultChainRecipe = new ArrayList<String>();

	private ArrayList<String> diamondRecipe = new ArrayList<String>();
	private ArrayList<String> ironRecipe = new ArrayList<String>();
	private ArrayList<String> goldRecipe = new ArrayList<String>();
	private ArrayList<String> leatherRecipe = new ArrayList<String>();
	private ArrayList<String> chainRecipe = new ArrayList<String>();

	BoosterBoots bbemListener = new BoosterBoots(this);
	Gui gui = new Gui();
	ParticleManager particleManager = new ParticleManager(this);

	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(bbemListener, this);
		getServer().getPluginManager().registerEvents(gui, this);
		this.getCommand("bb").setExecutor(new Reload(this));

		defaultDiamondRecipe.add(""+Material.NETHER_STAR.toString());
		defaultDiamondRecipe.add(""+Material.DIAMOND_BOOTS.toString());

		defaultIronRecipe.add(""+Material.NETHER_STAR.toString());
		defaultIronRecipe.add(""+Material.IRON_BOOTS.toString());

		defaultGoldRecipe.add(""+Material.NETHER_STAR.toString());
		defaultGoldRecipe.add(""+Material.GOLD_BOOTS.toString());

		defaultLeatherRecipe.add(""+Material.NETHER_STAR.toString());
		defaultLeatherRecipe.add(""+Material.LEATHER_BOOTS.toString());

		defaultChainRecipe.add(""+Material.NETHER_STAR.toString());
		defaultChainRecipe.add(""+Material.CHAINMAIL_BOOTS.toString());

		loadConfig();

		//add Recipes
		addRecipe(Material.DIAMOND_BOOTS, "Booster Boots", diamondRecipe, 100);
		addRecipe(Material.IRON_BOOTS, "Booster Boots", ironRecipe, 75);
		addRecipe(Material.GOLD_BOOTS, "Booster Boots", goldRecipe, 75);
		addRecipe(Material.LEATHER_BOOTS, "Booster Boots", leatherRecipe, 10);
		addRecipe(Material.CHAINMAIL_BOOTS, "Booster Boots", chainRecipe, 50);

		//warn that flying has to be enabled
		if(boolVerticalTakeoffEnabled && !Bukkit.getAllowFlight()){
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+"[WARNING]: Please enable flying in the server.properties file to use vertical take-off !");
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+"[WARNING]: Vertical take-off WILL trigger most anti-cheat plugins !");
		}else{
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.GOLD+"[Info]: This plugin can trigger anti-cheat plugins!");
		}

		if(bbemListener.enableParticles){
			@SuppressWarnings("unused")
			BukkitTask task = particleManager.runTaskTimer(this, 0, 1);
		}

		if(checkVersion){
			isVersionUpToDate();
		}else{
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+"[Info]: Version check disabled, make sure you are running the latest version!");
		}
	}

	@Override
	public void onDisable(){

	}

	@SuppressWarnings("unchecked")
	public void loadConfig(){
		//setup config
		FileConfiguration config = this.getConfig();
		config.addDefault("Diamond_Booster_Boots_Speed", 10);
		config.addDefault("Iron_Booster_Boots_Speed", 7.5);
		config.addDefault("Gold_Booster_Boots_Speed", 7.5);
		config.addDefault("Leather_Booster_Boots_Speed", 1);
		config.addDefault("Chain_Booster_Boots_Speed", 5);
		config.addDefault("Enable particles", true);
		config.addDefault("Particle amount", 5);
		config.addDefault("Warn player when elytra is about to break", true);
		config.addDefault("fuel", "FIREBALL");
		config.addDefault("Allow vertical takeoff", true);
		config.addDefault("NoCheatPlus exemption ticks", 200);
		config.addDefault("Permissions required", false);
		config.addDefault("Diamond_Booster_Boots_Recipe", defaultDiamondRecipe);
		config.addDefault("Iron_Booster_Boots_Recipe", defaultIronRecipe);
		config.addDefault("Gold_Booster_Boots_Recipe", defaultGoldRecipe);
		config.addDefault("Leather_Booster_Boots_Recipe", defaultLeatherRecipe);
		config.addDefault("Chain_Booster_Boots_Recipe", defaultChainRecipe);
		config.addDefault("Store advertisement", "These particles are locked!");
		config.addDefault("Do version check", true);
		config.options().copyDefaults(true);
		saveConfig();

		//read config
		bbemListener.dSpeed = config.getDouble("Diamond_Booster_Boots_Speed");
		bbemListener.iSpeed = config.getDouble("Iron_Booster_Boots_Speed");
		bbemListener.gSpeed = config.getDouble("Gold_Booster_Boots_Speed");
		bbemListener.lSpeed = config.getDouble("Leather_Booster_Boots_Speed");
		bbemListener.cSpeed = config.getDouble("Chain_Booster_Boots_Speed");
		bbemListener.enableParticles = config.getBoolean("Enable particles");
		bbemListener.particleAmount = config.getDouble("Particle amount");
		bbemListener.warnElytra = config.getBoolean("Warn player when elytra is about to break");
		BoosterBoots.fuel = Material.valueOf(config.getString("fuel"));
		boolVerticalTakeoffEnabled = config.getBoolean("Allow vertical takeoff");
		nocheatplusExemption = config.getInt("NoCheatPlus exemption ticks");
		permsRequired = config.getBoolean("Permissions required");
		diamondRecipe = (ArrayList<String>) config.getList("Diamond_Booster_Boots_Recipe");
		ironRecipe = (ArrayList<String>) config.getList("Iron_Booster_Boots_Recipe");
		goldRecipe = (ArrayList<String>) config.getList("Gold_Booster_Boots_Recipe");
		leatherRecipe = (ArrayList<String>) config.getList("Leather_Booster_Boots_Recipe");
		chainRecipe = (ArrayList<String>) config.getList("Chain_Booster_Boots_Recipe");
		storeAd = config.getString("Store advertisement");
		checkVersion = config.getBoolean("Do version check");

		//setup language file
		File language = new File(getDataFolder(), "language.yml");
		FileConfiguration lang = YamlConfiguration.loadConfiguration(language);

		lang.addDefault("No permission part 1", "You need the permission");
		lang.addDefault("No permission part 2", "to do this!");
		lang.addDefault("Out of fuel", "Out of fuel!");
		lang.addDefault("Elytra about to break", "[Warning] Your elytra is about to break!");
		lang.addDefault("Enchantment error", "You can't enchant Booster Boots!");
		lang.addDefault("Gui title", "Booster Boots settings");
		lang.addDefault("Gui orange", "orange");
		lang.addDefault("Gui red", "red");
		lang.addDefault("Gui blue", "blue");
		lang.addDefault("Gui yellow", "yellow");
		lang.addDefault("Gui green", "green");
		lang.addDefault("Gui Disable particles", "Disable particles");
		lang.addDefault("Gui particle-option-name part 1", "");
		lang.addDefault("Gui particle-option-name part 2", "particles");
		lang.addDefault("Gui enable vertical take-off", "Enable vertical take-off");
		lang.addDefault("Gui disable vertical take-off", "Disable vertical take-off");
		lang.addDefault("Changed particle color part 1", "Particle color set to");
		lang.addDefault("Changed particle color part 2", "!");
		lang.addDefault("Particles disabled", "Particles disabled!");
		lang.addDefault("Vertical take-off disabled", "Vertical take-off disabled!");
		lang.addDefault("Vertical take-off enabled", "Vertical take-off enabled!");
		lang.addDefault("Particles", "particles");
		lang.addDefault("Verticle take-off", "Verticle take-off");
		lang.addDefault("Enabled", "enabled");
		lang.addDefault("Disabled", "disabled");
		lang.addDefault("Locked", "Locked");
		lang.addDefault("Unlocked", "Unlocked");
		
		lang.options().copyDefaults(true);

		try {
			lang.save(language);
		} catch (IOException e) {

		}

		//read language file
		noPerm1 = lang.getString("No permission part 1");
		noPerm2 = lang.getString("No permission part 2");
		outOfFuel = lang.getString("Out of fuel");
		elytraAboutToBreak = lang.getString("Elytra about to break");
		enchantmentError = lang.getString("Enchantment error");
		guiTitle = lang.getString("Gui title");
		orange = lang.getString("Gui orange");
		red = lang.getString("Gui red");
		blue = lang.getString("Gui blue");
		yellow = lang.getString("Gui yellow");
		green = lang.getString("Gui green");
		disableParticles = lang.getString("Gui Disable particles");
		particleOption1 = lang.getString("Gui particle-option-name part 1");
		particleOption2 = lang.getString("Gui particle-option-name part 2");
		enableVTO = lang.getString("Gui enable vertical take-off");
		disableVTO = lang.getString("Gui disable vertical take-off");
		changedParticles1 = lang.getString("Changed particle color part 1");
		changedParticles2 = lang.getString("Changed particle color part 2");
		particlesDisabled = lang.getString("Particles disabled");
		vtoDisabled = lang.getString("Vertical take-off disabled");
		vtoEnabled = lang.getString("Vertical take-off enabled");
		particles = lang.getString("Particles");
		vto = lang.getString("Verticle take-off");
		enabled = lang.getString("Enabled");
		disabled = lang.getString("Disabled");
		locked = lang.getString("Locked");
		unlocked = lang.getString("Unlocked");
	}

	public void addRecipe(Material material, String name, ArrayList<String> ingredients, int featherFalling){
		ItemStack boosterBoots = new ItemStack(material);
		ItemMeta meta = boosterBoots.getItemMeta();
		meta.setDisplayName(name);
		//meta.spigot().setUnbreakable(true);
		meta.addEnchant(Enchantment.DURABILITY, 100, true);
		meta.addEnchant(Enchantment.PROTECTION_FALL, featherFalling, true);
		boosterBoots.setItemMeta(meta);
		ShapelessRecipe recipe = new ShapelessRecipe(boosterBoots);
		for(String s: ingredients){
			recipe.addIngredient(Material.valueOf(s));
		}
		getServer().addRecipe(recipe);
	}

	public static boolean checkPerms(Player p, String perm, boolean silent){
		if(!permsRequired || p.hasPermission(perm)){
			return true;
		}else if(!silent){
			if(perm.contains("particles")){
				p.sendMessage(ChatColor.RED+storeAd);
			}else{
				p.sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+noPerm1+" "+ChatColor.DARK_RED+perm+ChatColor.RED+" "+noPerm2);
			}
			return false;
		}else{
			return false;
		}
	}

	private void isVersionUpToDate(){
		try {
			URL versionFile = new URL("https://www.dropbox.com/s/mvmct901y01v77z/version.txt?dl=1");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(versionFile.openStream());
			String newestVersion = scanner.next();
			String news = "Update news: ";
			while(scanner.hasNext()){
				news = news+" "+scanner.next();
			}
			if(!this.getDescription().getVersion().equals(newestVersion)){
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+"[WARNING]: You are currently using version: "+this.getDescription().getVersion()+", Newest version is: "+newestVersion);
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+"[WARNING]: You are using an outdated version ! You can get the newest version here: "+ChatColor.YELLOW+"https://www.spigotmc.org/resources/booster-boots.21792/");
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.WHITE+news);
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[Booster Boots] "+ChatColor.RED+"[WARNING]: Unable to perform version check ! You can get the newest version here: https://www.spigotmc.org/resources/booster-boots.21792/");
		}
	}
}
