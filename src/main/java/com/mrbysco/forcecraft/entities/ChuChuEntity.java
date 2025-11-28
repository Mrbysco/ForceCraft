package com.mrbysco.forcecraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.jetbrains.annotations.Nullable;

public class ChuChuEntity extends Slime {

	public ChuChuEntity(EntityType<? extends Slime> type, Level level) {
		super(type, level);
	}

	@Override
	protected boolean isDealsDamage() {
		return true;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
		SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
		int newSize = 1 + random.nextInt(2);
		this.setSize(newSize, true);
		return data;
	}

	public static boolean canSpawnHere(EntityType<ChuChuEntity> chuEntityEntityType, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
		if (level.getDifficulty() != Difficulty.PEACEFUL) {
			if (level.getBiome(pos).is(Biomes.SWAMP) && pos.getY() > 50 && pos.getY() < 70 && randomIn.nextFloat() < 0.5F && randomIn.nextFloat() < level.getMoonBrightness() && level.getMaxLocalRawBrightness(pos) <= randomIn.nextInt(8)) {
				return checkMobSpawnRules(chuEntityEntityType, level, reason, pos, randomIn);
			}

			if (!(level instanceof WorldGenLevel)) {
				return false;
			}

			ChunkPos chunkpos = new ChunkPos(pos);
			boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel) level).getSeed(), 987234911L).nextInt(10) == 0;
			if (randomIn.nextInt(10) == 0 && flag && pos.getY() < 40) {
				return checkMobSpawnRules(chuEntityEntityType, level, reason, pos, randomIn);
			}
		}

		return false;
	}
}
