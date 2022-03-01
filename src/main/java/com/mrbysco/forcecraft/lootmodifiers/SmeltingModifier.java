package com.mrbysco.forcecraft.lootmodifiers;

import com.google.gson.JsonObject;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class SmeltingModifier extends LootModifier {
	public SmeltingModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
		return ret;
	}

	private static ItemStack smelt(ItemStack stack, LootContext context) {
		ItemStack ctxTool = context.getParamOrNull(LootContextParams.TOOL);
		IToolModifier toolModifierCap = ctxTool.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(toolModifierCap != null && toolModifierCap.hasHeat()) {
			return context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel())
					.map(SmeltingRecipe::getResultItem)
					.filter(itemStack -> !itemStack.isEmpty())
					.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
					.orElse(stack);
		}
		return stack;
	}

	public static class Serializer extends GlobalLootModifierSerializer<SmeltingModifier> {
		@Override
		public SmeltingModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
			return new SmeltingModifier(conditionsIn);
		}

		@Override
		public JsonObject write(SmeltingModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
