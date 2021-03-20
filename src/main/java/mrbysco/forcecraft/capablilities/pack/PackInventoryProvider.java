package mrbysco.forcecraft.capablilities.pack;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PackInventoryProvider implements ICapabilitySerializable<CompoundNBT> {
	private final LazyOptional<PackItemStackHandler> inventory = LazyOptional.of(() -> new PackItemStackHandler());

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		if (inventory.isPresent()) {
			return inventory.resolve().get().serializeNBT();
		}
		return new CompoundNBT();
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		inventory.ifPresent(h -> h.deserializeNBT(nbt));
	}
}