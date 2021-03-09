package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public class ForceTags {
	public static final ITag.INamedTag<Block> FORCE_BRICK = BlockTags.makeWrapperTag(Reference.MOD_ID + ":force_brick");

	public static final ITag.INamedTag<Item> VALID_INFUSER_MODIFIERS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_modifiers");
	public static final ITag.INamedTag<Item> VALID_INFUSER_TOOLS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_tools");
	public static final ITag.INamedTag<Item> FORCE_FUELS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":force_fuel");

	public static final ITag.INamedTag<Item> FORCE_INGOT = forgeTag("ingots/force");
	public static final ITag.INamedTag<Item> FORCE_NUGGET = forgeTag("nuggets/force");
	public static final ITag.INamedTag<Item> FORGE_GEM = forgeTag("gems/force");

	private static INamedTag<Item> forgeTag(String name) {
		return ItemTags.makeWrapperTag("forge:" + name);
	}
}
