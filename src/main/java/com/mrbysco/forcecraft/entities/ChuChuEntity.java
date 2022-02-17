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

	public ChuChuEntity(EntityType<? extends Slime> type, Level level) {
		super(type, level);
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
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(((double)1 + random.nextInt(2)));
		if (resetHealth) {
			this.setHealth(this.getMaxHealth());
		}

		this.xpReward = 1;
	}

	public static boolean canSpawnHere(EntityType<ChuChuEntity> chuEntityEntityType, LevelAccessor level, MobSpawnType reason, BlockPos pos, Random randomIn) {
		if (level.getDifficulty() != Difficulty.PEACEFUL) {
			if (Objects.equals(level.getBiomeName(pos), Optional.of(Biomes.SWAMP)) && pos.getY() > 50 && pos.getY() < 70 && randomIn.nextFloat() < 0.5F && randomIn.nextFloat() < level.getMoonBrightness() && level.getMaxLocalRawBrightness(pos) <= randomIn.nextInt(8)) {
				return checkMobSpawnRules(chuEntityEntityType, level, reason, pos, randomIn);
			}

			if (!(level instanceof WorldGenLevel)) {
				return false;
			}

			ChunkPos chunkpos = new ChunkPos(pos);
			boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel)level).getSeed(), 987234911L).nextInt(10) == 0;
			if (randomIn.nextInt(10) == 0 && flag && pos.getY() < 40) {
				return checkMobSpawnRules(chuEntityEntityType, level, reason, pos, randomIn);
			}
		}

		return false;
	}
}
