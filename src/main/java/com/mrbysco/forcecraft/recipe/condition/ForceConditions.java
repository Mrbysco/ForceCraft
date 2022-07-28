package com.mrbysco.forcecraft.recipe.condition;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class ForceConditions {
	@SubscribeEvent
	public void onRegisterSerializers(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(ForceTorchRecipeCondition.Serializer.INSTANCE);
		}
	}
}
