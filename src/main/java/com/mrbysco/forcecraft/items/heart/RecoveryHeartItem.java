package com.mrbysco.forcecraft.items.heart;

import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RecoveryHeartItem extends BaseItem {

	public RecoveryHeartItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, level, entityIn, itemSlot, isSelected);

		if (!(entityIn instanceof Player player))
			return;

		BlockPos pos = new BlockPos(player.getX(), player.getY() + player.getMyRidingOffset(), player.getZ());

		int HEAL_AMT = 2;
		for(int i = 0; i < stack.getCount(); i++) {
			// 2 health == 1 heart
			player.heal(HEAL_AMT);
			stack.shrink(1);
		}
		player.level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), ForceSounds.HEART_PICKUP.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

		for (int i1 = 0; i1 < 15; ++i1) {
			double d0 = level.random.nextGaussian() * 0.02D;
			double d1 = level.random.nextGaussian() * 0.02D;
			double d2 = level.random.nextGaussian() * 0.02D;

			level.addParticle(ParticleTypes.HEART, (double) ((float) pos.getX() + level.random.nextFloat()),
					((double) pos.getY() + 1.0f) + (double) level.random.nextFloat() * 2.0f, (double) ((float) pos.getZ() + level.random.nextFloat()), d0, d1, d2);
		}
	}
}
