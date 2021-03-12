package mrbysco.forcecraft.blocks.infuser;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class InfuserModifier {
    	
	ResourceLocation id;
	Ingredient input; 
	InfuserModifierType modifier;

	public InfuserModifier(InfuserModifierType m, Ingredient s) {
		modifier = m;
		input = s;
		id = new ResourceLocation(Reference.MOD_ID, modifier.name().toLowerCase() + "_" + s.hashCode());
		ForceCraft.LOGGER.info("New Infuser Modifier registered {}", id);
	}
	public InfuserModifier(InfuserModifierType m, ItemStack s) {
		this(m, Ingredient.fromStacks(s));
	}
	
}