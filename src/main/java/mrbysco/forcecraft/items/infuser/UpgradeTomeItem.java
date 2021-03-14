package mrbysco.forcecraft.items.infuser;

import java.util.List;

import javax.annotation.Nullable;

import mrbysco.forcecraft.items.BaseItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class UpgradeTomeItem extends BaseItem {

	public UpgradeTomeItem(Properties properties) {
		super(properties.maxStackSize(1));
	}


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

		UpgradeBookData bd = new UpgradeBookData(stack);
	
        TranslationTextComponent tt = new TranslationTextComponent("item.forcecraft.upgrade_tome.tt.tier");
        tt.mergeStyle(Style.EMPTY.applyFormatting(TextFormatting.AQUA));
        tt.appendString(" " + bd.getTier());
        tooltip.add(tt);  

        if(!Screen.hasShiftDown()){
		  tooltip.add(new TranslationTextComponent("forcecraft.tooltip.press_shift"));
		  return;
      	}
        tt = new TranslationTextComponent("item.forcecraft.upgrade_tome.tt.points");
        tt.mergeStyle(Style.EMPTY.applyFormatting(TextFormatting.AQUA));
        tt.appendString(" " + bd.points);
        tooltip.add(tt); 
        
        tt = new TranslationTextComponent("item.forcecraft.upgrade_tome.tt.nexttier");
        tt.mergeStyle(Style.EMPTY.applyFormatting(TextFormatting.AQUA));
        tt.appendString(" " + bd.nextTier());
        tooltip.add(tt);  

    }

	public static void onModifierApplied(ItemStack bookInSlot, ItemStack modifier, ItemStack tool) {
		UpgradeBookData bd = new UpgradeBookData(bookInSlot);
		bd.incrementPoints(25); // TODO: points per modifier upgrade value !! 
		bd.write(bookInSlot);
	}
}
