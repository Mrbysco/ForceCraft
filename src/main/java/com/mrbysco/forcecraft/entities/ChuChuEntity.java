package com.mrbysco.forcecraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
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
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @Nullable SpawnGroupData spawnGroupData) {
		SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
		int newSize = 1 + random.nextInt(2);
		this.setSize(newSize, true);
		return data;
	}

	public static boolean canSpawnHere(EntityType<ChuChuEntity> chuEntityEntityType, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
		if (level.getDifficulty() != Difficulty.PEACEFUL) {

			if (level.getBiome(pos).is(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) && pos.getY() > 50 && pos.getY() < 70) {
				float surfaceSlimeSpawnChance = level.environmentAttributes().getValue(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, pos);
				if (random.nextFloat() < surfaceSlimeSpawnChance && level.getMaxLocalRawBrightness(pos) <= random.nextInt(8)) {
					return checkMobSpawnRules(chuEntityEntityType, level, spawnReason, pos, random);
				}
			}

			if (!(level instanceof WorldGenLevel)) {
				return false;
			}

			ChunkPos chunkPos = ChunkPos.containing(pos);
			boolean flag = WorldgenRandom.seedSlimeChunk(chunkPos.x(), chunkPos.z(), ((WorldGenLevel) level).getSeed(), 987234911L).nextInt(10) == 0;
			if (random.nextInt(10) == 0 && flag && pos.getY() < 40) {
				return checkMobSpawnRules(chuEntityEntityType, level, spawnReason, pos, random);
			}
		}

		return false;
	}
}
