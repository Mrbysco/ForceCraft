//package com.mrbysco.forcecraft.compat.rei.infuser.category;
//
//import com.mrbysco.forcecraft.compat.rei.REIPlugin;
//import com.mrbysco.forcecraft.compat.rei.infuser.display.InfuserDisplay;
//import com.mrbysco.forcecraft.registry.ForceRegistry;
//import me.shedaniel.math.Point;
//import me.shedaniel.math.Rectangle;
//import me.shedaniel.rei.api.client.gui.Renderer;
//import me.shedaniel.rei.api.client.gui.widgets.Widget;
//import me.shedaniel.rei.api.client.gui.widgets.Widgets;
//import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
//import me.shedaniel.rei.api.common.category.CategoryIdentifier;
//import me.shedaniel.rei.api.common.entry.EntryStack;
//import me.shedaniel.rei.api.common.util.EntryStacks;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.item.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class InfuserCategory implements DisplayCategory<InfuserDisplay> {
//	private final Component title = Component.translatable("forcecraft.gui.jei.category.infuser");
//	private final EntryStack<ItemStack> icon = EntryStacks.of(ForceRegistry.INFUSER.get());
//
//	@Override
//	public List<Widget> setupDisplay(InfuserDisplay display, Rectangle bounds) {
//		Point centerPoint = new Point(bounds.getCenterX(), bounds.getCenterY());
//		List<Widget> widgets = new ArrayList<>();
//
//		//Background
//		widgets.add(Widgets.createTexturedWidget(REIPlugin.RECIPE_INFUSER_JEI,
//				centerPoint.getX() - (137 / 2), centerPoint.getY() - (109 / 2), 0, 0, 137, 109));
//
//		//Tier
//		widgets.add(Widgets.createLabel(new Point(centerPoint.x - 64, centerPoint.y - 52),
//						Component.translatable("forcecraft.gui.jei.category.infuser.tier", display.getTier().asInt()))
//				.leftAligned());
//		//Modifier type
//		widgets.add(Widgets.createLabel(new Point(centerPoint.x - 64, centerPoint.y + 46),
//						Component.translatable(display.getResultModifier().getTooltip()))
//				.leftAligned());
//
//		//Center
//		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 22, centerPoint.y - 7)).entries(display.getInputEntries().getFirst())
//				.disableBackground().markInput());
//
//		//Ingredient
//		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 14, centerPoint.y - 7)).entries(display.getInputEntries().get(1))
//				.disableBackground().markInput());
//
//		//Output
//		if (display.getOutputEntries().isEmpty()) {
//			widgets.add(Widgets.createSlot(new Point(centerPoint.x + 52, centerPoint.y - 7)).entries(display.getInputEntries().getFirst())
//					.markOutput());
//		} else {
//			widgets.add(Widgets.createSlot(new Point(centerPoint.x + 52, centerPoint.y - 7)).entries(display.getOutputEntries().getFirst())
//					.markOutput());
//		}
//		return widgets;
//	}
//
//	@Override
//	public CategoryIdentifier<? extends InfuserDisplay> getCategoryIdentifier() {
//		return REIPlugin.INFUSER_CATEGORY;
//	}
//
//	@Override
//	public Component getTitle() {
//		return title;
//	}
//
//	@Override
//	public Renderer getIcon() {
//		return icon;
//	}
//
//	@Override
//	public int getDisplayHeight() {
//		return 109;
//	}
//
//	@Override
//	public int getDisplayWidth(InfuserDisplay display) {
//		return 137;
//	}
//}
