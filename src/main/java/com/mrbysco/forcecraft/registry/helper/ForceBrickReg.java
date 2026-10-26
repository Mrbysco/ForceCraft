package com.mrbysco.forcecraft.registry.helper;

import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jspecify.annotations.Nullable;

public class ForceBrickReg {
	protected final @Nullable DyeColor color;
	protected final DeferredBlock<Block> brick;
	protected final DeferredBlock<WallBlock> wall;
	protected final DeferredBlock<StairBlock> stairs;
	protected final DeferredBlock<SlabBlock> slab;

	public @Nullable DyeColor getColor() {
		return color;
	}

	public DeferredBlock<Block> getBrick() {
		return brick;
	}

	public DeferredBlock<WallBlock> getWall() {
		return wall;
	}

	public DeferredBlock<StairBlock> getStairs() {
		return stairs;
	}

	public DeferredBlock<SlabBlock> getSlab() {
		return slab;
	}

	public BlockFamily getBlockFamily() {
		return new BlockFamily.Builder(brick.get())
				.wall(this.wall.get())
				.stairs(this.stairs.get())
				.slab(this.slab.get())
				.recipeGroupPrefix("force_brick")
				.getFamily();
	}

	public ForceBrickReg(DyeColor color) {
		this.color = color;
		String prefix = "force_brick" + (color != null ? "_" : "");
		String colorName = color != null ? color.getName() : "";

		this.brick = ForceRegistry.BLOCKS.registerBlock(prefix + colorName, Block::new, () ->
			BlockBehaviour.Properties.of().mapColor(color != null ? color : DyeColor.YELLOW)
					.strength(2.0F, 12.0F).requiresCorrectToolForDrops()
		);
		ForceRegistry.ITEMS.registerSimpleBlockItem(this.brick);

		this.wall = ForceRegistry.BLOCKS.registerBlock(prefix + colorName + "_wall", WallBlock::new,
				() -> BlockBehaviour.Properties.ofLegacyCopy(this.brick.get()).forceSolidOn());
		ForceRegistry.ITEMS.registerSimpleBlockItem(this.wall);

		this.stairs = ForceRegistry.BLOCKS.registerBlock(prefix + colorName + "_stairs", (prop) ->
						new StairBlock(this.brick.get().defaultBlockState(), prop),
				() -> BlockBehaviour.Properties.ofLegacyCopy(this.brick.get()));
		ForceRegistry.ITEMS.registerSimpleBlockItem(this.stairs);

		this.slab = ForceRegistry.BLOCKS.registerBlock(prefix + colorName + "_slab", SlabBlock::new,
				() -> BlockBehaviour.Properties.ofLegacyCopy(this.brick.get()));
		ForceRegistry.ITEMS.registerSimpleBlockItem(this.slab);
	}
}
