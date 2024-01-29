package com.mrbysco.forcecraft.attachment.forcerod;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.List;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.FORCE_ROD;

public class ForceRodAttachment implements IForceRodModifier, INBTSerializable<CompoundTag> {
	boolean camo = false;
	boolean ender = false;
	boolean sight = false;
	int speed = 0;
	int healing = 0;

	GlobalPos homeLocation = null;

	@Override
	public int getHealingLevel() {
		return healing;
	}

	@Override
	public boolean hasHealing() {
		return healing > 0;
	}

	@Override
	public void incrementHealing() {
		healing++;
	}

	@Override
	public void setHealing(int newHealing) {
		healing = newHealing;
	}

	@Override
	public boolean hasCamoModifier() {
		return camo;
	}

	@Override
	public void setCamoModifier(boolean newVal) {
		camo = newVal;
	}

	@Override
	public GlobalPos getHomeLocation() {
		return homeLocation;
	}

	@Override
	public void setHomeLocation(GlobalPos globalPos) {
		homeLocation = globalPos;
	}

	@Override
	public void teleportPlayerToLocation(Player player, GlobalPos globalPos) {
		if (player.level().dimension().location().equals(globalPos.dimension().location())) {
			BlockPos pos = globalPos.pos();
			int x = pos.getX();
			int y = pos.getY() + 1;
			int z = pos.getZ();

			player.randomTeleport(x, y, z, true);
		} else {
			if (!player.level().isClientSide) {
				player.sendSystemMessage(Component.translatable("forcecraft.ender_rod.dimension.text").withStyle(ChatFormatting.YELLOW));
			}
		}
	}

	@Override
	public boolean hasEnderModifier() {
		return ender;
	}

	@Override
	public void setEnderModifier(boolean newVal) {
		ender = newVal;
	}

	@Override
	public boolean isRodofEnder() {
		return ender;
	}

	@Override
	public boolean hasSightModifier() {
		return sight;
	}

	@Override
	public void setSightModifier(boolean newVal) {
		sight = newVal;
	}

	/**
	 * Modifier: Light
	 * Items: Glowstone Dust
	 * Levels: 1
	 * Effect: Shows mobs through walls
	 */

	boolean light;

	@Override
	public boolean hasLight() {
		return light;
	}

	@Override
	public void setLight(boolean val) {
		light = val;
	}

	@Override
	public int getSpeedLevel() {
		return speed;
	}

	@Override
	public void incrementSpeed() {
		setSpeed(speed + 1);
	}

	@Override
	public void setSpeed(int newSpeed) {
		speed = Math.min(3, newSpeed);
	}

	public static void attachInformation(ItemStack stack, List<Component> tooltip) {
		if (stack.hasData(FORCE_ROD)) {
			ForceRodAttachment attachment = stack.getData(FORCE_ROD);
			if (attachment.hasHealing()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.healing" + attachment.getHealingLevel()).withStyle(ChatFormatting.RED));
			}
			if (attachment.getSpeedLevel() > 0) {
				tooltip.add(Component.translatable("item.infuser.tooltip.speed" + attachment.getSpeedLevel()));
			}
			if (attachment.hasCamoModifier()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.camo").withStyle(ChatFormatting.DARK_GREEN));
			}
			if (attachment.hasEnderModifier()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.ender").withStyle(ChatFormatting.DARK_PURPLE));

				if (attachment.getHomeLocation() != null) {
					GlobalPos globalPos = attachment.getHomeLocation();
					BlockPos pos = globalPos.pos();
					tooltip.add(Component.translatable("forcecraft.ender_rod.location", pos.getX(), pos.getY(), pos.getZ(), globalPos.dimension().location()).withStyle(ChatFormatting.YELLOW));
				} else {
					tooltip.add(Component.translatable("forcecraft.ender_rod.unset").withStyle(ChatFormatting.RED));
				}
				tooltip.add(Component.translatable("forcecraft.ender_rod.text").withStyle(ChatFormatting.GRAY));
			}
			if (attachment.hasSightModifier()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.sight").withStyle(ChatFormatting.LIGHT_PURPLE));
			}
			if (attachment.hasLight()) {
				tooltip.add(Component.translatable("item.infuser.tooltip.light").withStyle(ChatFormatting.YELLOW));
			}
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("speed", this.getSpeedLevel());
		tag.putInt("healing", this.getHealingLevel());

		if (this.getHomeLocation() != null) {
			tag.putBoolean("HasHome", true);
			tag.putLong("HomeLocation", this.getHomeLocation().pos().asLong());
			tag.putString("HomeDimension", this.getHomeLocation().dimension().location().toString());
		}

		tag.putBoolean("camo", this.hasCamoModifier());
		tag.putBoolean("ender", this.hasEnderModifier());
		tag.putBoolean("sight", this.hasSightModifier());
		tag.putBoolean("light", this.hasLight());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.setSpeed(tag.getInt("speed"));
		this.setHealing(tag.getInt("healing"));

		if (tag.getBoolean("HasHome")) {
			BlockPos pos = BlockPos.of(tag.getLong("HomeLocation"));
			ResourceLocation location = ResourceLocation.tryParse(tag.getString("HomeDimension"));
			if (location != null) {
				ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, location);
				this.setHomeLocation(GlobalPos.of(dimension, pos));
			}
		}

		this.setCamoModifier(tag.getBoolean("camo"));
		this.setEnderModifier(tag.getBoolean("ender"));
		this.setSightModifier(tag.getBoolean("sight"));
		this.setLight(tag.getBoolean("light"));
	}
}
