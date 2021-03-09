package mrbysco.forcecraft.items.nonburnable;

import mrbysco.forcecraft.items.BaseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class InertCoreItem extends BaseItem {
    public InertCoreItem(Item.Properties properties) {
        super(properties);
    }

    /* Non Flamable */

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        ItemEntity entity = new NonBurnableItemEntity(world, location.getPosX(), location.getPosY(), location.getPosZ(), itemstack);
        if (location instanceof ItemEntity) {
            CompoundNBT tag = new CompoundNBT();
            location.writeWithoutTypeId(tag);
            entity.setPickupDelay(tag.getShort("PickupDelay"));
        }
        Vector3d locMotion = location.getMotion();
        entity.setMotion(locMotion.x, locMotion.y, locMotion.z);
        return entity;
    }
}
