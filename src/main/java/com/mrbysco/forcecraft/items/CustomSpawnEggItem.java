package com.mrbysco.forcecraft.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomSpawnEggItem extends SpawnEggItem {
	public final Supplier<EntityType<?>> entityType;

	private static final DefaultDispenseItemBehavior SPAWN_EGG_BEHAVIOR = new DefaultDispenseItemBehavior() {
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			Direction direction = source.getBlockState().get(DispenserBlock.FACING);
			((CustomSpawnEggItem) stack.getItem()).getType(stack.getTag()).spawn(source.getWorld(), stack, null,
					source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
			stack.shrink(1);
			return stack;
		}
	};

	public CustomSpawnEggItem(Supplier<EntityType<?>> type, int primary, int secondary, Properties properties) {
		super(null, primary, secondary, properties);
		this.entityType = type;
		DispenserBlock.registerDispenseBehavior(this, SPAWN_EGG_BEHAVIOR);
	}

	@Override
	public EntityType<?> getType(@Nullable final CompoundNBT nbt) {
		if (nbt != null && nbt.contains("EntityTag", Constants.NBT.TAG_COMPOUND)) {
			final CompoundNBT entityTag = nbt.getCompound("EntityTag");
			if (entityTag.contains("id", Constants.NBT.TAG_STRING)) {
				return EntityType.byKey(entityTag.getString("id")).orElse(entityType.get());
			}
		}

		return entityType.get();
	}
}