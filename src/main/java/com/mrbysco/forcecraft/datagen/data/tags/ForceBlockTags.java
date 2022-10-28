package com.mrbysco.forcecraft.datagen.data.tags;

import com.mrbysco.forcecraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ForceBlockTags extends BlockTagsProvider {
	public ForceBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, Reference.MOD_ID, existingFileHelper);
	}

	public static final ITag.INamedTag<Block> RELOCATION_NOT_SUPPORTED = forgeTag("relocation_not_supported");
	public static final ITag.INamedTag<Block> NON_MOVABLE = optionalTag("create", "non_movable");

	private static ITag.INamedTag<Block> forgeTag(String name) {
		return BlockTags.bind(new ResourceLocation("forge", name).toString());
	}

	private static ITag.INamedTag<Block> optionalTag(String modid, String name) {
		return BlockTags.bind(new ResourceLocation(modid, name).toString());
	}

	@Override
	protected void addTags() {

	}
}