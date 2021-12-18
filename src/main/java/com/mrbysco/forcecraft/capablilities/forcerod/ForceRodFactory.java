package com.mrbysco.forcecraft.capablilities.forcerod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.concurrent.Callable;

public class ForceRodFactory implements Callable<IForceRodModifier> {
    @Override
    public IForceRodModifier call() throws Exception {
        return new IForceRodModifier() {
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

            @Override
            public CompoundTag serializeNBT() {
                return null;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {

            }
        };
    }
}
