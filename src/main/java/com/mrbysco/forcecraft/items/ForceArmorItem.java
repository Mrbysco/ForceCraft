package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceArmorItem extends ArmorItem implements IForceChargingTool {

	public ForceArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties builderIn) {
		super(materialIn, slot, builderIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (CAPABILITY_TOOLMOD == null) {
			return null;
		}
		return new ToolModProvider();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
		ForceToolData fd = new ForceToolData(stack);
		fd.attachInformation(lores);
		ToolModStorage.attachInformation(stack, lores);
		super.appendHoverText(stack, worldIn, lores, flagIn);
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
	public CompoundNBT getShareTag(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);

		//on server  this runs . also has correct values.
		//set data for sync to client
		if (cap != null) {
			CompoundNBT shareTag = ToolModStorage.serializeNBT(cap);

			nbt.put(Reference.MOD_ID, shareTag);
//	        ForceCraft.LOGGER.info("(SERVER) getShareTag : ARMOR{}  ", shareTag);
		}

		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (nbt != null && nbt.contains(Reference.MOD_ID)) {

			IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
			//these logs run on client. and yes, on client speed:1 its going up as expected
			if (cap != null) {
				INBT shareTag = nbt.get(Reference.MOD_ID);
				ToolModStorage.deserializeNBT(cap, shareTag);

//            	ForceCraft.LOGGER.info("(CLIENT) readShareTag : ARMOR{}  ", shareTag);
				//if we used plain nbt and not capabilities, call super instead
//	        	super.readShareTag(stack, nbt);
			}
		}
		super.readShareTag(stack, nbt);
	}

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if (cap != null && cap.hasCamo()) {
			return "forcecraft:textures/models/armor/force_invisible.png";
		}
		return super.getArmorTexture(stack, entity, slot, type);
	}
}
