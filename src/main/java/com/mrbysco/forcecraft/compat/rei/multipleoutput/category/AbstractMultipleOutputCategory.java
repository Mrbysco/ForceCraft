//package com.mrbysco.forcecraft.compat.rei.multipleoutput.category;
//
//import com.mrbysco.forcecraft.compat.rei.REIPlugin;
//import com.mrbysco.forcecraft.compat.rei.multipleoutput.display.AbstractMultipleOutputDisplay;
//import me.shedaniel.math.Point;
//import me.shedaniel.math.Rectangle;
//import me.shedaniel.rei.api.client.gui.widgets.Widget;
//import me.shedaniel.rei.api.client.gui.widgets.Widgets;
//import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class AbstractMultipleOutputCategory<T extends AbstractMultipleOutputDisplay> implements DisplayCategory<T> {
//
//	@Override
//	public List<Widget> setupDisplay(T display, Rectangle bounds) {
//		Point centerPoint = new Point(bounds.getCenterX(), bounds.getCenterY());
//		List<Widget> widgets = new ArrayList<>();
//
//		widgets.add(Widgets.createTexturedWidget(REIPlugin.RECIPE_MULTIPLES_JEI, centerPoint.getX() - 70, centerPoint.getY() - 18, 0, textureV(), 140, 37));
//
//		widgets.add(Widgets.createSlot(new Point(centerPoint.x - 60, centerPoint.y - 8)).entries(display.getInputEntries().getFirst())
//				.disableBackground().markInput());
//
//		widgets.add(Widgets.createSlot(new Point(centerPoint.x + 13, centerPoint.y - 8)).entries(display.getOutputEntries().getFirst())
//				.disableBackground().markInput());
//
//		if (display.getOutputEntries().size() > 1) {
//			widgets.add(Widgets.createSlot(new Point(centerPoint.x + 43, centerPoint.y - 8)).entries(display.getOutputEntries().get(1))
//					.disableBackground().markInput());
//		}
//
//		return widgets;
//	}
//
//	public int textureV() {
//		return 0;
//	}
//
//	@Override
//	public int getDisplayWidth(T display) {
//		return 140;
//	}
//
//	@Override
//	public int getDisplayHeight() {
//		return 37;
//	}
//}
