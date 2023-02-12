package com.mrbysco.forcecraft.datagen.assets;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.mrbysco.forcecraft.registry.ForceRegistry.DEEPSLATE_POWER_ORE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLACK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_BROWN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_CYAN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_GREEN_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_BLUE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIGHT_GRAY_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_LIME_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_MAGENTA_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_ORANGE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PINK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_PURPLE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_RED_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_WHITE_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_BRICK_YELLOW_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PLANK_SLAB;
import static com.mrbysco.forcecraft.registry.ForceRegistry.FORCE_PLANK_STAIRS;
import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE;

public class ForceBlockStates extends BlockStateProvider {

	public ForceBlockStates(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		stairsBlock((StairBlock) FORCE_PLANK_STAIRS.get(), modLoc("block/force_planks"));
		stairsBlock((StairBlock) FORCE_BRICK_RED_STAIRS.get(), modLoc("block/force_brick_red"));
		stairsBlock((StairBlock) FORCE_BRICK_YELLOW_STAIRS.get(), modLoc("block/force_brick_yellow"));
		stairsBlock((StairBlock) FORCE_BRICK_GREEN_STAIRS.get(), modLoc("block/force_brick_green"));
		stairsBlock((StairBlock) FORCE_BRICK_BLUE_STAIRS.get(), modLoc("block/force_brick_blue"));
		stairsBlock((StairBlock) FORCE_BRICK_WHITE_STAIRS.get(), modLoc("block/force_brick_white"));
		stairsBlock((StairBlock) FORCE_BRICK_BLACK_STAIRS.get(), modLoc("block/force_brick_black"));
		stairsBlock((StairBlock) FORCE_BRICK_BROWN_STAIRS.get(), modLoc("block/force_brick_brown"));
		stairsBlock((StairBlock) FORCE_BRICK_ORANGE_STAIRS.get(), modLoc("block/force_brick_orange"));
		stairsBlock((StairBlock) FORCE_BRICK_LIGHT_BLUE_STAIRS.get(), modLoc("block/force_brick_light_blue"));
		stairsBlock((StairBlock) FORCE_BRICK_MAGENTA_STAIRS.get(), modLoc("block/force_brick_magenta"));
		stairsBlock((StairBlock) FORCE_BRICK_PINK_STAIRS.get(), modLoc("block/force_brick_pink"));
		stairsBlock((StairBlock) FORCE_BRICK_LIGHT_GRAY_STAIRS.get(), modLoc("block/force_brick_light_gray"));
		stairsBlock((StairBlock) FORCE_BRICK_LIME_STAIRS.get(), modLoc("block/force_brick_lime"));
		stairsBlock((StairBlock) FORCE_BRICK_CYAN_STAIRS.get(), modLoc("block/force_brick_cyan"));
		stairsBlock((StairBlock) FORCE_BRICK_PURPLE_STAIRS.get(), modLoc("block/force_brick_purple"));
		stairsBlock((StairBlock) FORCE_BRICK_GRAY_STAIRS.get(), modLoc("block/force_brick_gray"));
		stairsBlock((StairBlock) FORCE_BRICK_STAIRS.get(), modLoc("block/force_brick"));

		slabBlock((SlabBlock) FORCE_PLANK_SLAB.get(), modLoc("block/force_planks"), modLoc("block/force_planks"));
		slabBlock((SlabBlock) FORCE_BRICK_RED_SLAB.get(), modLoc("block/force_brick_red"), modLoc("block/force_brick_red"));
		slabBlock((SlabBlock) FORCE_BRICK_YELLOW_SLAB.get(), modLoc("block/force_brick_yellow"), modLoc("block/force_brick_yellow"));
		slabBlock((SlabBlock) FORCE_BRICK_GREEN_SLAB.get(), modLoc("block/force_brick_green"), modLoc("block/force_brick_green"));
		slabBlock((SlabBlock) FORCE_BRICK_BLUE_SLAB.get(), modLoc("block/force_brick_blue"), modLoc("block/force_brick_blue"));
		slabBlock((SlabBlock) FORCE_BRICK_WHITE_SLAB.get(), modLoc("block/force_brick_white"), modLoc("block/force_brick_white"));
		slabBlock((SlabBlock) FORCE_BRICK_BLACK_SLAB.get(), modLoc("block/force_brick_black"), modLoc("block/force_brick_black"));
		slabBlock((SlabBlock) FORCE_BRICK_BROWN_SLAB.get(), modLoc("block/force_brick_brown"), modLoc("block/force_brick_brown"));
		slabBlock((SlabBlock) FORCE_BRICK_ORANGE_SLAB.get(), modLoc("block/force_brick_orange"), modLoc("block/force_brick_orange"));
		slabBlock((SlabBlock) FORCE_BRICK_LIGHT_BLUE_SLAB.get(), modLoc("block/force_brick_light_blue"), modLoc("block/force_brick_light_blue"));
		slabBlock((SlabBlock) FORCE_BRICK_MAGENTA_SLAB.get(), modLoc("block/force_brick_magenta"), modLoc("block/force_brick_magenta"));
		slabBlock((SlabBlock) FORCE_BRICK_PINK_SLAB.get(), modLoc("block/force_brick_pink"), modLoc("block/force_brick_pink"));
		slabBlock((SlabBlock) FORCE_BRICK_LIGHT_GRAY_SLAB.get(), modLoc("block/force_brick_light_gray"), modLoc("block/force_brick_light_gray"));
		slabBlock((SlabBlock) FORCE_BRICK_LIME_SLAB.get(), modLoc("block/force_brick_lime"), modLoc("block/force_brick_lime"));
		slabBlock((SlabBlock) FORCE_BRICK_CYAN_SLAB.get(), modLoc("block/force_brick_cyan"), modLoc("block/force_brick_cyan"));
		slabBlock((SlabBlock) FORCE_BRICK_PURPLE_SLAB.get(), modLoc("block/force_brick_purple"), modLoc("block/force_brick_purple"));
		slabBlock((SlabBlock) FORCE_BRICK_GRAY_SLAB.get(), modLoc("block/force_brick_gray"), modLoc("block/force_brick_gray"));
		slabBlock((SlabBlock) FORCE_BRICK_SLAB.get(), modLoc("block/force_brick"), modLoc("block/force_brick"));

		//Ores
		simpleBlock(POWER_ORE.get(), models().getExistingFile(modLoc("block/power_ore")));
		simpleBlock(DEEPSLATE_POWER_ORE.get(), models().getExistingFile(modLoc("block/deepslate_power_ore")));
	}
}