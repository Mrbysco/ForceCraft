package com.mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;

public class ForceRodStorage implements Capability.IStorage<IForceRodModifier> {
    public ForceRodStorage() {
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


    @Nullable
    @Override
    public Tag writeNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side) {
        CompoundTag nbt = serializeNBT(instance);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side, Tag nbtIn) {
        deserializeNBT(instance, nbtIn);
    }

    public static CompoundTag serializeNBT(IForceRodModifier instance) {
        if (instance == null) {
            return null;
        }
        CompoundTag nbt  = new CompoundTag();
        nbt.putInt("speed", instance.getSpeedLevel());
        nbt.putInt("healing", instance.getHealingLevel());

        if(instance.getHomeLocation() != null) {
            nbt.putBoolean("HasHome", true);
            nbt.putLong("HomeLocation", instance.getHomeLocation().pos().asLong());
            nbt.putString("HomeDimension", instance.getHomeLocation().dimension().location().toString());
        }

        nbt.putBoolean("camo", instance.hasCamoModifier());
        nbt.putBoolean("ender", instance.hasEnderModifier());
        nbt.putBoolean("sight", instance.hasSightModifier());
        nbt.putBoolean("light", instance.hasLight());
        return nbt;
    }

    public static void deserializeNBT(IForceRodModifier instance, Tag nbtIn) {
        if (nbtIn instanceof CompoundTag nbt) {
            instance.setSpeed(nbt.getInt("speed"));
            instance.setHealing(nbt.getInt("healing"));

            if(nbt.getBoolean("HasHome")) {
                BlockPos pos = BlockPos.of(nbt.getLong("HomeLocation"));
                ResourceLocation location = ResourceLocation.tryParse(nbt.getString("HomeDimension"));
                if(location != null) {
                    ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
                    instance.setHomeLocation(GlobalPos.of(dimension, pos));
                }
            }

            instance.setCamoModifier(nbt.getBoolean("camo"));
            instance.setEnderModifier(nbt.getBoolean("ender"));
            instance.setSightModifier(nbt.getBoolean("sight"));
            instance.setLight(nbt.getBoolean("light"));
        }
    }
}
