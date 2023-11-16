package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE;
import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE_ITEM;

public class ForceItemTags extends ItemTagsProvider {
	public ForceItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagsProvider, Reference.MOD_ID, existingFileHelper);
	}

	public static final ITag.INamedTag<Item> ORES = forgeTag("ores");
	public static final ITag.INamedTag<Item> ORES_POWER = forgeTag("ores/power");

	private static ITag.INamedTag<Item> forgeTag(String name) {
		return ItemTags.bind(new ResourceLocation("forge", name).toString());
	}

	private static ITag.INamedTag<Item> optionalTag(String modid, String name) {
		return ItemTags.bind(new ResourceLocation(modid, name).toString());
	}

	@Override
	protected void addTags() {
		this.tag(ORES_POWER).add(POWER_ORE_ITEM.get());
		this.tag(ORES).addTag(ORES_POWER);
	}
}