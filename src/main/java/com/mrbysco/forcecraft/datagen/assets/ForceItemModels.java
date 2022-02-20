package com.mrbysco.forcecraft.datagen.assets;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ForceItemModels extends ItemModelProvider {
	public ForceItemModels(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		withExistingParent("power_ore", modLoc("block/power_ore"));
		withExistingParent("deepslate_power_ore", modLoc("block/deepslate_power_ore"));

		withExistingParent("infuser", modLoc("block/infuser"));
		withExistingParent("force_furnace", modLoc("block/force_furnace"));

		withExistingParent("force_log", modLoc("block/force_log"));
		withExistingParent("force_wood", modLoc("block/force_wood"));
		withExistingParent("force_leaves", modLoc("block/force_leaves"));
		withExistingParent("force_planks", modLoc("block/force_planks"));

		singleTexture("force_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch"));
		singleTexture("force_red_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_red"));
		singleTexture("force_orange_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_orange"));
		singleTexture("force_green_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_green"));
		singleTexture("force_blue_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_blue"));
		singleTexture("force_white_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_white"));
		singleTexture("force_black_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_black"));
		singleTexture("force_brown_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_brown"));
		singleTexture("force_light_blue_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_light_blue"));
		singleTexture("force_magenta_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_magenta"));
		singleTexture("force_pink_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_pink"));
		singleTexture("force_light_gray_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_light_gray"));
		singleTexture("force_lime_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_lime"));
		singleTexture("force_cyan_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_cyan"));
		singleTexture("force_purple_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_purple"));
		singleTexture("force_gray_torch", mcLoc("item/generated"), "layer0", modLoc("block/force_torch_gray"));
		singleTexture("time_torch", mcLoc("item/generated"), "layer0", modLoc("block/time_torch"));

		withExistingParent("force_brick_red", modLoc("block/force_brick_red"));
		withExistingParent("force_brick_yellow", modLoc("block/force_brick_yellow"));
		withExistingParent("force_brick_green", modLoc("block/force_brick_green"));
		withExistingParent("force_brick_blue", modLoc("block/force_brick_blue"));
		withExistingParent("force_brick_white", modLoc("block/force_brick_white"));
		withExistingParent("force_brick_black", modLoc("block/force_brick_black"));
		withExistingParent("force_brick_brown", modLoc("block/force_brick_brown"));
		withExistingParent("force_brick_orange", modLoc("block/force_brick_orange"));
		withExistingParent("force_brick_light_blue", modLoc("block/force_brick_light_blue"));
		withExistingParent("force_brick_magenta", modLoc("block/force_brick_magenta"));
		withExistingParent("force_brick_pink", modLoc("block/force_brick_pink"));
		withExistingParent("force_brick_light_gray", modLoc("block/force_brick_light_gray"));
		withExistingParent("force_brick_lime", modLoc("block/force_brick_lime"));
		withExistingParent("force_brick_cyan", modLoc("block/force_brick_cyan"));
		withExistingParent("force_brick_purple", modLoc("block/force_brick_purple"));
		withExistingParent("force_brick_gray", modLoc("block/force_brick_gray"));
		withExistingParent("force_brick", modLoc("block/force_brick"));

		withExistingParent("force_plank_stairs", modLoc("block/force_plank_stairs"));
		withExistingParent("force_brick_red_stairs", modLoc("block/force_brick_red_stairs"));
		withExistingParent("force_brick_yellow_stairs", modLoc("block/force_brick_yellow_stairs"));
		withExistingParent("force_brick_green_stairs", modLoc("block/force_brick_green_stairs"));
		withExistingParent("force_brick_blue_stairs", modLoc("block/force_brick_blue_stairs"));
		withExistingParent("force_brick_white_stairs", modLoc("block/force_brick_white_stairs"));
		withExistingParent("force_brick_black_stairs", modLoc("block/force_brick_black_stairs"));
		withExistingParent("force_brick_brown_stairs", modLoc("block/force_brick_brown_stairs"));
		withExistingParent("force_brick_orange_stairs", modLoc("block/force_brick_orange_stairs"));
		withExistingParent("force_brick_light_blue_stairs", modLoc("block/force_brick_light_blue_stairs"));
		withExistingParent("force_brick_magenta_stairs", modLoc("block/force_brick_magenta_stairs"));
		withExistingParent("force_brick_pink_stairs", modLoc("block/force_brick_pink_stairs"));
		withExistingParent("force_brick_light_gray_stairs", modLoc("block/force_brick_light_gray_stairs"));
		withExistingParent("force_brick_lime_stairs", modLoc("block/force_brick_lime_stairs"));
		withExistingParent("force_brick_cyan_stairs", modLoc("block/force_brick_cyan_stairs"));
		withExistingParent("force_brick_purple_stairs", modLoc("block/force_brick_purple_stairs"));
		withExistingParent("force_brick_gray_stairs", modLoc("block/force_brick_gray_stairs"));
		withExistingParent("force_brick_stairs", modLoc("block/force_brick_stairs"));

		withExistingParent("force_plank_slab", modLoc("block/force_plank_slab"));
		withExistingParent("force_brick_red_slab", modLoc("block/force_brick_red_slab"));
		withExistingParent("force_brick_yellow_slab", modLoc("block/force_brick_yellow_slab"));
		withExistingParent("force_brick_green_slab", modLoc("block/force_brick_green_slab"));
		withExistingParent("force_brick_blue_slab", modLoc("block/force_brick_blue_slab"));
		withExistingParent("force_brick_white_slab", modLoc("block/force_brick_white_slab"));
		withExistingParent("force_brick_black_slab", modLoc("block/force_brick_black_slab"));
		withExistingParent("force_brick_brown_slab", modLoc("block/force_brick_brown_slab"));
		withExistingParent("force_brick_orange_slab", modLoc("block/force_brick_orange_slab"));
		withExistingParent("force_brick_light_blue_slab", modLoc("block/force_brick_light_blue_slab"));
		withExistingParent("force_brick_magenta_slab", modLoc("block/force_brick_magenta_slab"));
		withExistingParent("force_brick_pink_slab", modLoc("block/force_brick_pink_slab"));
		withExistingParent("force_brick_light_gray_slab", modLoc("block/force_brick_light_gray_slab"));
		withExistingParent("force_brick_lime_slab", modLoc("block/force_brick_lime_slab"));
		withExistingParent("force_brick_cyan_slab", modLoc("block/force_brick_cyan_slab"));
		withExistingParent("force_brick_purple_slab", modLoc("block/force_brick_purple_slab"));
		withExistingParent("force_brick_gray_slab", modLoc("block/force_brick_gray_slab"));
		withExistingParent("force_brick_slab", modLoc("block/force_brick_slab"));
	}

	@Override
	public String getName() {
		return "Item Models";
	}
}