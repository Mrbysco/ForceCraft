package mrbysco.forcecraft.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class ChuChuEntity extends SlimeEntity {

	public ChuChuEntity(EntityType<? extends SlimeEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public EntityType<? extends SlimeEntity> getType() {
		return super.getType();
	}

	@Override
	protected ResourceLocation getLootTable() {
		return this.getType().getLootTable();
	}

	@Override
	protected boolean canDamagePlayer() {
		return true;
	}

	@Override
	protected void setSlimeSize(int size, boolean resetHealth) {
		this.dataManager.set(SLIME_SIZE, 1);
		this.recenterBoundingBox();
		this.recalculateSize();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(size));
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)1));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(((double)1 + rand.nextInt(2)));
		if (resetHealth) {
			this.setHealth(this.getMaxHealth());
		}

		this.experienceValue = 1;
	}

	public static boolean canSpawnHere(EntityType<ChuChuEntity> p_223366_0_, IWorld p_223366_1_, SpawnReason reason, BlockPos p_223366_3_, Random randomIn) {
		if (p_223366_1_.getDifficulty() != Difficulty.PEACEFUL) {
			if (Objects.equals(p_223366_1_.func_242406_i(p_223366_3_), Optional.of(Biomes.SWAMP)) && p_223366_3_.getY() > 50 && p_223366_3_.getY() < 70 && randomIn.nextFloat() < 0.5F && randomIn.nextFloat() < p_223366_1_.getMoonFactor() && p_223366_1_.getLight(p_223366_3_) <= randomIn.nextInt(8)) {
				return canSpawnOn(p_223366_0_, p_223366_1_, reason, p_223366_3_, randomIn);
			}

			if (!(p_223366_1_ instanceof ISeedReader)) {
				return false;
			}

			ChunkPos chunkpos = new ChunkPos(p_223366_3_);
			boolean flag = SharedSeedRandom.createSlimeChunkSpawningSeed(chunkpos.x, chunkpos.z, ((ISeedReader)p_223366_1_).getSeed(), 987234911L).nextInt(10) == 0;
			if (randomIn.nextInt(10) == 0 && flag && p_223366_3_.getY() < 40) {
				return canSpawnOn(p_223366_0_, p_223366_1_, reason, p_223366_3_, randomIn);
			}
		}

		return false;
	}
}
