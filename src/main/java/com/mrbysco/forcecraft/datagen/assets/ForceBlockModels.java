package com.mrbysco.forcecraft.datagen.assets;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceBlockModels extends BlockModelProvider {
	public ForceBlockModels(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generateOre(ForceRegistry.POWER_ORE.get(), modLoc("block/power_ore"), mcLoc("block/stone"));
		generateOre(ForceRegistry.DEEPSLATE_POWER_ORE.get(), modLoc("block/power_ore"), mcLoc("block/deepslate"));
	}


	protected void generateOre(Block block, ResourceLocation oreTexture, ResourceLocation stoneVariant) {
		String path = ForgeRegistries.BLOCKS.getKey(block).getPath();
		withExistingParent(path, modLoc("block/ore"))
				.texture("ore", oreTexture)
				.texture("stone", stoneVariant).renderType("cutout");
	}
}
