package com.mrbysco.forcecraft.recipe.condition;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForceConditions {
	@SubscribeEvent
	public void onRegisterSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
		CraftingHelper.register(ForceTorchRecipeCondition.Serializer.INSTANCE);
	}
}
