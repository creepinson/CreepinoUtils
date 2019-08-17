package me.creepinson.bukkit.core;

import me.creepinson.utils.PluginModule;
import org.bukkit.Bukkit;
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
    public final Logger logger = Logger.getLogger("CreepinoUtils");
    public static CreepinoUtils plugin;
    private static List<PluginModule> modules;

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();

		for (PluginModule m : getAllPluginModules()) {
			m.onDisable();
		}
    }

    @Override
    public void onEnable() {

        PluginDescriptionFile pdfFile = this.getDescription();

        plugin = this;
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

    public static File downloadFile(File inPath, String inURL) {
        URL url;
        URLConnection con;
        DataInputStream dis;
        FileOutputStream fos;
        byte[] fileData;
        try {
            url = new URL(inURL); // File Location goes here
            con = url.openConnection(); // open the url connection.
            dis = new DataInputStream(con.getInputStream());
            fileData = new byte[con.getContentLength()];
            for (int q = 0; q < fileData.length; q++) {
                fileData[q] = dis.readByte();
            }
            dis.close(); // close the data input stream
            fos = new FileOutputStream(inPath); // FILE Save Location goes
            // here
            fos.write(fileData); // write out the file we want to save.
            fos.close(); // close the output stream writer
        } catch (Exception m) {
            System.out.println(m);
        }
        return inPath;
    }

}