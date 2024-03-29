//package com.mrbysco.forcecraft.capabilities.pack;
//
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ICapabilitySerializable;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//public class PackInventoryProvider implements ICapabilitySerializable<CompoundTag> {
//	private final LazyOptional<PackItemStackHandler> inventory = LazyOptional.of(() -> new PackItemStackHandler());
//
//	@Nonnull
//	@Override
//	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//		if (cap == ForgeCapabilities.ITEM_HANDLER) {
//			return inventory.cast();
//		}
//		return LazyOptional.empty();
//	}
//
//	@Override
//	public CompoundTag serializeNBT() {
//		if (inventory.isPresent()) {
//			return inventory.resolve().get().serializeNBT();
//		}
//		return new CompoundTag();
//	}
//
//	@Override
//	public void deserializeNBT(CompoundTag nbt) {
//		inventory.ifPresent(h -> h.deserializeNBT(nbt));
//	}
//}