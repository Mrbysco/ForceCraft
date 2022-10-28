package com.mrbysco.forcecraft.entities.projectile;

import com.mrbysco.forcecraft.items.flask.EntityFlaskItem;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.fish.CodEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
		value = Dist.CLIENT,
		_interface = IRendersAsItem.class
)
public class FlaskEntity extends ProjectileItemEntity implements IRendersAsItem {
	public FlaskEntity(EntityType<? extends FlaskEntity> typeIn, World worldIn) {
		super(typeIn, worldIn);
	}

	public FlaskEntity(World worldIn, LivingEntity livingEntityIn) {
		super(ForceEntities.FORCE_FLASK.get(), livingEntityIn, worldIn);
	}

	public FlaskEntity(World worldIn, double x, double y, double z) {
		super(ForceEntities.FORCE_FLASK.get(), x, y, z, worldIn);
	}

	public FlaskEntity(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
		this(ForceEntities.FORCE_FLASK.get(), worldIn);
	}

	protected Item getDefaultItem() {
		return ForceRegistry.ENTITY_FLASK.get();
	}

	protected float getGravity() {
		return 0.05F;
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult result) {
		super.onHitEntity(result);
		if (!this.level.isClientSide) {
			ItemStack stack = getItem();
			if (stack.getItem() instanceof EntityFlaskItem) {
				Entity entity = result.getEntity();
				EntityFlaskItem forceFlask = (EntityFlaskItem) stack.getItem();
				if (forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.level);
					BlockPos pos = entity.blockPosition();
					storedEntity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.level.addFreshEntity(storedEntity);

					this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
				} else {
					if (entity.isAlive() && !entity.isInvulnerable() && !(entity instanceof PlayerEntity) && entity instanceof LivingEntity && entity.canChangeDimensions() && !forceFlask.isBlacklisted((LivingEntity) entity)) {
						LivingEntity livingEntity = (LivingEntity) entity;
						ItemStack entityFlask = null;
						if (entity instanceof BatEntity) {
							entityFlask = new ItemStack(ForceRegistry.BAT_FLASK.get());
						} else if (entity instanceof BeeEntity) {
							entityFlask = new ItemStack(ForceRegistry.BEE_FLASK.get());
						} else if (entity instanceof CatEntity) {
							entityFlask = new ItemStack(ForceRegistry.CAT_FLASK.get());
						} else if (entity instanceof ChickenEntity) {
							entityFlask = new ItemStack(ForceRegistry.CHICKEN_FLASK.get());
						} else if (entity instanceof CaveSpiderEntity) {
							entityFlask = new ItemStack(ForceRegistry.CAVE_SPIDER_FLASK.get());
						} else if (entity instanceof CodEntity) {
							entityFlask = new ItemStack(ForceRegistry.COD_FLASK.get());
						} else if (entity instanceof CowEntity) {
							entityFlask = new ItemStack(ForceRegistry.COW_FLASK.get());
						} else if (entity instanceof DolphinEntity) {
							entityFlask = new ItemStack(ForceRegistry.DOLPHIN_FLASK.get());
						} else if (entity instanceof DonkeyEntity) {
							entityFlask = new ItemStack(ForceRegistry.DONKEY_FLASK.get());
						} else if (entity instanceof EndermanEntity) {
							entityFlask = new ItemStack(ForceRegistry.ENDERMAN_FLASK.get());
						} else if (entity instanceof FoxEntity) {
							entityFlask = new ItemStack(ForceRegistry.FOX_FLASK.get());
						} else if (entity instanceof HorseEntity) {
							entityFlask = new ItemStack(ForceRegistry.HORSE_FLASK.get());
						} else if (entity instanceof IronGolemEntity) {
							entityFlask = new ItemStack(ForceRegistry.IRON_GOLEM_FLASK.get());
						} else if (entity instanceof LlamaEntity) {
							entityFlask = new ItemStack(ForceRegistry.LLAMA_FLASK.get());
						} else if (entity instanceof MooshroomEntity) {
							entityFlask = new ItemStack(ForceRegistry.MOOSHROOM_FLASK.get());
						} else if (entity instanceof MuleEntity) {
							entityFlask = new ItemStack(ForceRegistry.MULE_FLASK.get());
						} else if (entity instanceof PandaEntity) {
							entityFlask = new ItemStack(ForceRegistry.PANDA_FLASK.get());
						} else if (entity instanceof ParrotEntity) {
							entityFlask = new ItemStack(ForceRegistry.PARROT_FLASK.get());
						} else if (entity instanceof PigEntity) {
							entityFlask = new ItemStack(ForceRegistry.PIG_FLASK.get());
						} else if (entity instanceof AbstractPiglinEntity) {
							entityFlask = new ItemStack(ForceRegistry.PIGLIN_FLASK.get());
						} else if (entity instanceof PolarBearEntity) {
							entityFlask = new ItemStack(ForceRegistry.POLAR_BEAR_FLASK.get());
						} else if (entity instanceof PufferfishEntity) {
							entityFlask = new ItemStack(ForceRegistry.PUFFERFISH_FLASK.get());
						} else if (entity instanceof RabbitEntity) {
							entityFlask = new ItemStack(ForceRegistry.RABBIT_FLASK.get());
						} else if (entity instanceof SalmonEntity) {
							entityFlask = new ItemStack(ForceRegistry.SALMON_FLASK.get());
						} else if (entity instanceof SheepEntity) {
							entityFlask = new ItemStack(ForceRegistry.SHEEP_FLASK.get());
						} else if (entity instanceof AbstractSkeletonEntity) {
							entityFlask = new ItemStack(ForceRegistry.SKELETON_FLASK.get());
						} else if (entity instanceof SnowGolemEntity) {
							entityFlask = new ItemStack(ForceRegistry.SNOW_GOLEM_FLASK.get());
						} else if (entity instanceof SpiderEntity) {
							entityFlask = new ItemStack(ForceRegistry.SPIDER_FLASK.get());
						} else if (entity instanceof SquidEntity) {
							entityFlask = new ItemStack(ForceRegistry.SQUID_FLASK.get());
						} else if (entity instanceof StriderEntity) {
							entityFlask = new ItemStack(ForceRegistry.STRIDER_FLASK.get());
						} else if (entity instanceof TropicalFishEntity) {
							entityFlask = new ItemStack(ForceRegistry.TROPICAL_FISH_FLASK.get());
						} else if (entity instanceof TurtleEntity) {
							entityFlask = new ItemStack(ForceRegistry.TURTLE_FLASK.get());
						} else if (entity instanceof VillagerEntity) {
							entityFlask = new ItemStack(ForceRegistry.VILLAGER_FLASK.get());
						} else if (entity instanceof WanderingTraderEntity) {
							entityFlask = new ItemStack(ForceRegistry.WANDERING_TRADER_FLASK.get());
						} else if (entity instanceof WolfEntity) {
							entityFlask = new ItemStack(ForceRegistry.WOLF_FLASK.get());
						} else if (entity instanceof ZombifiedPiglinEntity) {
							entityFlask = new ItemStack(ForceRegistry.ZOMBIFIED_PIGLIN_FLASK.get());
						}

						if (entityFlask != null) {
							forceFlask.storeEntity(entityFlask, livingEntity);
							this.setItem(entityFlask);
						} else {
							forceFlask.storeEntity(stack, livingEntity);
							this.setItem(stack);
						}
					} else {
						this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
					}
				}
			}
		}
	}

	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			ItemStack stack = this.getItem();
			if (stack.getItem() instanceof EntityFlaskItem) {
				EntityFlaskItem forceFlask = (EntityFlaskItem) stack.getItem();
				if (forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.level);
					BlockPos pos = result.getBlockPos().relative(result.getDirection());
					storedEntity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.level.addFreshEntity(storedEntity);
				}
				this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
			}
		}
	}

	protected void onHit(RayTraceResult result) {
		super.onHit(result);
		if (!this.level.isClientSide) {
			this.spawnAtLocation(this.getItem(), 0.5F);

			this.level.broadcastEntityEvent(this, (byte) 3);
			this.remove();
		}
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
