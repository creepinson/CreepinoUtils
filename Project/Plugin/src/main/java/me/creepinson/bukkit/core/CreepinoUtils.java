package me.creepinson.bukkit.core;

import me.creepinson.utils.PluginModule;
import me.creepinson.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class CreepinoUtils extends JavaPlugin {
    private static CreepinoUtils plugin;
    private static List<PluginModule> modules;

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();

		for (PluginModule m : getAllPluginModules()) {
			m.onDisable();
		}
    }

    public static CreepinoUtils getInstance(){
        return plugin;
    }


    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return cfile;
    }

    private YamlConfiguration config;
    private File cfile;

    @Override
    public void onEnable() {

        plugin = this;

        PluginDescriptionFile pdfFile = this.getDescription();
        this.config = new YamlConfiguration();
        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        this.cfile = new File(getDataFolder(), "config.yml");
        if(this.cfile.exists()){
            this.config = YamlConfiguration.loadConfiguration(this.cfile);
        }
        this.getCommand("announce").setExecutor(new CommandAnnounce());

        try {
            if (this.getConfig().getBoolean("announcer-enabled")) {
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

                    public void run() {

                        Bukkit.getServer()
                                .broadcastMessage(TextUtils.withColor(CreepinoUtils.this.getConfig().getString("announcer-prefix")
                                        + " " + CreepinoUtils.this.getConfig().getString("announcer-message")));

                    }
                }, 0L, getConfig().getInt("announcer-interval") * 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (PluginModule m : getAllPluginModules()) {
			m.onEnable();
        }
    }

    public static List<Field> getFieldsOfType(Class<?> target, Class<?> searchType) {

        Field[] fields = target.getDeclaredFields();

        List<Field> results = new LinkedList<Field>();
        for (Field f : fields) {
            if (f.getType().equals(searchType)) {
                results.add(f);
            }
        }
        return results;
    }

    /**
     * Returns a list of all PluginModules contained in each plugin class.
     *
     * @see PluginModule
     */
    public static List<PluginModule> getAllPluginModules() {
        List<PluginModule> list = new ArrayList<PluginModule>();
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            if (pl instanceof JavaPlugin) {
                for (Field field : getFieldsOfType(pl.getClass(), PluginModule.class)) {
                    try {
                        if (field.get(pl) instanceof PluginModule) {
                            field.setAccessible(true);
                            list.add((PluginModule) field.get(pl));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return list;
    }

}
