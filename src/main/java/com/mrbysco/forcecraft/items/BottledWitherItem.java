package com.mrbysco.forcecraft.items;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BottledWitherItem extends BaseItem {

	public BottledWitherItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World worldIn = context.getLevel();
		if (!worldIn.isClientSide) {
			BlockPos pos = context.getClickedPos();
			WitherEntity wither = new WitherEntity(EntityType.WITHER, worldIn);
			wither.moveTo(pos.getX(), pos.getY() + 2.0, pos.getZ(), 0.0F, 0.0F);
			worldIn.addFreshEntity(wither);
		}

		PlayerEntity player = context.getPlayer();
		if (player != null && !player.abilities.instabuild) {
			context.getItemInHand().shrink(1);
		}

		return super.useOn(context);
	}
}
