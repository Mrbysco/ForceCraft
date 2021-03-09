package mrbysco.forcecraft.config;


import mrbysco.forcecraft.ForceCraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {
    public static class Common {
        public final BooleanValue generateForceOre;
        public final BooleanValue generateForceTree;
        public final BooleanValue timeTorchEnabled;
        public final BooleanValue timeTorchLogging;

        Common(ForgeConfigSpec.Builder builder) {
            //General settings
            builder.comment("General settings")
                    .push("general");

            generateForceOre = builder
                    .comment("Enable Force Ore generation. [Default: true]")
                    .define("generateForceOre", true);

            generateForceTree = builder
                    .comment("Enable Force Tree generation. [Default: true]")
                    .define("generateForceTree", true);

            timeTorchEnabled = builder
                    .comment("Enable Time Torch. [Default: true]")
                    .define("timeTorchEnabled", true);

            timeTorchLogging = builder
                    .comment("Print in Log when Time Torch is placed and by who. [Default: true]")
                    .define("timeTorchLogging", true);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }


    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        ForceCraft.LOGGER.debug("Loaded ForceCraft's config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {
        ForceCraft.LOGGER.fatal("ForceCraft's config just got changed on the file system!");
    }
}


