package com.mrbysco.forcecraft.loot;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceLootModifiers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class SmeltModifier extends LootModifier {
	public static final Supplier<MapCodec<SmeltModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, SmeltModifier::new)));


	public SmeltModifier(LootItemCondition[] conditionsIn, int priority) {
		super(conditionsIn, priority);
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ObjectArrayList<ItemStack> ret = new ObjectArrayList<>();
		generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
		return ret;
	}

	private static ItemStack smelt(ItemStack stack, LootContext context) {
		ItemInstance toolStack = context.getOptionalParameter(LootContextParams.TOOL);
		if (toolStack == null)
			return stack;
		if (toolStack.has(ForceComponents.HEAT)) {
			return context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING,
							new SingleRecipeInput(stack), context.getLevel())
					.map(smeltRecipe -> smeltRecipe.value().result().create())
					.filter(itemStack -> !itemStack.isEmpty())
					.map(itemStack -> itemStack.copyWithCount(stack.getCount() * itemStack.getCount()))
					.orElse(stack);
		}
		return stack;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return ForceLootModifiers.SMELTING.get();
	}
}
