package com.mrbysco.forcecraft.recipe.condition;

import com.google.gson.JsonObject;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.config.ConfigHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ForceTorchRecipeCondition implements ICondition {
	private static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "time_torch_enabled");

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test() {
		return ConfigHandler.COMMON.timeTorchEnabled.get();
	}

	public static class Serializer implements IConditionSerializer<ForceTorchRecipeCondition> {
		public static final ForceTorchRecipeCondition.Serializer INSTANCE = new ForceTorchRecipeCondition.Serializer();

		public void write(JsonObject json, ForceTorchRecipeCondition value) {

		}

		public ForceTorchRecipeCondition read(JsonObject json) {
			return new ForceTorchRecipeCondition();
		}

		public ResourceLocation getID() {
			return ForceTorchRecipeCondition.ID;
		}
	}
}