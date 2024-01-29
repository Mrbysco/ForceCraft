package com.mrbysco.forcecraft.datagen.assets;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ForceBlockModelProvider extends BlockModelProvider {
	public ForceBlockModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generateOre(ForceRegistry.POWER_ORE.getId(), modLoc("block/power_ore"), mcLoc("block/stone"));
		generateOre(ForceRegistry.DEEPSLATE_POWER_ORE.getId(), modLoc("block/power_ore"), mcLoc("block/deepslate"));
	}


	protected void generateOre(ResourceLocation blockID, ResourceLocation oreTexture, ResourceLocation stoneVariant) {
		String path = blockID.getPath();
		withExistingParent(path, modLoc("block/ore"))
				.texture("ore", oreTexture)
				.texture("stone", stoneVariant).renderType("cutout");
	}
}
