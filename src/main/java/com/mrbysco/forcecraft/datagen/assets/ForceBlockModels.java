package com.mrbysco.forcecraft.datagen.assets;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ForceBlockModels extends BlockModelProvider {
	public ForceBlockModels(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generateOre(ForceRegistry.POWER_ORE.get(), modLoc("block/power_ore"), mcLoc("block/stone"));
	}


	protected void generateOre(Block block, ResourceLocation oreTexture, ResourceLocation stoneVariant) {
		String path = block.getRegistryName().getPath();
		withExistingParent(path, modLoc("block/ore"))
				.texture("ore", oreTexture)
				.texture("stone", stoneVariant);
	}
}
