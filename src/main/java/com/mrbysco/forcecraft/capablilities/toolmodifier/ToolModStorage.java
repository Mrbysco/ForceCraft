package com.mrbysco.forcecraft.capablilities.toolmodifier;

import com.mrbysco.forcecraft.items.ForceArmorItem;
import com.mrbysco.forcecraft.items.tools.ForceBowItem;
import com.mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import com.mrbysco.forcecraft.items.tools.ForceRodItem;
import com.mrbysco.forcecraft.items.tools.ForceShearsItem;
import com.mrbysco.forcecraft.items.tools.ForceShovelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ToolModStorage implements Capability.IStorage<IToolModifier> {

	public ToolModStorage() {
	}

	public static void attachInformation(ItemStack stack, List<Component> tooltip) {
		stack.getCapability(CAPABILITY_TOOLMOD).ifPresent(cap -> {
			Item item = stack.getItem();

			if (cap.getSpeedLevel() > 0) {
				if (item instanceof ForceBowItem || item instanceof ForceArmorItem || item instanceof ForceRodItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.speed" + cap.getSpeedLevel()).withStyle(ChatFormatting.YELLOW));
				}
			}
			if (cap.hasLumberjack()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.lumberjack").withStyle(ChatFormatting.YELLOW));
			}
			if(cap.getLuckLevel() > 0) {
				if(item instanceof ForceBowItem || item instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.luck" + cap.getLuckLevel()).withStyle(ChatFormatting.GREEN));
				}
			}
			if(cap.getBaneLevel() > 0) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.bane").withStyle(ChatFormatting.LIGHT_PURPLE));
			}
//			if (cap.getForceLevel() > 0) {
//				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.force" + cap.getForceLevel()));
//			}
			if (cap.getSturdyLevel() > 0) {
				if(stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.sturdy" + cap.getSturdyLevel()).withStyle(ChatFormatting.DARK_PURPLE));
				}
			}
			if (cap.hasWing()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.wing"));
			}
			if (cap.hasBleed()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.bleed" + cap.getBleedLevel()).withStyle(ChatFormatting.RED));
			}
			if (cap.hasRainbow()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.rainbow").withStyle(ChatFormatting.GOLD));
			}
			if (cap.hasHeat()) {
				if(item instanceof ForceShovelItem || item instanceof ForcePickaxeItem || item instanceof ForceShearsItem || item instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.heat").withStyle(ChatFormatting.RED));
				}
			}
			if (cap.hasCamo()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.camo").withStyle(ChatFormatting.DARK_GREEN));
			}
			if (cap.hasEnder()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.ender").withStyle(ChatFormatting.DARK_PURPLE));
			}
			if (cap.hasFreezing()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.freezing").withStyle(ChatFormatting.BLUE));
			}
			if (cap.hasTreasure()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.treasure").withStyle(ChatFormatting.GOLD));
			}
			if (cap.hasLight()) {
				tooltip.add(new TranslatableComponent("item.infuser.tooltip.light").withStyle(ChatFormatting.GOLD));
			}
			if (cap.hasSharp()) {
				if(stack.getItem() instanceof ForceArmorItem) {
					tooltip.add(new TranslatableComponent("item.infuser.tooltip.sharp").withStyle(ChatFormatting.GOLD));
				}
			}
		});
	}

	@Nullable
	@Override
	public Tag writeNBT(Capability<IToolModifier> capability, IToolModifier instance, Direction side) {
		CompoundTag nbt = serializeNBT(instance);
		return nbt;
	}

	@Override
	public void readNBT(Capability<IToolModifier> capability, IToolModifier instance, Direction side, Tag nbtIn) {
		deserializeNBT(instance, nbtIn);
	}

	public static CompoundTag serializeNBT(IToolModifier instance) {
		if (instance == null) {
			return null;
		}
		CompoundTag nbt = new CompoundTag();
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

	public static void deserializeNBT(IToolModifier instance, Tag nbtIn) {
		if (nbtIn instanceof CompoundTag nbt) {
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
