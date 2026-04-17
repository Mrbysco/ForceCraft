package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import com.mrbysco.forcecraft.util.TooltipUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_CHARGE;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_CHARGEII;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_GRINDING;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_HEAT;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_LUCK;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_REPAIR;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_SPEED;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_STURDY;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_TOUCH;

public class ForceShovelItem extends ShovelItem implements IForceChargingTool {

	public List<Reference.MODIFIERS> applicableModifers = new ArrayList<>();

	public ForceShovelItem(Item.Properties properties) {
		super(ModToolTiers.FORCE, -7F, -3.0F, properties.enchantable(0));
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
//	public int getEnchantmentValue() {
//		return 0;
//	}
//
//	@Override
//	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
//		return false;
//	}
}
