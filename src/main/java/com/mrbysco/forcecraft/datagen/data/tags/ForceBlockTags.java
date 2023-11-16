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

import static com.mrbysco.forcecraft.registry.ForceRegistry.POWER_ORE;

public class ForceBlockTags extends BlockTagsProvider {
	public ForceBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, Reference.MOD_ID, existingFileHelper);
	}

	public static final ITag.INamedTag<Block> ORES = forgeTag("ores");
	public static final ITag.INamedTag<Block> ORES_POWER = forgeTag("ores/power");

	private static ITag.INamedTag<Block> forgeTag(String name) {
		return BlockTags.bind(new ResourceLocation("forge", name).toString());
	}

	private static ITag.INamedTag<Block> optionalTag(String modid, String name) {
		return BlockTags.bind(new ResourceLocation(modid, name).toString());
	}

	@Override
	protected void addTags() {
		this.tag(ORES_POWER).add(POWER_ORE.get());
		this.tag(ORES).addTag(ORES_POWER);
	}
}