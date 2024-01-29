package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.attachment.toolmodifier.ToolModifierAttachment;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.TOOL_MODIFIER;

public class ForceArmorItem extends ArmorItem implements IForceChargingTool {

	public ForceArmorItem(ArmorMaterial materialIn, ArmorItem.Type type, Item.Properties builderIn) {
		super(materialIn, type, builderIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
		ForceToolData fd = new ForceToolData(stack);
		fd.attachInformation(lores);
		ToolModifierAttachment.attachInformation(stack, lores);
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

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		if (stack.hasData(TOOL_MODIFIER) && stack.getData(TOOL_MODIFIER).hasCamo()) {
			return "forcecraft:textures/models/armor/force_invisible.png";
		}
		return super.getArmorTexture(stack, entity, slot, type);
	}
}
