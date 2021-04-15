package mrbysco.forcecraft.registry;

import mrbysco.forcecraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public class ForceTags {
	public static final ITag.INamedTag<Block> FORCE_BRICK = BlockTags.makeWrapperTag(Reference.MOD_ID + ":force_brick");

	public static final ITag.INamedTag<Item> VALID_INFUSER_MODIFIERS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_modifiers");
	public static final ITag.INamedTag<Item> VALID_INFUSER_TOOLS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_tools");
	public static final ITag.INamedTag<Item> VALID_INFUSER_CHARGE = ItemTags.makeWrapperTag(Reference.MOD_ID + ":valid_infuser_charge");
	public static final ITag.INamedTag<Item> FORCE_FUELS = ItemTags.makeWrapperTag(Reference.MOD_ID + ":force_fuel");
	public static final ITag.INamedTag<Item> BACONATOR_FOOD = ItemTags.makeWrapperTag(Reference.MOD_ID + ":baconator_food");

	public static final ITag.INamedTag<Item> FORCE_INGOT = forceItemTag("ingots/force");
	public static final ITag.INamedTag<Item> FORCE_NUGGET = forceItemTag("nuggets/force");
	public static final ITag.INamedTag<Item> FORGE_GEM = forceItemTag("gems/force");

	public static final ITag.INamedTag<Fluid> FORCE = forgeFluidTag("force");

	public static final ITag.INamedTag<Block> ENDERTOT_HOLDABLE = forgeBlockTag("endertot_holdable");

	private static INamedTag<Item> forceItemTag(String name) {
		return ItemTags.makeWrapperTag("forge:" + name);
	}
	private static INamedTag<Block> forgeBlockTag(String name) {
		return BlockTags.makeWrapperTag("forge:" + name);
	}
	private static INamedTag<Fluid> forgeFluidTag(String name) {
		return FluidTags.makeWrapperTag("forge:" + name);
	}
}
