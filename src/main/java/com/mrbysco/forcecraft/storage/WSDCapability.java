package com.mrbysco.forcecraft.storage;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WSDCapability implements ICapabilityProvider {
	private final ItemStack stack;
	private LazyOptional<IItemHandler> optional = LazyOptional.empty();

	public WSDCapability(ItemStack stack) {
		this.stack = stack;
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (!optional.isPresent())
				optional = StorageManager.getCapability(stack);

			return optional.cast();
		} else
			return LazyOptional.empty();
	}
}
