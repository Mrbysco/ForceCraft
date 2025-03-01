package com.mrbysco.forcecraft.compat.rei.multipleoutput.category;

import com.mrbysco.forcecraft.compat.rei.REIPlugin;
import com.mrbysco.forcecraft.compat.rei.multipleoutput.display.FreezingDisplay;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FreezingCategory extends AbstractMultipleOutputCategory<FreezingDisplay> {

	private final Component title = Component.translatable("forcecraft.gui.jei.category.freezing");
	private final EntryStack<ItemStack> icon = EntryStacks.of(ForceRegistry.FREEZING_CORE.get());

	@Override
	public CategoryIdentifier<? extends FreezingDisplay> getCategoryIdentifier() {
		return REIPlugin.FREEZING_CATEGORY;
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Renderer getIcon() {
		return icon;
	}

	@Override
	public int textureV() {
		return 37;
	}
}
