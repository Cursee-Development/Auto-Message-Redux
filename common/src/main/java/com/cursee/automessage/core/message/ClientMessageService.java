package com.cursee.automessage.core.message;

import com.cursee.automessage.Constants;
import com.cursee.automessage.core.message.util.Message;
import com.cursee.automessage.core.message.util.MessageSchedule;
import com.cursee.automessage.core.message.util.MessageServiceUtil;
import com.cursee.automessage.platform.Services;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientMessageService {
    
    public static ClientMessageService instance;

    public final Map<MessageSchedule, LinkedList<Message>> MESSAGES_FROM_SCHEDULE_MAP = new HashMap<>();
    public final LinkedList<Message> ON_FIRST_JOIN_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> ON_JOIN_LEVEL_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> ON_DEATH_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> ON_RESPAWN_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> ON_SLEEP_IN_BED_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> ON_WAKE_UP_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> OVERWORLD_TO_NETHER_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> OVERWORLD_TO_END_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> NETHER_TO_OVERWORLD_MESSAGES = new LinkedList<>();
    public final LinkedList<Message> END_TO_OVERWORLD_MESSAGES = new LinkedList<>();

    public GeneralMSConfig general = new GeneralMSConfig();
    public LinkedList<String> message_json_files = new LinkedList<>();

    public static void load() {

        long start = System.nanoTime();
        // instance = new MessageService();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String configDirPath = Services.PLATFORM.getGameDirectory() + File.separator + "config";
        File configDir = new File(configDirPath);
        if (!configDir.isDirectory()) configDir.mkdirs();
        
        File configFile = new File(configDirPath + File.separator + Constants.MOD_ID + "-client" + ".json");
        
        if (!configFile.isFile()) {

            Constants.LOG.info("[CLIENT] No config file found. Creating and reloading.");

            MessageServiceUtil.copyResourceToFile("/assets/automessage/client_default.json", configFile);

            File configMessagesFile = new File(configDirPath + File.separator + "messages" + File.separator + "client_messages.json");
            MessageServiceUtil.copyResourceToFile("/assets/automessage/client_messages.json", configMessagesFile);

            load();
            return;
        }

        if (configFile.isFile()) {
            try (FileReader reader = new FileReader(configFile)) {
                if (instance == null || !Services.PLATFORM.isClientSide()) {
                    instance = gson.fromJson(reader, ClientMessageService.class);
                }
                instance.initMaps();
            }
            catch (IOException e) {
                Constants.LOG.info("[CLIENT] Failed to read: {}", configFile.getName());
                Constants.LOG.info(e.toString());
                Constants.LOG.info("[CLIENT] Using default configuration messages.");
                e.fillInStackTrace();
            }

            if (instance.message_json_files == null || instance.message_json_files.isEmpty()) {
                Constants.LOG.info("[CLIENT] No message files listed in configuration: {}", configFile.getName());
                Constants.LOG.info("[CLIENT] Loading fallback configuration messages.");
                MessageServiceUtil.loadFallbackMessages(gson);
                return;
            }

            for (String pattern : instance.message_json_files) {
                List<File> filesToLoad = pattern.contains("*") ? MessageServiceUtil.resolveMessagePaths(pattern) : List.of(new File("config", pattern));

                AtomicInteger atomicInteger = new AtomicInteger(0);
                for (File file : filesToLoad) {
                    if (file.exists()) {
                        try (FileReader msgReader = new FileReader(file)) {

                            Type listType = new TypeToken<List<Message>>() {}.getType();

                            List<Message> loaded = gson.fromJson(msgReader, listType);

                            loaded.forEach(message -> {

                                // must be replaced
                                if (message.text.contains("§")) message.text = message.text.replaceAll("§", "\u00A7");

                                instance.MESSAGES_FROM_SCHEDULE_MAP.get(message.schedule).add(message);
                                atomicInteger.incrementAndGet();
                            });
                        }
                        catch (Exception e) {
                            Constants.LOG.info("[CLIENT] Failed to load message file: {}", file.getPath());
                            Constants.LOG.info(e.toString());
                            e.fillInStackTrace();
                        }
                    }
                    else {
                        Constants.LOG.info("[CLIENT] Message file not found: {}", file.getPath());
                    }
                }

                Constants.LOG.info("[CLIENT] MessageService loaded {} messages", atomicInteger.get());
            }

            List<List<?>> lists = instance.allMessageLists();

//            if (MessageServiceUtil.areAllListsEmpty(lists)) {
//                Constants.LOG.info("[CLIENT] No messages loaded from files. Falling back to defaults.");
//                MessageServiceUtil.loadFallbackMessages(gson);
//            }
        }

        long end = System.nanoTime();
        long time = end - start;
        Constants.LOG.info("[CLIENT] MessageService loaded on client in {}ns or {}ms or {}s", time, time / 1_000_000L, (float) time / (1_000_000.0f * 1_000.0f));
    }
    
    public void initMaps() {
        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.ON_FIRST_JOIN, ON_FIRST_JOIN_MESSAGES);
        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.ON_JOIN_LEVEL, ON_JOIN_LEVEL_MESSAGES);
        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.ON_DEATH, ON_DEATH_MESSAGES);
        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.ON_RESPAWN, ON_RESPAWN_MESSAGES);
//        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.ON_SLEEP_IN_BED, ON_SLEEP_IN_BED_MESSAGES);
//        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.ON_WAKE_UP, ON_WAKE_UP_MESSAGES);
//        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.OVERWORLD_TO_NETHER, OVERWORLD_TO_NETHER_MESSAGES);
//        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.OVERWORLD_TO_END, OVERWORLD_TO_END_MESSAGES);
//        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.NETHER_TO_OVERWORLD, NETHER_TO_OVERWORLD_MESSAGES);
//        MESSAGES_FROM_SCHEDULE_MAP.put(MessageSchedule.END_TO_OVERWORLD, END_TO_OVERWORLD_MESSAGES);
    }

    public List<List<?>> allMessageLists() {

        return List.of(
                ON_FIRST_JOIN_MESSAGES,
                ON_JOIN_LEVEL_MESSAGES,
                ON_DEATH_MESSAGES,
                ON_RESPAWN_MESSAGES,
                ON_SLEEP_IN_BED_MESSAGES,
                ON_WAKE_UP_MESSAGES,
                OVERWORLD_TO_NETHER_MESSAGES,
                OVERWORLD_TO_END_MESSAGES,
                NETHER_TO_OVERWORLD_MESSAGES,
                END_TO_OVERWORLD_MESSAGES
        );
    }
}
