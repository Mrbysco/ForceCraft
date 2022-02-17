package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.furnace.ForceFurnaceContainer;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ForceFurnaceBlockEntity extends AbstractForceFurnaceBlockEntity {
	public ForceFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ForceRegistry.FURNACE_TILE.get(), pos, state);
	}

	protected Component getDefaultName() {
		return new TranslatableComponent(Reference.MOD_ID + ".container.force_furnace");
	}

	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new ForceFurnaceContainer(id, player, this);
	}
}
