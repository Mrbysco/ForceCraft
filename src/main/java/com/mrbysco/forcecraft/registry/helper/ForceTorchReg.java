package com.mrbysco.forcecraft.registry.helper;

import com.mrbysco.forcecraft.block.torch.CamoTorchBlock;
import com.mrbysco.forcecraft.block.torch.ForceTorchBlock;
import com.mrbysco.forcecraft.block.torch.WallCamoTorchBlock;
import com.mrbysco.forcecraft.block.torch.WallForceTorchBlock;
import com.mrbysco.forcecraft.blockentity.torch.AbstractTorchBlockEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class ForceTorchReg {
	protected final @Nullable DyeColor color;
	protected final DeferredBlock<TorchBlock> torch;
	protected final DeferredBlock<WallTorchBlock> wallTorch;
	protected final DeferredItem<StandingAndWallBlockItem> torchItem;
	protected boolean specialTorch;

	public DeferredBlock<TorchBlock> getTorch() {
		return torch;
	}

	public DeferredBlock<WallTorchBlock> getWallTorch() {
		return wallTorch;
	}

	public DeferredItem<StandingAndWallBlockItem> getTorchItem() {
		return torchItem;
	}

	public @Nullable DyeColor getColor() {
		return color;
	}

	public boolean isSpecialTorch() {
		return specialTorch;
	}

	public ForceTorchReg(String name, @Nullable DyeColor color, Optional<Supplier<BlockEntityType<AbstractTorchBlockEntity>>> blockEntityType) {
		this.color = color;
		this.specialTorch = blockEntityType.isPresent();
		if (name.equals("camo_torch")) {
			this.specialTorch = true;
			this.torch = ForceRegistry.BLOCKS.registerBlock(name, (properties) -> new CamoTorchBlock(ParticleTypes.FLAME, properties), () -> BlockBehaviour.Properties.of().mapColor(color != null ? color : DyeColor.ORANGE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD));
			this.wallTorch = ForceRegistry.BLOCKS.registerBlock("wall_" + name, (properties) -> new WallCamoTorchBlock(ParticleTypes.FLAME, properties), () -> BlockBehaviour.Properties.ofFullCopy(torch.get()));
		} else {
			if (specialTorch) {
				this.torch = ForceRegistry.BLOCKS.registerBlock(name, (properties) -> new ForceTorchBlock(ParticleTypes.FLAME, properties, blockEntityType.get()), () -> BlockBehaviour.Properties.of().mapColor(color != null ? color : DyeColor.ORANGE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD));
				this.wallTorch = ForceRegistry.BLOCKS.registerBlock("wall_" + name, (properties) -> new WallForceTorchBlock(ParticleTypes.FLAME, properties, blockEntityType.get()), () -> BlockBehaviour.Properties.ofFullCopy(torch.get()));
			} else {
				this.torch = ForceRegistry.BLOCKS.registerBlock(name, (properties) -> new TorchBlock(ParticleTypes.FLAME, properties), () -> BlockBehaviour.Properties.of().mapColor(color != null ? color : DyeColor.ORANGE).noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOD));
				this.wallTorch = ForceRegistry.BLOCKS.registerBlock("wall_" + name, (properties) -> new WallTorchBlock(ParticleTypes.FLAME, properties), () -> BlockBehaviour.Properties.ofFullCopy(torch.get()));
			}
		}
		this.torchItem = ForceRegistry.ITEMS.registerItem(name, (properties) -> new StandingAndWallBlockItem(torch.get(), wallTorch.get(), Direction.DOWN, properties.useBlockDescriptionPrefix()));
	}
}
