package com.stunslam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class StunSlamConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("stunslam.json");

    public static StunSlamConfig INSTANCE = new StunSlamConfig();

    /** 0 = instant, 10 = ten ticks */
    public int delayTicks = 0;

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                StunSlamConfig c = GSON.fromJson(Files.readString(PATH), StunSlamConfig.class);
                if (c != null) INSTANCE = c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (INSTANCE.delayTicks != 0 && INSTANCE.delayTicks != 10) INSTANCE.delayTicks = 0;
    }

    public static void save() {
        try {
            Files.writeString(PATH, GSON.toJson(INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
