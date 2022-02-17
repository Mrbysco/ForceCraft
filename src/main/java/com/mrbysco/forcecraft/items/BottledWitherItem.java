package com.mrbysco.forcecraft.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class BottledWitherItem extends BaseItem {

    public BottledWitherItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            BlockPos pos = context.getClickedPos();
            WitherBoss wither = new WitherBoss(EntityType.WITHER, level);
            wither.moveTo(pos.getX(), pos.getY() + 2.0, pos.getZ(), 0.0F, 0.0F);
            level.addFreshEntity(wither);
        }

        Player player = context.getPlayer();
        if(player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }

        return super.useOn(context);
    }
}
