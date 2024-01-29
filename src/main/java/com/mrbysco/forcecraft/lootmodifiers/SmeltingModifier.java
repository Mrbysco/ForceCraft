package com.mrbysco.forcecraft.lootmodifiers;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.TOOL_MODIFIER;

public class SmeltingModifier extends LootModifier {
	public static final Supplier<Codec<SmeltingModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SmeltingModifier::new)));


	public SmeltingModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ObjectArrayList<ItemStack> ret = new ObjectArrayList<>();
		generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
		return ret;
	}

	private static ItemStack smelt(ItemStack stack, LootContext context) {
		ItemStack ctxTool = context.getParamOrNull(LootContextParams.TOOL);
		if (ctxTool == null)
			return stack;
		RegistryAccess registryAccess = context.getLevel().registryAccess();
		if (ctxTool.hasData(TOOL_MODIFIER) && ctxTool.getData(TOOL_MODIFIER).hasHeat()) {
			return context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel())
					.map(smeltRecipe -> smeltRecipe.value().getResultItem(registryAccess))
					.filter(itemStack -> !itemStack.isEmpty())
					.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
					.orElse(stack);
		}
		return stack;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
