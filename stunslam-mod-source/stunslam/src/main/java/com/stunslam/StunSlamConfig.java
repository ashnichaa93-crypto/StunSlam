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

    /** 0 = instant, 1 to 10 = that many ticks */
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
        INSTANCE.delayTicks = Math.max(0, Math.min(10, INSTANCE.delayTicks));
    }

    public static void save() {
        try {
            Files.writeString(PATH, GSON.toJson(INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
