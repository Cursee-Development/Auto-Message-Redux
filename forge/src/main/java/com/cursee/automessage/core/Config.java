package com.cursee.automessage.core;

import com.cursee.automessage.Constants;
import com.cursee.monolib.platform.Services;
import com.cursee.monolib.util.toml.Toml;
import com.cursee.monolib.util.toml.TomlWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    public static final File CONFIG_DIRECTORY = new File(Services.PLATFORM.getGameDirectory() + File.separator + "config");
    public static final String CONFIG_FILEPATH = CONFIG_DIRECTORY + File.separator + Constants.MOD_ID + ".toml";

    public static boolean enabled = true;
    public static List<String> messages = List.of("Sends after five seconds, only once ever", "Sends after 6 seconds, 5 times every session");
    public static List<String> links = List.of("https://www.google.com", "https://www.yahoo.com");
    public static List<Long> intervals = List.of(5L, 6L);
    public static List<Long> soft_limits = List.of(1L, 5L);
    public static List<Long> hard_limits = List.of(1L, 0L);

    public static final Map<String, Object> defaults = new HashMap<String, Object>();

    public static void initialize() {

        defaults.put("enabled", enabled);
        defaults.put("messages", messages);
        defaults.put("links", links);
        defaults.put("intervals", intervals);
        defaults.put("soft_limits", soft_limits);
        defaults.put("hard_limits", hard_limits);

        if (!CONFIG_DIRECTORY.isDirectory()) {
            CONFIG_DIRECTORY.mkdir();
        }

        final File CONFIG_FILE = new File(CONFIG_FILEPATH);

        if (!CONFIG_FILE.isFile()) {
            try {
                TomlWriter writer = new TomlWriter();
                writer.write(defaults, CONFIG_FILE);
            }
            catch (IOException exception) {
                Constants.LOG.error("Fatal error occurred while attempting to write " + Constants.MOD_ID + ".toml");
                Constants.LOG.error("Did another process delete the config directory during writing?");
                Constants.LOG.error(exception.getMessage());
            }
        }
        else {
            try {
                Toml toml = new Toml().read(CONFIG_FILE);
                enabled = toml.getBoolean("enabled");
                messages = toml.getList("messages", messages);
                links = toml.getList("links", links);
                intervals = toml.getList("intervals", intervals);
                soft_limits = toml.getList("soft_limits", soft_limits);
                hard_limits = toml.getList("hard_limits", hard_limits);
            }
            catch (IllegalStateException exception) {
                Constants.LOG.error("Fatal error occurred while attempting to read " + Constants.MOD_ID + ".toml");
                Constants.LOG.error("Did another process delete the file during reading?");
                Constants.LOG.error(exception.getMessage());
            }
        }

    }
}
