package com.cursee.automessage;

import com.cursee.automessage.core.message.util.MessageServiceUtil;
import com.cursee.automessage.platform.Services;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraft.resources.ResourceLocation;

import java.io.File;

public class AutoMessage {

    public static void init() {
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);

        String dataPath = Services.PLATFORM.getGameDirectory() + File.separator + "automessage_data";
        File dataDirectory = new File(dataPath);
        if (!dataDirectory.isDirectory()) dataDirectory.mkdirs();
        File checkFile = new File(dataPath + File.separator + "checkfile.txt");

        if (!checkFile.exists()) {
            String configPath = Services.PLATFORM.getGameDirectory() + File.separator + "config";
            File configDirectory = new File(configPath);
            if (!configDirectory.isDirectory()) configDirectory.mkdirs();

            File readmeFile = new File(configPath + File.separator + Constants.MOD_ID + "_README.txt");
            MessageServiceUtil.copyResourceToFile("/assets/automessage/automessage_README.txt", readmeFile);
            MessageServiceUtil.copyResourceToFile("/assets/automessage/checkfile.txt", checkFile);
        }
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}