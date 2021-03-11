package mrbysco.forcecraft.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class UpgradeTomeItem extends BaseItem {

	public UpgradeTomeItem(Properties properties) {
		super(properties);
	}


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//        if(!Screen.hasShiftDown()){
//            tooltip.add(new StringTextComponent("Press Shift for Details"));
//            return;
//        }
        TranslationTextComponent tt = new TranslationTextComponent("item.forcecraft.upgrade_tome.tt.tier");
        tt.mergeStyle(Style.EMPTY.applyFormatting(TextFormatting.AQUA));
        tt.appendString(" " + 1);
        tooltip.add(tt); // TODO: get value
//
        tt = new TranslationTextComponent("item.forcecraft.upgrade_tome.tt.points");
        tt.mergeStyle(Style.EMPTY.applyFormatting(TextFormatting.AQUA));
        tt.appendString(" " + 0);
        tooltip.add(tt); // TODO: get zero points
        
        tt = new TranslationTextComponent("item.forcecraft.upgrade_tome.tt.nexttier");
        tt.mergeStyle(Style.EMPTY.applyFormatting(TextFormatting.AQUA));
        tt.appendString(" " + 0);
        tooltip.add(tt); // TODO: get zero points
        
//        tooltip.add(new StringTextComponent(Float.toString(getExperience(stack)) + " / " + Float.toString(getMaxExperience(stack))));
    }
}
