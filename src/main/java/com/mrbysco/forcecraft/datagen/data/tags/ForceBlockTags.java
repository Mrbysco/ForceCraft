package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static com.mrbysco.forcecraft.registry.ForceRegistry.*;

public class ForceBlockTags extends BlockTagsProvider {
	public ForceBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, Reference.MOD_ID, existingFileHelper);
	}

	public static final TagKey<Block> RELOCATION_NOT_SUPPORTED = forgeTag("relocation_not_supported");
	public static final TagKey<Block> NON_MOVABLE = optionalTag("create", "non_movable");

	private static TagKey<Block> forgeTag(String name) {
		return BlockTags.create(new ResourceLocation("forge", name));
	}

	private static TagKey<Block> optionalTag(String modid, String name) {
		return BlockTags.create(new ResourceLocation(modid, name));
	}

	@Override
	protected void addTags() {
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
				FORCE_BRICK_GRAY_SLAB.get(), FORCE_BRICK_SLAB.get(), FORCE_ENGINE.get()
		);
		this.tag(ForceTags.NEEDS_FORCE_TOOL).add(INFUSER.get());
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
	}
}