package mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;

public class ForceRodStorage implements Capability.IStorage<IForceRodModifier> {
    public ForceRodStorage() {
    }

    public static void attachInformation(ItemStack stack, List<ITextComponent> tooltip) {
        stack.getCapability(CAPABILITY_FORCEROD).ifPresent(cap -> {
            if(cap.hasHealing()) {
                tooltip.add(new TranslationTextComponent("item.infuser.tooltip.healing" + cap.getHealingLevel()).mergeStyle(TextFormatting.RED));
            }
            if(cap.getSpeedLevel() > 0) {
                tooltip.add(new TranslationTextComponent("item.infuser.tooltip.speed" + cap.getSpeedLevel()));
            }
            if(cap.hasCamoModifier()) {
                tooltip.add(new TranslationTextComponent("item.infuser.tooltip.camo").mergeStyle(TextFormatting.DARK_GREEN));
            }
            if(cap.hasEnderModifier()) {
                tooltip.add(new TranslationTextComponent("item.infuser.tooltip.ender").mergeStyle(TextFormatting.DARK_PURPLE));

                if(cap.getHomeLocation() != null) {
                    GlobalPos globalPos = cap.getHomeLocation();
                    BlockPos pos = globalPos.getPos();
                    tooltip.add(new TranslationTextComponent("forcecraft.ender_rod.location", pos.getX(), pos.getY(), pos.getZ(), globalPos.getDimension().getLocation()).mergeStyle(TextFormatting.YELLOW));
                } else {
                    tooltip.add(new TranslationTextComponent("forcecraft.ender_rod.unset").mergeStyle(TextFormatting.RED));
                }
                tooltip.add(new TranslationTextComponent("forcecraft.ender_rod.text").mergeStyle(TextFormatting.GRAY));
            }
            if(cap.hasSightModifier()) {
                tooltip.add(new TranslationTextComponent("item.infuser.tooltip.sight").mergeStyle(TextFormatting.LIGHT_PURPLE));
            }
            if(cap.hasLight()) {
                tooltip.add(new TranslationTextComponent("item.infuser.tooltip.light").mergeStyle(TextFormatting.YELLOW));
            }
        });
    }


    @Nullable
    @Override
    public INBT writeNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side) {
        CompoundNBT nbt = serializeNBT(instance);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side, INBT nbtIn) {
        deserializeNBT(instance, nbtIn);
    }

    public static CompoundNBT serializeNBT(IForceRodModifier instance) {
        if (instance == null) {
            return null;
        }
        CompoundNBT nbt  = new CompoundNBT();
        nbt.putInt("speed", instance.getSpeedLevel());
        nbt.putInt("healing", instance.getHealingLevel());

        if(instance.getHomeLocation() != null) {
            nbt.putBoolean("HasHome", true);
            nbt.putLong("HomeLocation", instance.getHomeLocation().getPos().toLong());
            nbt.putString("HomeDimension", instance.getHomeLocation().getDimension().getLocation().toString());
        }

        nbt.putBoolean("camo", instance.hasCamoModifier());
        nbt.putBoolean("ender", instance.hasEnderModifier());
        nbt.putBoolean("sight", instance.hasSightModifier());
        nbt.putBoolean("light", instance.hasLight());
        return nbt;
    }

    public static void deserializeNBT(IForceRodModifier instance, INBT nbtIn) {
        if (nbtIn instanceof CompoundNBT) {
            CompoundNBT nbt = (CompoundNBT) nbtIn;
            instance.setSpeed(nbt.getInt("speed"));
            instance.setHealing(nbt.getInt("healing"));

            if(nbt.getBoolean("HasHome")) {
                BlockPos pos = BlockPos.fromLong(nbt.getLong("HomeLocation"));
                ResourceLocation location = ResourceLocation.tryCreate(nbt.getString("HomeDimension"));
                if(location != null) {
                    RegistryKey<World> dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, location);
                    instance.setHomeLocation(GlobalPos.getPosition(dimension, pos));
                }
            }

            instance.setCamoModifier(nbt.getBoolean("camo"));
            instance.setEnderModifier(nbt.getBoolean("ender"));
            instance.setSightModifier(nbt.getBoolean("sight"));
            instance.setLight(nbt.getBoolean("light"));
        }
    }
}
