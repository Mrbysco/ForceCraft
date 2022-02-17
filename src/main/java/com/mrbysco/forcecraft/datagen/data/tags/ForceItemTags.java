package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ForceItemTags extends ItemTagsProvider {
	public ForceItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagsProvider, Reference.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {

	}
}