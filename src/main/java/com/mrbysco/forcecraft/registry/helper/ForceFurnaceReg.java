package com.mrbysco.forcecraft.registry.helper;

import com.mrbysco.forcecraft.block.ForceFurnaceBlock;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jspecify.annotations.Nullable;

public class ForceFurnaceReg {
	protected final @Nullable DyeColor color;
	protected final DeferredBlock<Block> furnace;

	public @Nullable DyeColor getColor() {
		return color;
	}

	public DeferredBlock<Block> getFurnace() {
		return furnace;
	}

	public ForceFurnaceReg(DyeColor color) {
		this.color = color;
		String prefix = "force_furnace" + (color != null ? "_" : "");
		String colorName = color != null ? color.getName() : "";

		this.furnace = ForceRegistry.BLOCKS.registerBlock(prefix + colorName, ForceFurnaceBlock::new, () ->
				BlockBehaviour.Properties.ofFullCopy(ForceRegistry.FORCE_FURNACE.get())
		);
		ForceRegistry.ITEMS.registerSimpleBlockItem(this.furnace);
	}
}
