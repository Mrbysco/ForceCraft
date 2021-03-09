package mrbysco.forcecraft.capablilities.toolmodifier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ToolModStorage implements Capability.IStorage<IToolModifier> {

    public ToolModStorage(){
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IToolModifier> capability, IToolModifier instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        //Speed
        nbt.putInt("speed", instance.getSpeedLevel());
        //Heat
        nbt.putBoolean("heat", instance.hasHeat());
        //Force
        nbt.putInt("force", instance.getForceLevel());
        //Silk
        nbt.putBoolean("silk", instance.hasSilk());
        //Sharpness
        nbt.putInt("sharp", instance.getSpeedLevel());
        //Luck
        nbt.putInt("luck", instance.getLuckLevel());
        //Sturdy
        nbt.putInt("sturdy", instance.getSturdyLevel());
        //Rainbow
        nbt.putBoolean("rainbow", instance.hasRainbow());
        //Lumberjack
        nbt.putBoolean("lumber", instance.hasLumberjack());
        //Bleeding
        nbt.putInt("bleed", instance.getBleedLevel());
        //Bane
        nbt.putInt("bane", instance.getBaneLevel());
        //Wing
        nbt.putBoolean("wing", instance.hasWing());
        //Camo
        nbt.putBoolean("camo", instance.hasCamo());
        //Sight
        nbt.putBoolean("sight", instance.hasSight());
        //Light
        nbt.putBoolean("light", instance.hasLight());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IToolModifier> capability, IToolModifier instance, Direction side, INBT nbtIn) {
        if(nbtIn instanceof CompoundNBT){
            CompoundNBT nbt = (CompoundNBT) nbtIn;
            instance.setSpeed(nbt.getInt("speed"));
            instance.setHeat(nbt.getBoolean("heat"));
            instance.setForce(nbt.getInt("force"));
            instance.setSpeed(nbt.getInt("silk"));
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
        }
    }


}
