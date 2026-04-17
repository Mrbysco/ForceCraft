package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ForceArmorItem extends Item implements IForceChargingTool {

	public ForceArmorItem(ArmorMaterial material, ArmorType type, Item.Properties properties) {
		super(properties.humanoidArmor(material, type));
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		TooltipUtil.addForceTooltips(itemStack, builder);
		ForceToolData fd = new ForceToolData(itemStack);
		fd.attachInformation(builder);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		return this.damageItem(stack, amount);
	}

//	@Override
//	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
//		return false;
//	}
}
