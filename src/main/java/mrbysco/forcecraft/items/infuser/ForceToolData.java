package mrbysco.forcecraft.items.infuser;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ForceToolData {

	private int force = 0;
	
	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public ForceToolData(ItemStack tool) {
		CompoundNBT tag = tool.getTag();
		if(tag != null && tag.contains("force")) {
			this.read(tool, tag);
		}
	}

	private void read(ItemStack tool, CompoundNBT tag) {
		force = tag.getInt("force");
	}

	public CompoundNBT write(ItemStack tool) {
		CompoundNBT tag = tool.getOrCreateTag();
		tag.putInt("force", force);
		return tag;
	}

	public void charge(int incoming) {
		force += incoming;
	}

	public void attachInformation(List<ITextComponent> tooltip) {
		if(this.force > 0) { 
			TranslationTextComponent t = new TranslationTextComponent("item.infuser.tooltip.forcelevel");
			t.appendString("" + this.force);
			t.mergeStyle(TextFormatting.GOLD); 
			tooltip.add(t);
		}
	}
}
