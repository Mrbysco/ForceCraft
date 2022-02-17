package com.mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;

public class ForceRodCapability implements IForceRodModifier, ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
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
        if(player.level.dimension().location().equals(globalPos.dimension().location())) {
            BlockPos pos = globalPos.pos();
            int x = pos.getX();
            int y = pos.getY() + 1;
            int z = pos.getZ();

            player.randomTeleport(x, y, z, true);
        } else {
            if(!player.level.isClientSide) {
                player.sendMessage(new TranslatableComponent("forcecraft.ender_rod.dimension.text").withStyle(ChatFormatting.YELLOW), Util.NIL_UUID);
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
        stack.getCapability(CAPABILITY_FORCEROD).ifPresent(cap -> {
            if(cap.hasHealing()) {
                tooltip.add(new TranslatableComponent("item.infuser.tooltip.healing" + cap.getHealingLevel()).withStyle(ChatFormatting.RED));
            }
            if(cap.getSpeedLevel() > 0) {
                tooltip.add(new TranslatableComponent("item.infuser.tooltip.speed" + cap.getSpeedLevel()));
            }
            if(cap.hasCamoModifier()) {
                tooltip.add(new TranslatableComponent("item.infuser.tooltip.camo").withStyle(ChatFormatting.DARK_GREEN));
            }
            if(cap.hasEnderModifier()) {
                tooltip.add(new TranslatableComponent("item.infuser.tooltip.ender").withStyle(ChatFormatting.DARK_PURPLE));

                if(cap.getHomeLocation() != null) {
                    GlobalPos globalPos = cap.getHomeLocation();
                    BlockPos pos = globalPos.pos();
                    tooltip.add(new TranslatableComponent("forcecraft.ender_rod.location", pos.getX(), pos.getY(), pos.getZ(), globalPos.dimension().location()).withStyle(ChatFormatting.YELLOW));
                } else {
                    tooltip.add(new TranslatableComponent("forcecraft.ender_rod.unset").withStyle(ChatFormatting.RED));
                }
                tooltip.add(new TranslatableComponent("forcecraft.ender_rod.text").withStyle(ChatFormatting.GRAY));
            }
            if(cap.hasSightModifier()) {
                tooltip.add(new TranslatableComponent("item.infuser.tooltip.sight").withStyle(ChatFormatting.LIGHT_PURPLE));
            }
            if(cap.hasLight()) {
                tooltip.add(new TranslatableComponent("item.infuser.tooltip.light").withStyle(ChatFormatting.YELLOW));
            }
        });
    }

    @Override
    public CompoundTag serializeNBT() {
        return writeNBT(this);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        readNBT(this, nbt);
    }

    public static CompoundTag writeNBT(IForceRodModifier instance) {
        if (instance == null) {
            return null;
        }
        CompoundTag tag  = new CompoundTag();
        tag.putInt("speed", instance.getSpeedLevel());
        tag.putInt("healing", instance.getHealingLevel());

        if(instance.getHomeLocation() != null) {
            tag.putBoolean("HasHome", true);
            tag.putLong("HomeLocation", instance.getHomeLocation().pos().asLong());
            tag.putString("HomeDimension", instance.getHomeLocation().dimension().location().toString());
        }

        tag.putBoolean("camo", instance.hasCamoModifier());
        tag.putBoolean("ender", instance.hasEnderModifier());
        tag.putBoolean("sight", instance.hasSightModifier());
        tag.putBoolean("light", instance.hasLight());
        return tag;
    }

    public static void readNBT(IForceRodModifier instance, CompoundTag tag) {
        instance.setSpeed(tag.getInt("speed"));
        instance.setHealing(tag.getInt("healing"));

        if(tag.getBoolean("HasHome")) {
            BlockPos pos = BlockPos.of(tag.getLong("HomeLocation"));
            ResourceLocation location = ResourceLocation.tryParse(tag.getString("HomeDimension"));
            if(location != null) {
                ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
                instance.setHomeLocation(GlobalPos.of(dimension, pos));
            }
        }

        instance.setCamoModifier(tag.getBoolean("camo"));
        instance.setEnderModifier(tag.getBoolean("ender"));
        instance.setSightModifier(tag.getBoolean("sight"));
        instance.setLight(tag.getBoolean("light"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_FORCEROD.orEmpty(cap, LazyOptional.of(() -> this));
    }

    public final Capability<IForceRodModifier> getCapability(){
        return CAPABILITY_FORCEROD;
    }
}
