package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.mrbysco.forcecraft.registry.ForceRegistry.*;

public class ForceItemTags extends ItemTagsProvider {
	public ForceItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
						 TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagProvider.contentsGetter(), Reference.MOD_ID, existingFileHelper);
	}

	public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = forgeTag("ores_in_ground/deepslate");
	public static final TagKey<Item> ORES_IN_GROUND_STONE = forgeTag("ores_in_ground/stone");
	public static final TagKey<Item> ORES = forgeTag("ores");
	public static final TagKey<Item> ORES_POWER = forgeTag("ores/power");

	private static TagKey<Item> forgeTag(String name) {
		return ItemTags.create(new ResourceLocation("forge", name));
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ORES_IN_GROUND_DEEPSLATE).add(DEEPSLATE_POWER_ORE_ITEM.get());
		this.tag(ORES_IN_GROUND_STONE).add(POWER_ORE_ITEM.get());
		this.tag(ORES_POWER).add(POWER_ORE_ITEM.get(), DEEPSLATE_POWER_ORE_ITEM.get());
		this.tag(ORES).addTag(ORES_POWER);
	}
}