package com.mrbysco.forcecraft.item.tool;

import com.mrbysco.forcecraft.registry.ForceComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;

import java.util.List;

public class ForceShearsItem extends ShearsItem {
	public ForceShearsItem(Properties properties) {
		super(properties.component(DataComponents.TOOL, createToolProperties()));
	}

	private static final Item[] WOOL = {Items.RED_WOOL, Items.BLUE_WOOL, Items.BLACK_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL, Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL,
			Items.CYAN_WOOL, Items.PURPLE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL};

	private ItemStack getRandomWool(Level level) {
		return new ItemStack(WOOL[Mth.nextInt(level.getRandom(), 0, WOOL.length)]);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		Level level = entity.level();
		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.PASS;
		}
		RandomSource rand = level.getRandom();
		if (stack.has(ForceComponents.RAINBOW) && entity instanceof Sheep target) {
			BlockPos pos = BlockPos.containing(entity.position());
			if (target.isShearable(player, stack, level, pos)) {
				List<ItemStack> drops = target.onSheared(player, stack, level, pos);
				for (int i = 0; i < drops.size(); i++) {
					ItemEntity ent = entity.spawnAtLocation(serverLevel, getRandomWool(level), 1.0F);
					if (ent != null)
						ent.setDeltaMovement(ent.getDeltaMovement().add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F));
				}
				stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
				return InteractionResult.SUCCESS;
			}
		}

		//TODO: Handle cold mob

		return super.interactLivingEntity(stack, player, entity, hand);
	}
}
