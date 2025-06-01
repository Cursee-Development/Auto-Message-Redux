package com.cursee.automessage.core.message.util;

import com.cursee.automessage.Constants;
import com.cursee.automessage.core.message.ClientMessageService;
import com.cursee.automessage.core.message.ServerMessageService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.ResourceLocationException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MessageServiceUtil {

    public static void copyResourceToFile(String resource, File destination) {
        try (InputStream input = ClientMessageService.class.getResourceAsStream(resource)) {
            if (input == null) throw new ResourceLocationException("Invalid resource path: " + resource);

            destination.getParentFile().mkdirs();

            try (FileOutputStream output = new FileOutputStream(destination)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                Constants.LOG.info("Copied resource to: {}", destination.getAbsolutePath());
            }
        }
        catch (IOException e) {
            Constants.LOG.info("Failed to write {} to file: {}", resource, destination.getPath());
            Constants.LOG.info(e.toString());
            e.fillInStackTrace();
        }
    }

    public static void loadFallbackMessages(Gson gson) {
        try (InputStream in = ClientMessageService.class.getResourceAsStream("/assets/automessage/default_messages.json")) {
            if (in != null) {
                try (InputStreamReader reader = new InputStreamReader(in)) {
                    Type listType = new TypeToken<List<Message>>() {}.getType();
                    List<Message> fallback = gson.fromJson(reader, listType);

                    ClientMessageService.instance.ON_FIRST_JOIN_MESSAGES.addAll(fallback);
                    ServerMessageService.instance.ON_FIRST_JOIN_MESSAGES.addAll(fallback);
//                    if (isClientSide) MessageService.instance.clientMessages.addAll(fallback);
//                    else MessageService.instance.serverMessages.addAll(fallback);

                    Constants.LOG.info("Loaded fallback messages from mod resources.");
                    return;
                }
            }
        }
        catch (Exception e) {
            Constants.LOG.info("Failed to load fallback message resource.");
            Constants.LOG.info(e.toString());
            e.fillInStackTrace();
        }

        Message fallbackMessage = Message.defaultFallback();

        ClientMessageService.instance.ON_FIRST_JOIN_MESSAGES.add(fallbackMessage);
        ServerMessageService.instance.ON_FIRST_JOIN_MESSAGES.add(fallbackMessage);
//        if (isClientSide) MessageService.instance.clientMessages.add(fallbackMessage);
//        else MessageService.instance.serverMessages.add(fallbackMessage);

        Constants.LOG.info("Loaded hard-coded fallback message.");
    }

    public static ArrayList<File> resolveMessagePaths(String pattern) {
        ArrayList<File> results = new ArrayList<>();

        Path configDir = new File("config").toPath();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);

        try (Stream<Path> pathStream = Files.walk(configDir)) {
            pathStream.filter(path -> matcher.matches(configDir.relativize(path)))
                    .forEach(path -> results.add(path.toFile()));
        }
        catch (IOException e) {
            Constants.LOG.info("Failed to resolve glob : {}", pattern);
            Constants.LOG.info(e.toString());
            e.fillInStackTrace();
        }

        return results;
    }

    public static boolean areAllListsEmpty(List<List<?>> lists) {
        if (lists == null) {
            return true; // or throw an exception if null is not allowed
        }
        for (List<?> list : lists) {
            if (list != null && !list.isEmpty()) {
                return false; // If any list is not empty, return false
            }
        }
        return true; // If all lists are empty or null, return true
    }
}
