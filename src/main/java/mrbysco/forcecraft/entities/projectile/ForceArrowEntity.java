package mrbysco.forcecraft.entities.projectile;

import mrbysco.forcecraft.registry.ForceEntities;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class ForceArrowEntity extends ArrowEntity {
	public ForceArrowEntity(EntityType<? extends ArrowEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public ForceArrowEntity(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.setShooter(shooter);
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ForceRegistry.FORCE_ARROW.get());
	}

	@Override
	public EntityType<?> getType() {
		return ForceEntities.FORCE_ARROW.get();
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
