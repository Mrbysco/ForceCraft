package mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ForceRodStorage implements Capability.IStorage<IForceRodModifier> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side) {
        CompoundNBT nbt  = new CompoundNBT();
		nbt.putInt("speed", instance.getSpeedLevel());
        // why isn't this just an integer
        nbt.putBoolean("healingOne", instance.isRodOfHealing(1));	
        nbt.putBoolean("healingTwo", instance.isRodOfHealing(2));
        nbt.putBoolean("healingThree", instance.isRodOfHealing(3));

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

    @Override
    public void readNBT(Capability<IForceRodModifier> capability, IForceRodModifier instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = (CompoundNBT) nbtIn;
			instance.setSpeed(nbt.getInt("speed"));
            instance.setRodOfHealing(nbt.getBoolean("healingOne"), 1);
            instance.setRodOfHealing(nbt.getBoolean("healingTwo"), 2);
            instance.setRodOfHealing(nbt.getBoolean("healingThree"), 3);

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
