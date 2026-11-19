package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ForceBlockTagProvider extends BlockTagsProvider {
	public ForceBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Reference.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ForceRegistry.INFUSER.get(), ForceRegistry.POWER_ORE.get(),
				ForceRegistry.DEEPSLATE_POWER_ORE.get(), ForceRegistry.FORCE_BRICK_RED.get(), ForceRegistry.FORCE_BRICK_YELLOW.get(),
				ForceRegistry.FORCE_BRICK_GREEN.get(), ForceRegistry.FORCE_BRICK_BLUE.get(), ForceRegistry.FORCE_BRICK_WHITE.get(), ForceRegistry.FORCE_BRICK_BLACK.get(),
				ForceRegistry.FORCE_BRICK_BROWN.get(), ForceRegistry.FORCE_BRICK_ORANGE.get(), ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get(), ForceRegistry.FORCE_BRICK_MAGENTA.get(),
				ForceRegistry.FORCE_BRICK_PINK.get(), ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get(), ForceRegistry.FORCE_BRICK_LIME.get(), ForceRegistry.FORCE_BRICK_CYAN.get(),
				ForceRegistry.FORCE_BRICK_PURPLE.get(), ForceRegistry.FORCE_BRICK_GRAY.get(), ForceRegistry.FORCE_BRICK.get(),
				ForceRegistry.FORCE_BRICK_RED_STAIRS.get(), ForceRegistry.FORCE_BRICK_YELLOW_STAIRS.get(), ForceRegistry.FORCE_BRICK_GREEN_STAIRS.get(), ForceRegistry.FORCE_BRICK_BLUE_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_WHITE_STAIRS.get(), ForceRegistry.FORCE_BRICK_BLACK_STAIRS.get(), ForceRegistry.FORCE_BRICK_BROWN_STAIRS.get(), ForceRegistry.FORCE_BRICK_ORANGE_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS.get(), ForceRegistry.FORCE_BRICK_PINK_STAIRS.get(), ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_LIME_STAIRS.get(), ForceRegistry.FORCE_BRICK_CYAN_STAIRS.get(), ForceRegistry.FORCE_BRICK_PURPLE_STAIRS.get(), ForceRegistry.FORCE_BRICK_GRAY_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_STAIRS.get(), ForceRegistry.FORCE_BRICK_RED_SLAB.get(), ForceRegistry.FORCE_BRICK_YELLOW_SLAB.get(), ForceRegistry.FORCE_BRICK_GREEN_SLAB.get(),
				ForceRegistry.FORCE_BRICK_BLUE_SLAB.get(), ForceRegistry.FORCE_BRICK_WHITE_SLAB.get(), ForceRegistry.FORCE_BRICK_BLACK_SLAB.get(), ForceRegistry.FORCE_BRICK_BROWN_SLAB.get(),
				ForceRegistry.FORCE_BRICK_ORANGE_SLAB.get(), ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB.get(), ForceRegistry.FORCE_BRICK_MAGENTA_SLAB.get(), ForceRegistry.FORCE_BRICK_PINK_SLAB.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB.get(), ForceRegistry.FORCE_BRICK_LIME_SLAB.get(), ForceRegistry.FORCE_BRICK_CYAN_SLAB.get(), ForceRegistry.FORCE_BRICK_PURPLE_SLAB.get(),
				ForceRegistry.FORCE_BRICK_GRAY_SLAB.get(), ForceRegistry.FORCE_BRICK_SLAB.get(), ForceRegistry.FORCE_FURNACE.get(), ForceRegistry.BLACK_FORCE_FURNACE.get(),
				ForceRegistry.BLUE_FORCE_FURNACE.get(), ForceRegistry.BROWN_FORCE_FURNACE.get(), ForceRegistry.CYAN_FORCE_FURNACE.get(), ForceRegistry.GRAY_FORCE_FURNACE.get(),
				ForceRegistry.GREEN_FORCE_FURNACE.get(), ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get(), ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get(),
				ForceRegistry.LIME_FORCE_FURNACE.get(), ForceRegistry.MAGENTA_FORCE_FURNACE.get(), ForceRegistry.ORANGE_FORCE_FURNACE.get(), ForceRegistry.PINK_FORCE_FURNACE.get(),
				ForceRegistry.PURPLE_FORCE_FURNACE.get(), ForceRegistry.RED_FORCE_FURNACE.get(), ForceRegistry.WHITE_FORCE_FURNACE.get(), ForceRegistry.FORCE_ENGINE.get()
		);

		this.tag(ForceTags.Blocks.INCORRECT_FOR_FORCE).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ForceRegistry.FORCE_BRICK_RED.get(), ForceRegistry.FORCE_BRICK_YELLOW.get(),
				ForceRegistry.FORCE_BRICK_GREEN.get(), ForceRegistry.FORCE_BRICK_BLUE.get(),
				ForceRegistry.FORCE_BRICK_WHITE.get(), ForceRegistry.FORCE_BRICK_BLACK.get(),
				ForceRegistry.FORCE_BRICK_BROWN.get(), ForceRegistry.FORCE_BRICK_ORANGE.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get(), ForceRegistry.FORCE_BRICK_MAGENTA.get(),
				ForceRegistry.FORCE_BRICK_PINK.get(), ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get(),
				ForceRegistry.FORCE_BRICK_LIME.get(), ForceRegistry.FORCE_BRICK_CYAN.get(),
				ForceRegistry.FORCE_BRICK_PURPLE.get(), ForceRegistry.FORCE_BRICK_GRAY.get(), ForceRegistry.FORCE_BRICK.get(),
				ForceRegistry.FORCE_BRICK_RED_STAIRS.get(), ForceRegistry.FORCE_BRICK_YELLOW_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_GREEN_STAIRS.get(), ForceRegistry.FORCE_BRICK_BLUE_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_WHITE_STAIRS.get(), ForceRegistry.FORCE_BRICK_BLACK_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_BROWN_STAIRS.get(), ForceRegistry.FORCE_BRICK_ORANGE_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_PINK_STAIRS.get(), ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_LIME_STAIRS.get(), ForceRegistry.FORCE_BRICK_CYAN_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_PURPLE_STAIRS.get(), ForceRegistry.FORCE_BRICK_GRAY_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_STAIRS.get(), ForceRegistry.FORCE_BRICK_RED_SLAB.get(),
				ForceRegistry.FORCE_BRICK_YELLOW_SLAB.get(), ForceRegistry.FORCE_BRICK_GREEN_SLAB.get(),
				ForceRegistry.FORCE_BRICK_BLUE_SLAB.get(), ForceRegistry.FORCE_BRICK_WHITE_SLAB.get(),
				ForceRegistry.FORCE_BRICK_BLACK_SLAB.get(), ForceRegistry.FORCE_BRICK_BROWN_SLAB.get(),
				ForceRegistry.FORCE_BRICK_ORANGE_SLAB.get(), ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB.get(),
				ForceRegistry.FORCE_BRICK_MAGENTA_SLAB.get(), ForceRegistry.FORCE_BRICK_PINK_SLAB.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB.get(), ForceRegistry.FORCE_BRICK_LIME_SLAB.get(),
				ForceRegistry.FORCE_BRICK_CYAN_SLAB.get(), ForceRegistry.FORCE_BRICK_PURPLE_SLAB.get(),
				ForceRegistry.FORCE_BRICK_GRAY_SLAB.get(), ForceRegistry.FORCE_BRICK_SLAB.get()
		);
		this.tag(ForceTags.Blocks.TICKING_NOT_SUPPORTED).add(
				ForceRegistry.TIME_TORCH.value(),
				ForceRegistry.WALL_TIME_TORCH.value()
		);
		this.tag(ForceTags.Blocks.COLD_EDIBLE_GRASS_BLOCKS).add(Blocks.GRASS_BLOCK, Blocks.PODZOL);
		this.tag(ForceTags.Blocks.ENDERTOT_HOLDABLE).addTags(BlockTags.STAIRS, BlockTags.SLABS);
		this.tag(ForceTags.Blocks.MINEABLE_WITH_MITTS).addTags(
				BlockTags.MINEABLE_WITH_SHOVEL,
				BlockTags.MINEABLE_WITH_AXE,
				BlockTags.MINEABLE_WITH_PICKAXE
		).add(Blocks.COBWEB);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(
				ForceRegistry.POWER_ORE.get(),
				ForceRegistry.DEEPSLATE_POWER_ORE.get()
		);

		this.tag(ForceTags.Blocks.FORCE_TORCHES).add(ForceRegistry.FORCE_TORCH.get(), ForceRegistry.FORCE_RED_TORCH.get(),
				ForceRegistry.FORCE_ORANGE_TORCH.get(), ForceRegistry.FORCE_GREEN_TORCH.get(),
				ForceRegistry.FORCE_BLUE_TORCH.get(), ForceRegistry.FORCE_WHITE_TORCH.get(),
				ForceRegistry.FORCE_BLACK_TORCH.get(), ForceRegistry.FORCE_BROWN_TORCH.get(),
				ForceRegistry.FORCE_LIGHT_BLUE_TORCH.get(), ForceRegistry.FORCE_MAGENTA_TORCH.get(),
				ForceRegistry.FORCE_PINK_TORCH.get(), ForceRegistry.FORCE_LIGHT_GRAY_TORCH.get(),
				ForceRegistry.FORCE_LIME_TORCH.get(), ForceRegistry.FORCE_CYAN_TORCH.get(),
				ForceRegistry.FORCE_PURPLE_TORCH.get(), ForceRegistry.FORCE_GRAY_TORCH.get()
		);

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
				ForceRegistry.FORCE_LOG.get(),
				ForceRegistry.FORCE_WOOD.get(),
				ForceRegistry.FORCE_PLANKS.get(),
				ForceRegistry.FORCE_PLANK_SLAB.get(),
				ForceRegistry.FORCE_PLANK_STAIRS.get()
		);
		this.tag(BlockTags.MINEABLE_WITH_HOE).add(ForceRegistry.FORCE_LEAVES.get());

		this.tag(ForceTags.Blocks.FORCE_LOGS).add(ForceRegistry.FORCE_LOG.get(), ForceRegistry.FORCE_WOOD.get());
		this.tag(BlockTags.LOGS_THAT_BURN).addTag(ForceTags.Blocks.FORCE_LOGS);
		this.tag(BlockTags.LEAVES).add(ForceRegistry.FORCE_LEAVES.get());
		this.tag(BlockTags.SAPLINGS).add(ForceRegistry.FORCE_SAPLING.get());
		this.tag(BlockTags.PLANKS).add(ForceRegistry.FORCE_PLANKS.get());

		this.tag(ForceTags.Blocks.FORCE_FURNACES).add(
				ForceRegistry.FORCE_FURNACE.get(), ForceRegistry.BLACK_FORCE_FURNACE.get(),
				ForceRegistry.BLUE_FORCE_FURNACE.get(), ForceRegistry.BROWN_FORCE_FURNACE.get(),
				ForceRegistry.CYAN_FORCE_FURNACE.get(), ForceRegistry.GRAY_FORCE_FURNACE.get(),
				ForceRegistry.GREEN_FORCE_FURNACE.get(), ForceRegistry.LIGHT_BLUE_FORCE_FURNACE.get(),
				ForceRegistry.LIGHT_GRAY_FORCE_FURNACE.get(), ForceRegistry.LIME_FORCE_FURNACE.get(),
				ForceRegistry.MAGENTA_FORCE_FURNACE.get(), ForceRegistry.ORANGE_FORCE_FURNACE.get(),
				ForceRegistry.PINK_FORCE_FURNACE.get(), ForceRegistry.PURPLE_FORCE_FURNACE.get(),
				ForceRegistry.RED_FORCE_FURNACE.get(), ForceRegistry.WHITE_FORCE_FURNACE.get()
		);

		this.tag(BlockTags.STAIRS).add(ForceRegistry.FORCE_BRICK_RED_STAIRS.get(), ForceRegistry.FORCE_BRICK_YELLOW_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_GREEN_STAIRS.get(), ForceRegistry.FORCE_BRICK_BLUE_STAIRS.get(), ForceRegistry.FORCE_BRICK_WHITE_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_BLACK_STAIRS.get(), ForceRegistry.FORCE_BRICK_BROWN_STAIRS.get(), ForceRegistry.FORCE_BRICK_ORANGE_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS.get(), ForceRegistry.FORCE_BRICK_PINK_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), ForceRegistry.FORCE_BRICK_LIME_STAIRS.get(), ForceRegistry.FORCE_BRICK_CYAN_STAIRS.get(),
				ForceRegistry.FORCE_BRICK_PURPLE_STAIRS.get(), ForceRegistry.FORCE_BRICK_GRAY_STAIRS.get(), ForceRegistry.FORCE_BRICK_STAIRS.get());
		this.tag(BlockTags.SLABS).add(ForceRegistry.FORCE_BRICK_RED_SLAB.get(), ForceRegistry.FORCE_BRICK_YELLOW_SLAB.get(),
				ForceRegistry.FORCE_BRICK_GREEN_SLAB.get(), ForceRegistry.FORCE_BRICK_BLUE_SLAB.get(), ForceRegistry.FORCE_BRICK_WHITE_SLAB.get(),
				ForceRegistry.FORCE_BRICK_BLACK_SLAB.get(), ForceRegistry.FORCE_BRICK_BROWN_SLAB.get(), ForceRegistry.FORCE_BRICK_ORANGE_SLAB.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB.get(), ForceRegistry.FORCE_BRICK_MAGENTA_SLAB.get(), ForceRegistry.FORCE_BRICK_PINK_SLAB.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB.get(), ForceRegistry.FORCE_BRICK_LIME_SLAB.get(), ForceRegistry.FORCE_BRICK_CYAN_SLAB.get(),
				ForceRegistry.FORCE_BRICK_PURPLE_SLAB.get(), ForceRegistry.FORCE_BRICK_GRAY_SLAB.get(), ForceRegistry.FORCE_BRICK_SLAB.get());

		this.tag(ForceTags.Blocks.FORCE_BRICKS).add(ForceRegistry.FORCE_BRICK_RED.get(), ForceRegistry.FORCE_BRICK_YELLOW.get(),
				ForceRegistry.FORCE_BRICK_GREEN.get(), ForceRegistry.FORCE_BRICK_BLUE.get(), ForceRegistry.FORCE_BRICK_WHITE.get(),
				ForceRegistry.FORCE_BRICK_BLACK.get(), ForceRegistry.FORCE_BRICK_BROWN.get(), ForceRegistry.FORCE_BRICK_ORANGE.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_BLUE.get(), ForceRegistry.FORCE_BRICK_MAGENTA.get(), ForceRegistry.FORCE_BRICK_PINK.get(),
				ForceRegistry.FORCE_BRICK_LIGHT_GRAY.get(), ForceRegistry.FORCE_BRICK_LIME.get(), ForceRegistry.FORCE_BRICK_CYAN.get(),
				ForceRegistry.FORCE_BRICK_PURPLE.get(), ForceRegistry.FORCE_BRICK_GRAY.get(), ForceRegistry.FORCE_BRICK_STAIRS.get());

		this.tag(BlockTags.WOODEN_SLABS).add(ForceRegistry.FORCE_PLANK_SLAB.get());
		this.tag(BlockTags.WOODEN_STAIRS).add(ForceRegistry.FORCE_PLANK_STAIRS.get());

		this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(ForceRegistry.POWER_ORE.get());
		this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(ForceRegistry.DEEPSLATE_POWER_ORE.get());
		this.tag(ForceTags.Blocks.ORES_POWER).add(ForceRegistry.POWER_ORE.get(), ForceRegistry.DEEPSLATE_POWER_ORE.get());
		this.tag(Tags.Blocks.ORES).addTag(ForceTags.Blocks.ORES_POWER);

	}
}