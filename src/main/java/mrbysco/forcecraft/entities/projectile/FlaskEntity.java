package mrbysco.forcecraft.entities.projectile;

import mrbysco.forcecraft.items.flask.EntityFlaskItem;
import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PigEntity;
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

	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult result) {
		super.onEntityHit(result);
		if (!this.world.isRemote) {
			ItemStack stack = getItem();
			if(stack.getItem() instanceof EntityFlaskItem) {
				Entity entity = result.getEntity();
				EntityFlaskItem forceFlask = (EntityFlaskItem) stack.getItem();
				if(forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.world);
					BlockPos pos = entity.getPosition();
					storedEntity.setPositionAndRotation(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.world.addEntity(storedEntity);

					this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
				} else {
					if(entity.isAlive() && !(entity instanceof PlayerEntity) && entity instanceof LivingEntity && entity.canChangeDimension() && !forceFlask.isBlacklisted((LivingEntity)entity)) {
						LivingEntity livingEntity = (LivingEntity)entity;
						if(entity instanceof ChickenEntity) {
							ItemStack chickenFlask = new ItemStack(ForceRegistry.CHICKEN_FLASK.get());
							forceFlask.storeEntity(chickenFlask, livingEntity);
							this.setItem(chickenFlask);
						} else if(entity instanceof PigEntity) {
							ItemStack pigFlask = new ItemStack(ForceRegistry.PIG_FLASK.get());
							forceFlask.storeEntity(pigFlask, livingEntity);
							this.setItem(pigFlask);
						} else if(entity instanceof AbstractSkeletonEntity) {
							ItemStack skeletonFlask = new ItemStack(ForceRegistry.SKELETON_FLASK.get());
							forceFlask.storeEntity(skeletonFlask, livingEntity);
							this.setItem(skeletonFlask);
						} else {
							forceFlask.storeEntity(stack, livingEntity);
						}
					}
				}
			}
		}
	}

	protected void func_230299_a_(BlockRayTraceResult result) {
		super.func_230299_a_(result);
		if (!this.world.isRemote) {
			ItemStack stack = this.getItem();
			if(stack.getItem() instanceof EntityFlaskItem) {
				EntityFlaskItem forceFlask = (EntityFlaskItem) stack.getItem();
				if(forceFlask.hasEntityStored(stack)) {
					Entity storedEntity = forceFlask.getStoredEntity(stack, this.world);
					BlockPos pos = result.getPos().offset(result.getFace());
					storedEntity.setPositionAndRotation(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
					this.world.addEntity(storedEntity);
				}
				this.setItem(new ItemStack(ForceRegistry.FORCE_FLASK.get()));
			}
		}
	}

	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		if (!this.world.isRemote) {
			this.entityDropItem(this.getItem(), 0.5F);

			this.world.setEntityState(this, (byte)3);
			this.remove();
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
