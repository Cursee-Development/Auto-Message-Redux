package com.cursee.automessage.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonFileHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, List<String>>>() {}.getType();

    public static Map<String, List<String>> read(String path) {
        File file = new File(path);
        if (!file.exists()) return new HashMap<>();

        try (FileReader reader = new FileReader(file)) {
            Map<String, List<String>> data = GSON.fromJson(reader, MAP_TYPE);
            return data != null ? data : new HashMap<>();
        }
        catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static boolean write(String path, Map<String, List<String>> data) {
        File file = new File(path);
        File tempFile = new File(path + ".tmp");

        try (FileWriter writer = new FileWriter(tempFile)) {
            GSON.toJson(data, MAP_TYPE, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addTag(String path, String serverName, String message) {
        Map<String, List<String>> data = read(path);
        data.computeIfAbsent(serverName, k -> new ArrayList<>()).add(message);
        write(path, data);
    }

    public static void removeTag(String path, String serverName, String message) {
        Map<String, List<String>> data = read(path);
        List<String> list = data.get(serverName);
        if (list != null) {
            list.remove(message);
            if (list.isEmpty()) {
                data.remove(serverName);
            }
            write(path, data);
        }
    }

    public static boolean hasTag(String filePath, String serverName, String tag) {
        Map<String, List<String>> data = read(filePath);
        return data.containsKey(serverName) && data.get(serverName).contains(tag);
    }

    public static boolean hasKey(String filePath, String serverName) {
        Map<String, List<String>> data = read(filePath);
        return data.containsKey(serverName);
    }

    public static boolean anyKeyContainsTag(File file, String tag) {
        if (!file.exists()) {
            return false;
        }

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            Map<String, List<String>> data = gson.fromJson(reader, type);

            return data.values().stream().anyMatch(list -> list.contains(tag));
        }
        catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }
}

