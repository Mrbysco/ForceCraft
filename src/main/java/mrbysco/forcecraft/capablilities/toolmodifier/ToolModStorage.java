package mrbysco.forcecraft.capablilities.toolmodifier;

import mrbysco.forcecraft.items.ForceArmorItem;
import mrbysco.forcecraft.items.tools.ForceBowItem;
import mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import mrbysco.forcecraft.items.tools.ForceRodItem;
import mrbysco.forcecraft.items.tools.ForceShearsItem;
import mrbysco.forcecraft.items.tools.ForceShovelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ToolModStorage implements Capability.IStorage<IToolModifier> {

	public ToolModStorage() {
	}

	public static void attachInformation(ItemStack stack, List<ITextComponent> tooltip) {
		stack.getCapability(CAPABILITY_TOOLMOD).ifPresent(cap -> {
			Item item = stack.getItem();

			if (cap.getSpeedLevel() > 0) {
				if (item instanceof ForceBowItem || item instanceof ForceArmorItem || item instanceof ForceRodItem) {
					tooltip.add(new TranslationTextComponent("item.infuser.tooltip.speed" + cap.getSpeedLevel()).mergeStyle(TextFormatting.YELLOW));
				}
			}
			if (cap.hasLumberjack()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.lumberjack").mergeStyle(TextFormatting.YELLOW));
			}
			if(cap.getLuckLevel() > 0) {
				if(item instanceof ForceBowItem || item instanceof ForceArmorItem) {
					tooltip.add(new TranslationTextComponent("item.infuser.tooltip.luck" + cap.getLuckLevel()).mergeStyle(TextFormatting.GREEN));
				}
			}
			if(cap.getBaneLevel() > 0) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.bane").mergeStyle(TextFormatting.LIGHT_PURPLE));
			}
//			if (cap.getForceLevel() > 0) {
//				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.force" + cap.getForceLevel()));
//			}
			if (cap.getSturdyLevel() > 0) {
				if(stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(new TranslationTextComponent("item.infuser.tooltip.sturdy" + cap.getSturdyLevel()).mergeStyle(TextFormatting.DARK_PURPLE));
				}
			}
			if (cap.hasWing()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.wing"));
			}
			if (cap.hasBleed()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.bleed" + cap.getBleedLevel()).mergeStyle(TextFormatting.RED));
			}
			if (cap.hasRainbow()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.rainbow").mergeStyle(TextFormatting.GOLD));
			}
			if (cap.hasHeat()) {
				if(item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceShearsItem || item instanceof ForceArmorItem) {
					tooltip.add(new TranslationTextComponent("item.infuser.tooltip.heat").mergeStyle(TextFormatting.RED));
				}
			}
			if (cap.hasCamo()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.camo").mergeStyle(TextFormatting.DARK_GREEN));
			}
			if (cap.hasEnder()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.ender").mergeStyle(TextFormatting.DARK_PURPLE));
			}
			if (cap.hasFreezing()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.freezing").mergeStyle(TextFormatting.BLUE));
			}
			if (cap.hasTreasure()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.treasure").mergeStyle(TextFormatting.GOLD));
			}
			if (cap.hasLight()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.light").mergeStyle(TextFormatting.GOLD));
			}
			if (cap.hasSharp()) {
				if(stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(new TranslationTextComponent("item.infuser.tooltip.sharp").mergeStyle(TextFormatting.GOLD));
				}
			}
		});
	}

	@Nullable
	@Override
	public INBT writeNBT(Capability<IToolModifier> capability, IToolModifier instance, Direction side) {
		CompoundNBT nbt = serializeNBT(instance);
		return nbt;
	}

	@Override
	public void readNBT(Capability<IToolModifier> capability, IToolModifier instance, Direction side, INBT nbtIn) {
		deserializeNBT(instance, nbtIn);
	}

	public static CompoundNBT serializeNBT(IToolModifier instance) {
		if (instance == null) {
			return null;
		}
		CompoundNBT nbt = new CompoundNBT();
		// Speed
		nbt.putInt("speed", instance.getSpeedLevel());
		// Heat
		nbt.putBoolean("heat", instance.hasHeat());
		// Force
		nbt.putInt("force", instance.getForceLevel());
		// Silk
		nbt.putBoolean("silk", instance.hasSilk());
		// Sharpness
		nbt.putInt("sharp", instance.getSharpLevel());
		// Luck
		nbt.putInt("luck", instance.getLuckLevel());
		// Sturdy
		nbt.putInt("sturdy", instance.getSturdyLevel());
		// Rainbow
		nbt.putBoolean("rainbow", instance.hasRainbow());
		// Lumberjack
		nbt.putBoolean("lumber", instance.hasLumberjack());
		// Bleeding
		nbt.putInt("bleed", instance.getBleedLevel());
		// Bane
		nbt.putInt("bane", instance.getBaneLevel());
		// Wing
		nbt.putBoolean("wing", instance.hasWing());
		// Camo
		nbt.putBoolean("camo", instance.hasCamo());
		// Sight
		nbt.putBoolean("sight", instance.hasSight());
		// Light
		nbt.putBoolean("light", instance.hasLight());
		// Ender
		nbt.putBoolean("ender", instance.hasEnder());
		// Freezing
		nbt.putBoolean("freezing", instance.hasFreezing());
		// Treasure
		nbt.putBoolean("treasure", instance.hasTreasure());
		return nbt;
	}

	public static void deserializeNBT(IToolModifier instance, INBT nbtIn) {
		if (nbtIn instanceof CompoundNBT) {
			CompoundNBT nbt = (CompoundNBT) nbtIn;
			instance.setSpeed(nbt.getInt("speed"));
			instance.setHeat(nbt.getBoolean("heat"));
			instance.setForce(nbt.getInt("force"));
			instance.setSilk(nbt.getBoolean("silk"));
			instance.setSharp(nbt.getInt("sharp"));
			instance.setLuck(nbt.getInt("luck"));
			instance.setSturdy(nbt.getInt("sturdy"));
			instance.setRainbow(nbt.getBoolean("rainbow"));
			instance.setLumberjack(nbt.getBoolean("lumber"));
			instance.setBleed(nbt.getInt("bleed"));
			instance.setBane(nbt.getInt("bane"));
			instance.setWing(nbt.getBoolean("wing"));
			instance.setCamo(nbt.getBoolean("camo"));
			instance.setSight(nbt.getBoolean("sight"));
			instance.setLight(nbt.getBoolean("light"));
			instance.setEnder(nbt.getBoolean("ender"));
			instance.setFreezing(nbt.getBoolean("freezing"));
			instance.setTreasure(nbt.getBoolean("treasure"));
		}
	}

}
