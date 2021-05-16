package mrbysco.forcecraft.recipe.condition;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForceConditions {
	@SubscribeEvent
	public void onRegisterSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		CraftingHelper.register(ForceTorchRecipeCondition.Serializer.INSTANCE);
	}
}
