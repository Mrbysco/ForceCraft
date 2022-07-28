package com.mrbysco.forcecraft.blockentities;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.furnace.ForceFurnaceMenu;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ForceFurnaceBlockEntity extends AbstractForceFurnaceBlockEntity {
	public ForceFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ForceRegistry.FURNACE_BLOCK_ENTITY.get(), pos, state);
	}

	protected Component getDefaultName() {
		return Component.translatable(Reference.MOD_ID + ".container.force_furnace");
	}

	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new ForceFurnaceMenu(id, player, this);
	}
}
