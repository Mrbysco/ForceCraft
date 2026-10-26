package com.mrbysco.forcecraft.entity;

import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class Bomb extends Entity implements TraceableEntity {
	private static final EntityDataAccessor<Boolean> SUPER = SynchedEntityData.defineId(Bomb.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(Bomb.class, EntityDataSerializers.INT);
	private static final int DEFAULT_FUSE_TIME = 100;
	private static final float DEFAULT_EXPLOSION_POWER = 4.0F;
	private @Nullable EntityReference<LivingEntity> owner;
	private float explosionPower = 4.0F;

	private static final ExplosionDamageCalculator IGNORE_LIQUID_EXPLOSION_CALCULATOR = new ExplosionDamageCalculator() {
		@Override
		public Optional<Float> getBlockExplosionResistance(
				Explosion explosion,
				BlockGetter level,
				BlockPos pos,
				BlockState blockState,
				FluidState fluidState
		) {
			if (blockState.isAir()) {
				return Optional.empty();
			}

			return Optional.of(blockState.getBlock().getExplosionResistance());
		}
	};

	public Bomb(EntityType<? extends Bomb> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public Bomb(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
		this(ForceEntities.BOMB.get(), level);
		this.setPos(x, y, z);
		this.setDeltaMovement(0, 0, 0);
		this.setFuse(DEFAULT_FUSE_TIME);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.owner = EntityReference.of(owner);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(SUPER, false);
		builder.define(FUSE, DEFAULT_FUSE_TIME);
	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	protected double getDefaultGravity() {
		return 0.04;
	}

	@Override
	public void tick() {
		this.handlePortal();
		this.applyGravity();
		this.move(MoverType.SELF, this.getDeltaMovement());
		this.applyEffectsFromBlocks();
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
		}
		if (this.isInWaterOrRain()) {
			this.discard();
		}

		int fuse = this.getFuse() - 1;
		this.setFuse(fuse);
		if (fuse <= 0 || this.isEyeInFluid(FluidTags.LAVA)) {
			this.discard();
			if (!this.level().isClientSide()) {
				this.explode();
			}
		} else {
			this.updateFluidInteraction();
			if (this.level().isClientSide()) {
				this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
			}
		}
	}

	protected void explode() {
		if (this.level() instanceof ServerLevel level && level.getGameRules().get(GameRules.TNT_EXPLODES)) {
			this.level()
					.explode(
							this,
							Explosion.getDefaultDamageSource(this.level(), this),
							IGNORE_LIQUID_EXPLOSION_CALCULATOR,
							this.getX(),
							this.getY(0.0625),
							this.getZ(),
							this.explosionPower,
							false,
							Level.ExplosionInteraction.TNT
					);
		}
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putInt("fuse", this.getFuse());
		if (this.explosionPower != DEFAULT_EXPLOSION_POWER) {
			output.putFloat("explosion_power", this.explosionPower);
		}
		EntityReference.store(this.owner, output, "owner");
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setFuse(input.getIntOr("fuse", DEFAULT_FUSE_TIME));
		this.explosionPower = Mth.clamp(input.getFloatOr("explosion_power", DEFAULT_EXPLOSION_POWER), 0.0F, 128.0F);
		this.owner = EntityReference.read(input, "owner");
	}

	public @Nullable LivingEntity getOwner() {
		return EntityReference.getLivingEntity(this.owner, this.level());
	}

	@Override
	public void restoreFrom(Entity oldEntity) {
		super.restoreFrom(oldEntity);
		if (oldEntity instanceof Bomb bomb) {
			this.owner = bomb.owner;
		}
	}

	public void setFuse(int time) {
		this.entityData.set(FUSE, time);
	}

	public int getFuse() {
		return this.entityData.get(FUSE);
	}

	public void setSuper(boolean isSuper) {
		this.entityData.set(SUPER, isSuper);
		if (isSuper) {
			this.explosionPower = 30.0F;
			this.setFuse(300);
		} else {
			this.explosionPower = DEFAULT_EXPLOSION_POWER;
			this.setFuse(DEFAULT_FUSE_TIME);
		}
	}

	public boolean isSuper() {
		return this.entityData.get(SUPER);
	}

	@Override
	public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		return false;
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}
}
