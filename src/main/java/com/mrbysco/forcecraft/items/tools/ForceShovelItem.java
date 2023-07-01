package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capabilities.toolmodifier.ToolModCapability;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.Reference.MODIFIERS.*;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceShovelItem extends ShovelItem implements IForceChargingTool {

	public List<Reference.MODIFIERS> applicableModifers = new ArrayList<>();

	public ForceShovelItem(Item.Properties properties) {
		super(ModToolTiers.FORCE, -7F, -3.0F, properties);
		setApplicableModifiers();
	}

	public void setApplicableModifiers() {
		applicableModifers.add(MOD_CHARGE);
		applicableModifers.add(MOD_CHARGEII);
		applicableModifers.add(MOD_HEAT);
		applicableModifers.add(MOD_LUCK);
		applicableModifers.add(MOD_GRINDING);
		applicableModifers.add(MOD_TOUCH);
		applicableModifers.add(MOD_STURDY);
		applicableModifers.add(MOD_REPAIR);
		applicableModifers.add(MOD_SPEED);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if (CAPABILITY_TOOLMOD == null) {
			return null;
		}
		return new ToolModCapability();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
		ForceToolData fd = new ForceToolData(stack);
		fd.attachInformation(lores);
		ToolModCapability.attachInformation(stack, lores);
		super.appendHoverText(stack, level, lores, flagIn);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	// ShareTag for server->client capability data sync
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = super.getShareTag(stack);

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if (cap != null) {
			CompoundTag shareTag = ToolModCapability.writeNBT(cap);
			nbt.put(Reference.MOD_ID, shareTag);
		}
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		if (nbt == null || !nbt.contains(Reference.MOD_ID)) {
			return;
		}
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if (cap != null) {
			CompoundTag shareTag = nbt.getCompound(Reference.MOD_ID);
			ToolModCapability.readNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}
}
