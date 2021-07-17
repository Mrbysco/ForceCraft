package mrbysco.forcecraft.items.heart;

import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.registry.ForceSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RecoveryHeartItem extends BaseItem {

	public RecoveryHeartItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		if (!(entityIn instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) entityIn;
		BlockPos pos = new BlockPos(player.getPosX(), player.getPosY() + player.getYOffset(), player.getPosZ());

		int HEAL_AMT = 2;
		for(int i = 0; i < stack.getCount(); i++) {
			// 2 health == 1 heart
			player.heal(HEAL_AMT);
			stack.shrink(1);
		}
		player.world.playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(), ForceSounds.HEART_PICKUP.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);

		for (int i1 = 0; i1 < 15; ++i1) {
			double d0 = random.nextGaussian() * 0.02D;
			double d1 = random.nextGaussian() * 0.02D;
			double d2 = random.nextGaussian() * 0.02D;

			worldIn.addParticle(ParticleTypes.HEART, (double) ((float) pos.getX() + random.nextFloat()), ((double) pos.getY() + 1.0f) + (double) random.nextFloat() * 2.0f, (double) ((float) pos.getZ() + random.nextFloat()), d0, d1, d2);
		}
	}
}
