package com.mrbysco.forcecraft.entities.projectile;

import com.mrbysco.forcecraft.items.flask.EntityFlaskItem;
import com.mrbysco.forcecraft.registry.ForceEntities;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class FlaskEntity extends ThrowableItemProjectile implements ItemSupplier {
	public FlaskEntity(EntityType<? extends FlaskEntity> typeIn, Level level) {
		super(typeIn, level);
	}

	public FlaskEntity(Level level, LivingEntity livingEntityIn, ItemStack stack) {
		super(ForceEntities.FORCE_FLASK.get(), livingEntityIn, level, stack);
	}

	public FlaskEntity(Level level, double x, double y, double z, ItemStack stack) {
		super(ForceEntities.FORCE_FLASK.get(), x, y, z, level, stack);
	}

	protected Item getDefaultItem() {
		return ForceRegistry.ENTITY_FLASK.get();
	}

	@Override
	protected double getDefaultGravity() {
		return 0.05D;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level().isClientSide()) {
			ItemStack stack = getItem();
			if (stack.getItem() instanceof EntityFlaskItem forceFlask) {
				Entity entity = result.getEntity();
				if (forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.level());
					BlockPos pos = entity.blockPosition();
					storedEntity.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.level().addFreshEntity(storedEntity);

					this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
				} else {
					if (entity.isAlive() && !entity.isInvulnerable() && !(entity instanceof Player) &&
							entity instanceof LivingEntity livingEntity && entity.canTeleport(this.level(), entity.level()) &&
							!forceFlask.isBlacklisted(livingEntity)) {
						ItemStack entityFlask = null;
						if (entity instanceof Bat) {
							entityFlask = new ItemStack(ForceRegistry.BAT_FLASK.get());
						} else if (entity instanceof Bee) {
							entityFlask = new ItemStack(ForceRegistry.BEE_FLASK.get());
						} else if (entity instanceof Cat) {
							entityFlask = new ItemStack(ForceRegistry.CAT_FLASK.get());
						} else if (entity instanceof Chicken) {
							entityFlask = new ItemStack(ForceRegistry.CHICKEN_FLASK.get());
						} else if (entity instanceof CaveSpider) {
							entityFlask = new ItemStack(ForceRegistry.CAVE_SPIDER_FLASK.get());
						} else if (entity instanceof Cod) {
							entityFlask = new ItemStack(ForceRegistry.COD_FLASK.get());
						} else if (entity instanceof Cow && !(entity instanceof MushroomCow)) {
							entityFlask = new ItemStack(ForceRegistry.COW_FLASK.get());
						} else if (entity instanceof Dolphin) {
							entityFlask = new ItemStack(ForceRegistry.DOLPHIN_FLASK.get());
						} else if (entity instanceof Donkey) {
							entityFlask = new ItemStack(ForceRegistry.DONKEY_FLASK.get());
						} else if (entity instanceof EnderMan) {
							entityFlask = new ItemStack(ForceRegistry.ENDERMAN_FLASK.get());
						} else if (entity instanceof Fox) {
							entityFlask = new ItemStack(ForceRegistry.FOX_FLASK.get());
						} else if (entity instanceof Horse) {
							entityFlask = new ItemStack(ForceRegistry.HORSE_FLASK.get());
						} else if (entity instanceof IronGolem) {
							entityFlask = new ItemStack(ForceRegistry.IRON_GOLEM_FLASK.get());
						} else if (entity instanceof Llama) {
							entityFlask = new ItemStack(ForceRegistry.LLAMA_FLASK.get());
						} else if (entity instanceof MushroomCow) {
							entityFlask = new ItemStack(ForceRegistry.MOOSHROOM_FLASK.get());
						} else if (entity instanceof Mule) {
							entityFlask = new ItemStack(ForceRegistry.MULE_FLASK.get());
						} else if (entity instanceof Panda) {
							entityFlask = new ItemStack(ForceRegistry.PANDA_FLASK.get());
						} else if (entity instanceof Parrot) {
							entityFlask = new ItemStack(ForceRegistry.PARROT_FLASK.get());
						} else if (entity instanceof Pig) {
							entityFlask = new ItemStack(ForceRegistry.PIG_FLASK.get());
						} else if (entity instanceof AbstractPiglin) {
							entityFlask = new ItemStack(ForceRegistry.PIGLIN_FLASK.get());
						} else if (entity instanceof PolarBear) {
							entityFlask = new ItemStack(ForceRegistry.POLAR_BEAR_FLASK.get());
						} else if (entity instanceof Pufferfish) {
							entityFlask = new ItemStack(ForceRegistry.PUFFERFISH_FLASK.get());
						} else if (entity instanceof Rabbit) {
							entityFlask = new ItemStack(ForceRegistry.RABBIT_FLASK.get());
						} else if (entity instanceof Salmon) {
							entityFlask = new ItemStack(ForceRegistry.SALMON_FLASK.get());
						} else if (entity instanceof Sheep) {
							entityFlask = new ItemStack(ForceRegistry.SHEEP_FLASK.get());
						} else if (entity instanceof AbstractSkeleton) {
							entityFlask = new ItemStack(ForceRegistry.SKELETON_FLASK.get());
						} else if (entity instanceof SnowGolem) {
							entityFlask = new ItemStack(ForceRegistry.SNOW_GOLEM_FLASK.get());
						} else if (entity instanceof Spider) {
							entityFlask = new ItemStack(ForceRegistry.SPIDER_FLASK.get());
						} else if (entity instanceof Squid) {
							entityFlask = new ItemStack(ForceRegistry.SQUID_FLASK.get());
						} else if (entity instanceof Strider) {
							entityFlask = new ItemStack(ForceRegistry.STRIDER_FLASK.get());
						} else if (entity instanceof TropicalFish) {
							entityFlask = new ItemStack(ForceRegistry.TROPICAL_FISH_FLASK.get());
						} else if (entity instanceof Turtle) {
							entityFlask = new ItemStack(ForceRegistry.TURTLE_FLASK.get());
						} else if (entity instanceof Villager) {
							entityFlask = new ItemStack(ForceRegistry.VILLAGER_FLASK.get());
						} else if (entity instanceof WanderingTrader) {
							entityFlask = new ItemStack(ForceRegistry.WANDERING_TRADER_FLASK.get());
						} else if (entity instanceof Wolf) {
							entityFlask = new ItemStack(ForceRegistry.WOLF_FLASK.get());
						} else if (entity instanceof ZombifiedPiglin) {
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

	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level().isClientSide()) {
			ItemStack stack = this.getItem();
			if (stack.getItem() instanceof EntityFlaskItem forceFlask) {
				if (forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.level());
					BlockPos pos = result.getBlockPos().relative(result.getDirection());
					storedEntity.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.level().addFreshEntity(storedEntity);
				}
				this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
			}
		}
	}

	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!this.level().isClientSide()) {
			this.spawnAtLocation((ServerLevel) this.level(), this.getItem(), 0.5F);

			this.level().broadcastEntityEvent(this, (byte) 3);
			this.discard();
		}
	}
}
