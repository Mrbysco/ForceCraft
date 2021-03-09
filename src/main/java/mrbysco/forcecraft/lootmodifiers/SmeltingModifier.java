package mrbysco.forcecraft.lootmodifiers;

import com.google.gson.JsonObject;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class SmeltingModifier extends LootModifier {
	public SmeltingModifier(ILootCondition[] conditionsIn) {
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
		ItemStack ctxTool = context.get(LootParameters.TOOL);
		IToolModifier toolModifierCap = ctxTool.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(toolModifierCap != null && toolModifierCap.hasHeat()) {
			return context.getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), context.getWorld())
					.map(FurnaceRecipe::getRecipeOutput)
					.filter(itemStack -> !itemStack.isEmpty())
					.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
					.orElse(stack);
		}
		return stack;
	}

	public static class Serializer extends GlobalLootModifierSerializer<SmeltingModifier> {
		@Override
		public SmeltingModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
			return new SmeltingModifier(conditionsIn);
		}

		@Override
		public JsonObject write(SmeltingModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
