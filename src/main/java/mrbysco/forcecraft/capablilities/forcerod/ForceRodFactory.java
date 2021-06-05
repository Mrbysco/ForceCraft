package mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.concurrent.Callable;

public class ForceRodFactory implements Callable<IForceRodModifier> {
    @Override
    public IForceRodModifier call() throws Exception {
        return new IForceRodModifier() {

            boolean[] rodOfHealing = {false, false, false};
            boolean camo = false;
            boolean ender = false;
            boolean sight = false;
            int speed = 0;

            GlobalPos homeLocation = null;

            @Override
            public boolean isRodOfHealing(int level) {
                return rodOfHealing[level - 1];
            }

            @Override
            public void setRodOfHealing(boolean newVal, int level) {
                rodOfHealing[level - 1] = newVal;
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
            public void teleportPlayerToLocation(PlayerEntity player, GlobalPos globalPos) {
                if(player.world.getDimensionKey().getLocation().equals(globalPos.getDimension().getLocation())) {
                    BlockPos pos = globalPos.getPos();
                    int x = pos.getX();
                    int y = pos.getY() + 1;
                    int z = pos.getZ();

                    player.attemptTeleport(x, y, z, true);
                } else {
                    if(!player.world.isRemote) {
                        player.sendMessage(new TranslationTextComponent("forcecraft.ender_rod.dimension.text").mergeStyle(TextFormatting.YELLOW), Util.DUMMY_UUID);
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
            public CompoundNBT serializeNBT() {
                return null;
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt) {

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
        };
    }
}
