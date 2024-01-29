package com.mrbysco.forcecraft.attachment.forcewrench;

import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.List;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.FORCE_WRENCH;

public class ForceWrenchAttachment implements IForceWrench, INBTSerializable<CompoundTag> {
	CompoundTag storedBlockNBT = null;
	BlockState storedBlockState = null;
	String name = "";

	@Override
	public boolean hasBlockStored() {
		return storedBlockState != null;
	}

	@Override
	public boolean canStoreBlock() {
		return hasBlockStored();
	}

	@Override
	public net.minecraft.nbt.CompoundTag getStoredBlockNBT() {
		return storedBlockNBT;
	}

	@Override
	public BlockState getStoredBlockState() {
		return storedBlockState;
	}

	@Override
	public String getStoredName() {
		return name;
	}

	@Override
	public void storeBlockNBT(net.minecraft.nbt.CompoundTag tag) {
		storedBlockNBT = tag;
	}

	@Override
	public void storeBlockState(BlockState base) {
		storedBlockState = base;
	}

	@Override
	public void setBlockName(String name) {
		this.name = name;
	}

	@Override
	public void clearBlockStorage() {
		storedBlockState = null;
		storedBlockNBT = null;
		name = "";
	}

	public static void attachInformation(ItemStack stack, List<Component> tooltip) {
		ForceToolData fd = new ForceToolData(stack);
		fd.attachInformation(tooltip);
		if (stack.hasData(FORCE_WRENCH)) {
			ForceWrenchAttachment attachment = stack.getData(FORCE_WRENCH);
			if (attachment.getStoredName() != null && !attachment.getStoredName().isEmpty()) { // idk what this is
				tooltip.add(Component.literal("Stored: ").withStyle(ChatFormatting.GOLD)
						.append(Component.translatable(attachment.getStoredName()).withStyle(ChatFormatting.GRAY)));
			}
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		if (this.getStoredBlockNBT() != null) {
			tag.put("storedNBT", this.getStoredBlockNBT());
		}

		if (this.getStoredBlockState() != null) {
			tag.put("storedBlockState", NbtUtils.writeBlockState(this.getStoredBlockState()));
		}
		if (!this.getStoredName().isEmpty()) {
			tag.putString("name", this.getStoredName());
		}

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.storeBlockNBT(tag.getCompound("storedNBT"));
		if (tag.contains("storedBlockState")) {
			this.storeBlockState(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("storedBlockState")));
		}
		this.setBlockName(tag.getString("name"));
	}
}
