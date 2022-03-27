package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capabilities.toolmodifier.ToolModCapability;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceArmorItem extends ArmorItem implements IForceChargingTool {

	public ForceArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Item.Properties builderIn) {
		super(materialIn, slot, builderIn);
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
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	// ShareTag for server->client capability data sync
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);

		//on server  this runs . also has correct values.
		//set data for sync to client
		if (cap != null) {
			CompoundTag shareTag = ToolModCapability.writeNBT(cap);

			nbt.put(Reference.MOD_ID, shareTag);
//	        ForceCraft.LOGGER.info("(SERVER) getShareTag : ARMOR{}  ", shareTag);
		}

		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		if (nbt != null && nbt.contains(Reference.MOD_ID)) {

			IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			//these logs run on client. and yes, on client speed:1 its going up as expected
			if (cap != null) {
				CompoundTag shareTag = nbt.getCompound(Reference.MOD_ID);
				ToolModCapability.readNBT(cap, shareTag);

//            	ForceCraft.LOGGER.info("(CLIENT) readShareTag : ARMOR{}  ", shareTag);
				//if we used plain nbt and not capabilities, call super instead
//	        	super.readShareTag(stack, nbt);
			}
		}
		super.readShareTag(stack, nbt);
	}

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if (cap != null && cap.hasCamo()) {
			return "forcecraft:textures/models/armor/force_invisible.png";
		}
		return super.getArmorTexture(stack, entity, slot, type);
	}
}
