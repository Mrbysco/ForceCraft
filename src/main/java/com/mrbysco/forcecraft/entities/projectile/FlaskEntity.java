package com.mrbysco.forcecraft.entities.projectile;

import com.mrbysco.forcecraft.items.flask.EntityFlaskItem;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;

@OnlyIn(
		value = Dist.CLIENT,
		_interface = ItemSupplier.class
)
public class FlaskEntity extends ThrowableItemProjectile implements ItemSupplier {
	public FlaskEntity(EntityType<? extends FlaskEntity> typeIn, Level worldIn) {
		super(typeIn, worldIn);
	}

	public FlaskEntity(Level worldIn, LivingEntity livingEntityIn) {
		super(ForceEntities.FORCE_FLASK.get(), livingEntityIn, worldIn);
	}

	public FlaskEntity(Level worldIn, double x, double y, double z) {
		super(ForceEntities.FORCE_FLASK.get(), x, y, z, worldIn);
	}

	public FlaskEntity(SpawnEntity spawnEntity, Level worldIn) {
		this(ForceEntities.FORCE_FLASK.get(), worldIn);
	}

	protected Item getDefaultItem() {
		return ForceRegistry.ENTITY_FLASK.get();
	}

	protected float getGravity() {
		return 0.05F;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level.isClientSide) {
			ItemStack stack = getItem();
			if(stack.getItem() instanceof EntityFlaskItem forceFlask) {
				Entity entity = result.getEntity();
				if(forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.level);
					BlockPos pos = entity.blockPosition();
					storedEntity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.level.addFreshEntity(storedEntity);

					this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
				} else {
					if(entity.isAlive() && !entity.isInvulnerable() && !(entity instanceof Player) && entity instanceof LivingEntity livingEntity && entity.canChangeDimensions() && !forceFlask.isBlacklisted((LivingEntity)entity)) {
						ItemStack entityFlask = null;
						if(entity instanceof Bat) {
							entityFlask = new ItemStack(ForceRegistry.BAT_FLASK.get());
						} else if(entity instanceof Bee) {
							entityFlask = new ItemStack(ForceRegistry.BEE_FLASK.get());
						} else if(entity instanceof Cat) {
							entityFlask = new ItemStack(ForceRegistry.CAT_FLASK.get());
						} else if(entity instanceof Chicken) {
							entityFlask = new ItemStack(ForceRegistry.CHICKEN_FLASK.get());
						} else if(entity instanceof CaveSpider) {
							entityFlask = new ItemStack(ForceRegistry.CAVE_SPIDER_FLASK.get());
						} else if(entity instanceof Cod) {
							entityFlask = new ItemStack(ForceRegistry.COD_FLASK.get());
						} else if(entity instanceof Cow) {
							entityFlask = new ItemStack(ForceRegistry.COW_FLASK.get());
						} else if(entity instanceof Dolphin) {
							entityFlask = new ItemStack(ForceRegistry.DOLPHIN_FLASK.get());
						} else if(entity instanceof Donkey) {
							entityFlask = new ItemStack(ForceRegistry.DONKEY_FLASK.get());
						} else if(entity instanceof EnderMan) {
							entityFlask = new ItemStack(ForceRegistry.ENDERMAN_FLASK.get());
						} else if(entity instanceof Fox) {
							entityFlask = new ItemStack(ForceRegistry.FOX_FLASK.get());
						} else if(entity instanceof Horse) {
							entityFlask = new ItemStack(ForceRegistry.HORSE_FLASK.get());
						} else if(entity instanceof IronGolem) {
							entityFlask = new ItemStack(ForceRegistry.IRON_GOLEM_FLASK.get());
						} else if(entity instanceof Llama) {
							entityFlask = new ItemStack(ForceRegistry.LLAMA_FLASK.get());
						} else if(entity instanceof MushroomCow) {
							entityFlask = new ItemStack(ForceRegistry.MOOSHROOM_FLASK.get());
						} else if(entity instanceof Mule) {
							entityFlask = new ItemStack(ForceRegistry.MULE_FLASK.get());
						} else if(entity instanceof Panda) {
							entityFlask = new ItemStack(ForceRegistry.PANDA_FLASK.get());
						} else if(entity instanceof Parrot) {
							entityFlask = new ItemStack(ForceRegistry.PARROT_FLASK.get());
						} else if(entity instanceof Pig) {
							entityFlask = new ItemStack(ForceRegistry.PIG_FLASK.get());
						} else if(entity instanceof AbstractPiglin) {
							entityFlask = new ItemStack(ForceRegistry.PIGLIN_FLASK.get());
						} else if(entity instanceof PolarBear) {
							entityFlask = new ItemStack(ForceRegistry.POLAR_BEAR_FLASK.get());
						} else if(entity instanceof Pufferfish) {
							entityFlask = new ItemStack(ForceRegistry.PUFFERFISH_FLASK.get());
						} else if(entity instanceof Rabbit) {
							entityFlask = new ItemStack(ForceRegistry.RABBIT_FLASK.get());
						} else if(entity instanceof Salmon) {
							entityFlask = new ItemStack(ForceRegistry.SALMON_FLASK.get());
						} else if(entity instanceof Sheep) {
							entityFlask = new ItemStack(ForceRegistry.SHEEP_FLASK.get());
						} else if(entity instanceof AbstractSkeleton) {
							entityFlask = new ItemStack(ForceRegistry.SKELETON_FLASK.get());
						} else if(entity instanceof SnowGolem) {
							entityFlask = new ItemStack(ForceRegistry.SNOW_GOLEM_FLASK.get());
						} else if(entity instanceof Spider) {
							entityFlask = new ItemStack(ForceRegistry.SPIDER_FLASK.get());
						} else if(entity instanceof Squid) {
							entityFlask = new ItemStack(ForceRegistry.SQUID_FLASK.get());
						} else if(entity instanceof Strider) {
							entityFlask = new ItemStack(ForceRegistry.STRIDER_FLASK.get());
						} else if(entity instanceof TropicalFish) {
							entityFlask = new ItemStack(ForceRegistry.TROPICAL_FISH_FLASK.get());
						} else if(entity instanceof Turtle) {
							entityFlask = new ItemStack(ForceRegistry.TURTLE_FLASK.get());
						} else if(entity instanceof Villager) {
							entityFlask = new ItemStack(ForceRegistry.VILLAGER_FLASK.get());
						} else if(entity instanceof WanderingTrader) {
							entityFlask = new ItemStack(ForceRegistry.WANDERING_TRADER_FLASK.get());
						} else if(entity instanceof Wolf) {
							entityFlask = new ItemStack(ForceRegistry.WOLF_FLASK.get());
						} else if(entity instanceof ZombifiedPiglin) {
							entityFlask = new ItemStack(ForceRegistry.ZOMBIFIED_PIGLIN_FLASK.get());
						}

						if(entityFlask != null) {
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

	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			ItemStack stack = this.getItem();
			if(stack.getItem() instanceof EntityFlaskItem forceFlask) {
				if(forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.level);
					BlockPos pos = result.getBlockPos().relative(result.getDirection());
					storedEntity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.level.addFreshEntity(storedEntity);
				}
				this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
			}
		}
	}

	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!this.level.isClientSide) {
			this.spawnAtLocation(this.getItem(), 0.5F);

			this.level.broadcastEntityEvent(this, (byte)3);
			this.discard();
		}
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
