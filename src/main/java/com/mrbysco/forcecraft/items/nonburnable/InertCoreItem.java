package com.mrbysco.forcecraft.items.nonburnable;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.items.BaseItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
	public Entity createEntity(Level level, Entity location, ItemStack itemstack) {
		ItemEntity entity = new NonBurnableItemEntity(level, location.getX(), location.getY(), location.getZ(), itemstack);
		if (location instanceof ItemEntity) {
			try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(ForceCraft.LOGGER)) {
				TagValueOutput output = TagValueOutput.createWithContext(problemreporter$scopedcollector, level.registryAccess());

				location.saveWithoutId(output);
				CompoundTag tag = output.buildResult();
				entity.setPickUpDelay(tag.getShortOr("PickupDelay", (short)0));

			}
		}
		Vec3 locMotion = location.getDeltaMovement();
		entity.setDeltaMovement(locMotion.x, locMotion.y, locMotion.z);
		return entity;
	}
}
