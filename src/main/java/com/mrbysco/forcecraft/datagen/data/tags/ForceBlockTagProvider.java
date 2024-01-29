package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.mrbysco.forcecraft.registry.ForceRegistry.BLACK_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.BLUE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.BROWN_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.CYAN_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.DEEPSLATE_POWER_ORE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_ENGINE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.GRAY_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.GREEN_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.INFUSER;
import static com.mrbysco.forcecraft.registry.ForceRegistry.LIGHT_BLUE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.LIGHT_GRAY_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.LIME_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.MAGENTA_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.ORANGE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.PINK_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.PURPLE_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.RED_FORCE_FURNACE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.WHITE_FORCE_FURNACE;

public class ForceBlockTagProvider extends BlockTagsProvider {
	public ForceBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Reference.MOD_ID, existingFileHelper);
	}

	public static final TagKey<Block> ORES_IN_GROUND_DEEPSLATE = forgeTag("ores_in_ground/deepslate");
	public static final TagKey<Block> ORES_IN_GROUND_STONE = forgeTag("ores_in_ground/stone");
	public static final TagKey<Block> ORES = forgeTag("ores");
	public static final TagKey<Block> ORES_POWER = forgeTag("ores/power");

	private static TagKey<Block> forgeTag(String name) {
		return BlockTags.create(new ResourceLocation("forge", name));
	}

	private static TagKey<Block> optionalTag(String modid, String name) {
		return BlockTags.create(new ResourceLocation(modid, name));
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(INFUSER.get(), POWER_ORE.get(), DEEPSLATE_POWER_ORE.get(), FORCE_BRICK_RED.get(), FORCE_BRICK_YELLOW.get(),
				FORCE_BRICK_GREEN.get(), FORCE_BRICK_BLUE.get(), FORCE_BRICK_WHITE.get(), FORCE_BRICK_BLACK.get(),
				FORCE_BRICK_BROWN.get(), FORCE_BRICK_ORANGE.get(), FORCE_BRICK_LIGHT_BLUE.get(), FORCE_BRICK_MAGENTA.get(),
				FORCE_BRICK_PINK.get(), FORCE_BRICK_LIGHT_GRAY.get(), FORCE_BRICK_LIME.get(), FORCE_BRICK_CYAN.get(),
				FORCE_BRICK_PURPLE.get(), FORCE_BRICK_GRAY.get(), FORCE_BRICK.get(),
				FORCE_BRICK_RED_STAIRS.get(), FORCE_BRICK_YELLOW_STAIRS.get(), FORCE_BRICK_GREEN_STAIRS.get(), FORCE_BRICK_BLUE_STAIRS.get(),
				FORCE_BRICK_WHITE_STAIRS.get(), FORCE_BRICK_BLACK_STAIRS.get(), FORCE_BRICK_BROWN_STAIRS.get(), FORCE_BRICK_ORANGE_STAIRS.get(),
				FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), FORCE_BRICK_MAGENTA_STAIRS.get(), FORCE_BRICK_PINK_STAIRS.get(), FORCE_BRICK_LIGHT_GRAY_STAIRS.get(),
				FORCE_BRICK_LIME_STAIRS.get(), FORCE_BRICK_CYAN_STAIRS.get(), FORCE_BRICK_PURPLE_STAIRS.get(), FORCE_BRICK_GRAY_STAIRS.get(),
				FORCE_BRICK_STAIRS.get(), FORCE_BRICK_RED_SLAB.get(), FORCE_BRICK_YELLOW_SLAB.get(), FORCE_BRICK_GREEN_SLAB.get(),
				FORCE_BRICK_BLUE_SLAB.get(), FORCE_BRICK_WHITE_SLAB.get(), FORCE_BRICK_BLACK_SLAB.get(), FORCE_BRICK_BROWN_SLAB.get(),
				FORCE_BRICK_ORANGE_SLAB.get(), FORCE_BRICK_LIGHT_BLUE_SLAB.get(), FORCE_BRICK_MAGENTA_SLAB.get(), FORCE_BRICK_PINK_SLAB.get(),
				FORCE_BRICK_LIGHT_GRAY_SLAB.get(), FORCE_BRICK_LIME_SLAB.get(), FORCE_BRICK_CYAN_SLAB.get(), FORCE_BRICK_PURPLE_SLAB.get(),
				FORCE_BRICK_GRAY_SLAB.get(), FORCE_BRICK_SLAB.get(), FORCE_FURNACE.get(), BLACK_FORCE_FURNACE.get(),
				BLUE_FORCE_FURNACE.get(), BROWN_FORCE_FURNACE.get(), CYAN_FORCE_FURNACE.get(), GRAY_FORCE_FURNACE.get(),
				GREEN_FORCE_FURNACE.get(), LIGHT_BLUE_FORCE_FURNACE.get(), LIGHT_GRAY_FORCE_FURNACE.get(),
				LIME_FORCE_FURNACE.get(), MAGENTA_FORCE_FURNACE.get(), ORANGE_FORCE_FURNACE.get(), PINK_FORCE_FURNACE.get(),
				PURPLE_FORCE_FURNACE.get(), RED_FORCE_FURNACE.get(), WHITE_FORCE_FURNACE.get(), FORCE_ENGINE.get()
		);
		this.tag(ForceTags.NEEDS_FORCE_TOOL).add(INFUSER.get(), FORCE_ENGINE.get());
		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(FORCE_BRICK_RED.get(), FORCE_BRICK_YELLOW.get(),
				FORCE_BRICK_GREEN.get(), FORCE_BRICK_BLUE.get(), FORCE_BRICK_WHITE.get(), FORCE_BRICK_BLACK.get(),
				FORCE_BRICK_BROWN.get(), FORCE_BRICK_ORANGE.get(), FORCE_BRICK_LIGHT_BLUE.get(), FORCE_BRICK_MAGENTA.get(),
				FORCE_BRICK_PINK.get(), FORCE_BRICK_LIGHT_GRAY.get(), FORCE_BRICK_LIME.get(), FORCE_BRICK_CYAN.get(),
				FORCE_BRICK_PURPLE.get(), FORCE_BRICK_GRAY.get(), FORCE_BRICK.get(),
				FORCE_BRICK_RED_STAIRS.get(), FORCE_BRICK_YELLOW_STAIRS.get(), FORCE_BRICK_GREEN_STAIRS.get(), FORCE_BRICK_BLUE_STAIRS.get(),
				FORCE_BRICK_WHITE_STAIRS.get(), FORCE_BRICK_BLACK_STAIRS.get(), FORCE_BRICK_BROWN_STAIRS.get(), FORCE_BRICK_ORANGE_STAIRS.get(),
				FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), FORCE_BRICK_MAGENTA_STAIRS.get(), FORCE_BRICK_PINK_STAIRS.get(), FORCE_BRICK_LIGHT_GRAY_STAIRS.get(),
				FORCE_BRICK_LIME_STAIRS.get(), FORCE_BRICK_CYAN_STAIRS.get(), FORCE_BRICK_PURPLE_STAIRS.get(), FORCE_BRICK_GRAY_STAIRS.get(),
				FORCE_BRICK_STAIRS.get(), FORCE_BRICK_RED_SLAB.get(), FORCE_BRICK_YELLOW_SLAB.get(), FORCE_BRICK_GREEN_SLAB.get(),
				FORCE_BRICK_BLUE_SLAB.get(), FORCE_BRICK_WHITE_SLAB.get(), FORCE_BRICK_BLACK_SLAB.get(), FORCE_BRICK_BROWN_SLAB.get(),
				FORCE_BRICK_ORANGE_SLAB.get(), FORCE_BRICK_LIGHT_BLUE_SLAB.get(), FORCE_BRICK_MAGENTA_SLAB.get(), FORCE_BRICK_PINK_SLAB.get(),
				FORCE_BRICK_LIGHT_GRAY_SLAB.get(), FORCE_BRICK_LIME_SLAB.get(), FORCE_BRICK_CYAN_SLAB.get(), FORCE_BRICK_PURPLE_SLAB.get(),
				FORCE_BRICK_GRAY_SLAB.get(), FORCE_BRICK_SLAB.get()
		);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(POWER_ORE.get(), DEEPSLATE_POWER_ORE.get());

		this.tag(ORES_IN_GROUND_DEEPSLATE).add(DEEPSLATE_POWER_ORE.get());
		this.tag(ORES_IN_GROUND_STONE).add(POWER_ORE.get());
		this.tag(ORES_POWER).add(POWER_ORE.get(), DEEPSLATE_POWER_ORE.get());
		this.tag(ORES).addTag(ORES_POWER);

	}
}