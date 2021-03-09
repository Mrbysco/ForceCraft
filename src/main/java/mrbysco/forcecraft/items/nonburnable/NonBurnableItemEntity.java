package mrbysco.forcecraft.items.nonburnable;

import mrbysco.forcecraft.registry.ForceEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class NonBurnableItemEntity extends ItemEntity {

    public NonBurnableItemEntity(EntityType<? extends ItemEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public NonBurnableItemEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public NonBurnableItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
    }

    @Override
    public EntityType<?> getType() {
        return ForceEntities.NON_BURNABLE_ITEM.get();
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getDamageType().equals(DamageSource.OUT_OF_WORLD.damageType)) {
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static class EventHandler {
        public static void onExpire(ItemExpireEvent event) {
            if(event.getEntityItem() instanceof NonBurnableItemEntity) {
                event.setCanceled(true);
            }
        }
    }
}
