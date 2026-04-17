package com.mrbysco.forcecraft.client.extension;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.components.ForceComponents;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jspecify.annotations.Nullable;

public class ForceArmorItemExtension implements IClientItemExtensions {
	@Override
	public @Nullable Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier _default) {
		if (stack.has(ForceComponents.TOOL_CAMO)) {
			return Reference.modLoc("textures/models/armor/force_invisible.png");
		}
		return IClientItemExtensions.super.getArmorTexture(stack, type, layer, _default);
	}
}
