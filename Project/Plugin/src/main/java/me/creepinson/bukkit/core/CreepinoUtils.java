package me.creepinson.bukkit.core;

import com.ezetech.util.JavaClassFinder;


import me.creepinson.util.PluginModule;
import me.creepinson.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreepinoUtils extends JavaPlugin {
    private static CreepinoUtils plugin;
    private static List<PluginModule> modules;

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();

        for (PluginModule m : modules) {
            m.onDisable();
        }
    }

    public static CreepinoUtils getInstance() {
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
        if (this.cfile.exists()) {
            this.config = YamlConfiguration.loadConfiguration(this.cfile);
        }

        modules = getAllPluginModules(this);
        this.getCommand("announce").setExecutor(new CommandAnnounce());
        try {
            if (this.getConfig().getBoolean("announcer-enabled")) {
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

                    public void run() {

                        Bukkit.getServer()
                                .broadcastMessage(TextUtils.color(CreepinoUtils.this.getConfig().getString("announcer-prefix")
                                        + " " + CreepinoUtils.this.getConfig().getString("announcer-message")));

                    }
                }, 0L, getConfig().getInt("announcer-interval") * 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (PluginModule m : modules) {
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
     * Returns a list of instantiated classes that implement PluginModule.
     * This method instantiates each class and adds it to the returned list.
     * A good way of using is to create a list to store this in,
     * so that you aren't recreating each plugin module every time.
     *
     * @see PluginModule
     */
    public static List<PluginModule> getAllPluginModules(JavaPlugin owningPlugin) {
        List<PluginModule> list = new ArrayList<>();
        try {
            JavaClassFinder classFinder = new JavaClassFinder();
            List<Class<? extends PluginModule>> classes = classFinder.findAllMatchingTypes(PluginModule.class);
            for (Class<? extends PluginModule> clazz : classes) {
                PluginModule object = clazz.getConstructor(JavaPlugin.class).newInstance(owningPlugin);
                list.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
