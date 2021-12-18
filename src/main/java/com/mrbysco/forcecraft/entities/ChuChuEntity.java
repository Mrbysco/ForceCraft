package com.mrbysco.forcecraft.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class ChuChuEntity extends Slime {

	public ChuChuEntity(EntityType<? extends Slime> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return this.getType().getDefaultLootTable();
	}

	@Override
	protected boolean isDealsDamage() {
		return true;
	}

	@Override
	protected void setSize(int size, boolean resetHealth) {
		this.entityData.set(ID_SIZE, 1);
		this.reapplyPosition();
		this.refreshDimensions();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(size));
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)1));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(((double)1 + random.nextInt(2)));
		if (resetHealth) {
			this.setHealth(this.getMaxHealth());
		}

		this.xpReward = 1;
	}

	public static boolean canSpawnHere(EntityType<ChuChuEntity> p_223366_0_, LevelAccessor p_223366_1_, MobSpawnType reason, BlockPos p_223366_3_, Random randomIn) {
		if (p_223366_1_.getDifficulty() != Difficulty.PEACEFUL) {
			if (Objects.equals(p_223366_1_.getBiomeName(p_223366_3_), Optional.of(Biomes.SWAMP)) && p_223366_3_.getY() > 50 && p_223366_3_.getY() < 70 && randomIn.nextFloat() < 0.5F && randomIn.nextFloat() < p_223366_1_.getMoonBrightness() && p_223366_1_.getMaxLocalRawBrightness(p_223366_3_) <= randomIn.nextInt(8)) {
				return checkMobSpawnRules(p_223366_0_, p_223366_1_, reason, p_223366_3_, randomIn);
			}

			if (!(p_223366_1_ instanceof WorldGenLevel)) {
				return false;
			}

			ChunkPos chunkpos = new ChunkPos(p_223366_3_);
			boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel)p_223366_1_).getSeed(), 987234911L).nextInt(10) == 0;
			if (randomIn.nextInt(10) == 0 && flag && p_223366_3_.getY() < 40) {
				return checkMobSpawnRules(p_223366_0_, p_223366_1_, reason, p_223366_3_, randomIn);
			}
		}

		return false;
	}
}
