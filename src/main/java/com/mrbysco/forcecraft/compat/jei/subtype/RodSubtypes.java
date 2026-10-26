package com.mrbysco.forcecraft.compat.jei.subtype;

import com.mrbysco.forcecraft.registry.ForceComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class RodSubtypes implements ISubtypeInterpreter<ItemStack> {
	private static final List<Supplier<? extends DataComponentType<?>>> COMPONENTS = List.of(
			ForceComponents.SIGHT,
			ForceComponents.CAMO,
			ForceComponents.TIME,
			ForceComponents.ENDER,
			ForceComponents.HEAT,
			ForceComponents.HEALING,
			ForceComponents.SPEED
	);

	@Override
	public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
		for (Supplier<? extends DataComponentType<?>> component : COMPONENTS) {
			DataComponentType<?> type = component.get();
			Object value = ingredient.get(type);
			if (value != null) {
				return type + "=" + value;
			}
		}
		return null;
	}
}
