package mrbysco.forcecraft.config;


import mrbysco.forcecraft.ForceCraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class ConfigHandler {
    public static class Common {
        public final BooleanValue generateForceOre;
        public final BooleanValue generateForceTree;
        public final BooleanValue timeTorchEnabled;
        public final BooleanValue timeTorchLogging;
        public final IntValue baconatorMaxStacks;
        public final ConfigValue<List<? extends String>> furnaceOutputBlacklist;

        public final BooleanValue ChuChuSpawning;
        public final IntValue ChuChuWeight;
        public final IntValue ChuChuMinGroup;
        public final IntValue ChuChuMaxGroup;

        public final BooleanValue FairySpawning;
        public final IntValue FairyWeight;
        public final IntValue FairyMinGroup;
        public final IntValue FairyMaxGroup;


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

            baconatorMaxStacks = builder
                    .comment("The max amount of food stacks stored in the baconator [Default: 4]")
                    .defineInRange("baconatorMaxStacks", 4, 1, Integer.MAX_VALUE);

            furnaceOutputBlacklist = builder
                    .comment("An additional list of tile entities the Force Furnace can NOT insert into [Syntax: modid:tile_name]\n" +
                            "Examples: \"minecraft:shulker_box\" would it stop inserting into shulkers")
                    .defineList("furnaceOutputBlacklist", Arrays.asList(new String[] {"minecraft:hopper"}), o -> (o instanceof String));

            builder.pop();
            builder.comment("General settings")
                    .push("general");

            ChuChuSpawning = builder
                    .comment("Enable Chu Chu's to spawn [Default: 1]")
                    .define("ChuChuSpawning", true);

            ChuChuWeight = builder
                    .comment("Chu Chu spawn weight [Default: 100]")
                    .defineInRange("ChuChuWeight", 100, 1, Integer.MAX_VALUE);

            ChuChuMinGroup = builder
                    .comment("Chu Chu Min Group size [Default: 1]")
                    .defineInRange("ChuChuMinGroup", 1, 1, Integer.MAX_VALUE);

            ChuChuMaxGroup = builder
                    .comment("Chu Chu Max Group size [Default: 1]")
                    .defineInRange("ChuChuMaxGroup", 1, 1, Integer.MAX_VALUE);

            FairySpawning = builder
                    .comment("Enable Fairy's to spawn [Default: 1]")
                    .define("FairySpawning", true);

            FairyWeight = builder
                    .comment("Fairy spawn weight [Default: 4]")
                    .defineInRange("FairyWeight", 4, 1, Integer.MAX_VALUE);

            FairyMinGroup = builder
                    .comment("Fairy Min Group size [Default: 1]")
                    .defineInRange("FairyMinGroup", 1, 1, Integer.MAX_VALUE);

            FairyMaxGroup = builder
                    .comment("Fairy Max Group size [Default: 1]")
                    .defineInRange("FairyMaxGroup", 2, 1, Integer.MAX_VALUE);

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


