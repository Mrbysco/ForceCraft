package com.mrbysco.forcecraft.entity;

import com.mrbysco.forcecraft.registry.ForceAttachments;
import com.mrbysco.forcecraft.registry.ForceDatamaps;
import com.mrbysco.forcecraft.registry.ForceEffects;
import com.mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

public class ForceArrow extends Arrow {
	private static final EntityDataAccessor<ItemStack> TORCH = SynchedEntityData.defineId(ForceArrow.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Boolean> ICE = SynchedEntityData.defineId(ForceArrow.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> BANE = SynchedEntityData.defineId(ForceArrow.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> HEAT = SynchedEntityData.defineId(ForceArrow.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> BLEED = SynchedEntityData.defineId(ForceArrow.class, EntityDataSerializers.INT);

	private static final int EXPIRE_TICKS = 3 * 20;
	private int expireTicks = -1;
	private boolean waterConverted;

	public ForceArrow(EntityType<? extends Arrow> type, Level level) {
		super(type, level);
		this.pickup = AbstractArrow.Pickup.DISALLOWED;
	}

	public ForceArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(level, x, y, z, pickupItemStack, firedFromWeapon);
		this.pickup = AbstractArrow.Pickup.DISALLOWED;
	}

	public ForceArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(level, owner, pickupItemStack, firedFromWeapon);
		this.pickup = AbstractArrow.Pickup.DISALLOWED;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(TORCH, ItemStack.EMPTY);
		entityData.define(ICE, false);
		entityData.define(BANE, false);
		entityData.define(HEAT, false);
		entityData.define(BLEED, 0);
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.FORCE_ARROW.get();
	}

	@Override
	public void handleEntityEvent(byte id) {
		super.handleEntityEvent(id);
		if (id == 3 && isIce()) {
			ParticleOptions particle = ParticleTypes.SNOWFLAKE;

			for (int i = 0; i < 8; i++) {
				this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
			}
		}
	}

	public void setTorch(ItemStack torch) {
		this.entityData.set(TORCH, torch);
	}

	public ItemStack getTorch() {
		return this.entityData.get(TORCH);
	}

	public void setIce(boolean ice) {
		this.entityData.set(ICE, ice);
	}

	public boolean isIce() {
		return this.entityData.get(ICE);
	}

	public void setBane(boolean bane) {
		this.entityData.set(BANE, bane);
	}

	public boolean isBane() {
		return this.entityData.get(BANE);
	}

	public void setBleed(int level) {
		this.entityData.set(BLEED, level);
	}

	public int getBleed() {
		return this.entityData.get(BLEED);
	}

	public void setHeat(boolean b) {
		this.entityData.set(HEAT, b);
	}

	public boolean isHeat() {
		return this.entityData.get(HEAT);
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level().isClientSide()) {
			if (expireTicks >= 0 && --expireTicks < 0) {
				this.discard();
				return;
			}
		}

		if (!this.level().isClientSide() && this.isInWater() && isIce() && !waterConverted) {
			waterConverted = true;
			handleIceConversion(this.blockPosition());
		}
	}

	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		if (expireTicks < 0) {
			expireTicks = EXPIRE_TICKS; // 6 seconds
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (this.level() instanceof ServerLevel serverLevel && !getTorch().isEmpty()) {
			Block.popResource(serverLevel, hitResult.getEntity().blockPosition(), getTorch().copy());
			setTorch(ItemStack.EMPTY);
		}
	}

	@Override
	protected void doPostHurtEffects(LivingEntity mob) {
		super.doPostHurtEffects(mob);

		if (isBane()) {
			if (mob instanceof Creeper || mob instanceof EnderMan) {
				mob.setData(ForceAttachments.BANE, true);

				if (mob instanceof Creeper creeper) {
					creeper.setSwellDir(-1);
					creeper.getEntityData().set(Creeper.DATA_IS_IGNITED, false);
					creeper.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof SwellGoal);
				}
			}
		}

		if (isIce()) {
			mob.addEffect(new MobEffectInstance(ForceEffects.FROZEN, 100, 0), this.getOwner());
		}

		if (getBleed() > 0) {
			mob.addEffect(new MobEffectInstance(ForceEffects.BLEEDING, 100, getBleed()), this.getOwner());
		}

		if (isHeat()) {
			mob.igniteForSeconds(LevelBasedValue.perLevel(4.0F).calculate(1));
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		if (!(this.level() instanceof ServerLevel serverLevel)) return;

		ItemStack torch = getTorch();
		if (torch.isEmpty()) return;

		BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
		boolean placed = false;
		if (torch.getItem() instanceof BlockItem blockItem) {
			DirectionalPlaceContext context = new DirectionalPlaceContext(
					serverLevel, pos, hitResult.getDirection().getOpposite(),
					torch.copy(), hitResult.getDirection());
			placed = blockItem.place(context).consumesAction();
		}
		if (!placed) {
			Block.popResource(serverLevel, pos, torch.copy());
		}
		setTorch(ItemStack.EMPTY);
	}

	@Override
	protected void hitBlockEnchantmentEffects(ServerLevel serverLevel, BlockHitResult hitResult, ItemStack weapon) {
		super.hitBlockEnchantmentEffects(serverLevel, hitResult, weapon);
		handleIceConversion(hitResult.getBlockPos());
		handleHeat(hitResult.getBlockPos(), hitResult.getDirection());
	}

	private void handleIceConversion(BlockPos hitPos) {
		if (!isIce()) return;
		if (!(this.level() instanceof ServerLevel serverLevel)) return;

		for (BlockPos target : BlockPos.betweenClosed(
				hitPos.offset(-1, -1, -1),
				hitPos.offset(1, 1, 1))) {
			BlockState state = level().getBlockState(target);
			Block block = state.getBlock();
			Block conversionBlock = block.builtInRegistryHolder().getData(ForceDatamaps.ICE_CONVERSIONS);
			if (conversionBlock != null) {
				level().setBlock(target, conversionBlock.defaultBlockState(), 3);
				// Ice particles
			}
			serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
					target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5,
					8, 0.25, 0.25, 0.25, 0.0);
		}
	}

	private void handleHeat(BlockPos hitPos, Direction direction) {
		if (!isHeat()) return;
		if (!(this.level() instanceof ServerLevel serverLevel)) return;

		hitPos = hitPos.above();
		for (BlockPos target : BlockPos.betweenClosed(
				hitPos.offset(-1, 0, -1),
				hitPos.offset(1, 0, 1))) {
			if (BaseFireBlock.canBePlacedAt(serverLevel, target, direction)) {
				serverLevel.playSound(null, target, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F,
						serverLevel.getRandom().nextFloat() * 0.4F + 0.8F);
				BlockState fireState = BaseFireBlock.getState(serverLevel, target);
				serverLevel.setBlock(target, fireState, 11);
				serverLevel.gameEvent(this, GameEvent.BLOCK_PLACE, target);
			}
		}
	}
}
