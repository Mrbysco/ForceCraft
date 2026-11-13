package com.mrbysco.forcecraft.item.tool;

import com.mrbysco.forcecraft.advancement.ForceCriteriaTriggers;
import com.mrbysco.forcecraft.entity.cold.ColdChicken;
import com.mrbysco.forcecraft.entity.cold.ColdCow;
import com.mrbysco.forcecraft.entity.cold.ColdPig;
import com.mrbysco.forcecraft.entity.cold.IColdMob;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ForceShearsItem extends ShearsItem {
	private static final int SET_FIRE_TIME = 10;

	public ForceShearsItem(Properties properties) {
		super(properties.component(DataComponents.TOOL, createToolProperties()));
	}

	private static final Item[] WOOL = {Items.WOOL.red(), Items.WOOL.blue(), Items.WOOL.black(), Items.WOOL.blue(), Items.WOOL.brown(), Items.WOOL.white(), Items.WOOL.orange(), Items.WOOL.magenta(), Items.WOOL.lightBlue(), Items.WOOL.yellow(), Items.WOOL.lime(), Items.WOOL.pink(), Items.WOOL.gray(), Items.WOOL.lightGray(),
			Items.WOOL.cyan(), Items.WOOL.purple(), Items.WOOL.brown(), Items.WOOL.green()};

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

		if (!(entity instanceof IColdMob) && !entity.isBaby()) {
			boolean hasHeat = stack.has(ForceComponents.HEAT);
			Mob replacementMob = null;
			List<ItemStack> drops = new ArrayList<>();
			if (entity instanceof Cow originalCow) {
				int i = 1 + rand.nextInt(3);
				for (int j = 0; j < i; ++j)
					drops.add(new ItemStack(Items.LEATHER, 1));

				replacementMob = new ColdCow(level, originalCow.getType());
			}
			if (entity instanceof Chicken originalChicken) {
				int i = 1 + rand.nextInt(3);
				for (int j = 0; j < i; ++j) {
					drops.add(new ItemStack(Items.FEATHER, 1));
				}

				replacementMob = new ColdChicken(level, originalChicken.getType());
			}
			if (entity instanceof Pig originalPig) {
				int i = 1 + rand.nextInt(2);
				for (int j = 0; j < i; ++j)
					drops.add(new ItemStack(hasHeat ? ForceRegistry.COOKED_BACON.get() : ForceRegistry.RAW_BACON.get(), 1));

				replacementMob = new ColdPig(level, originalPig.getType());
			}

			if (replacementMob != null) {
				drops.forEach(dropStack -> {
					ItemEntity itemEntity = entity.spawnAtLocation(serverLevel, dropStack, 1.0F);
					if (itemEntity != null) {
						itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(
								(rand.nextFloat() - rand.nextFloat()) * 0.1F,
								rand.nextFloat() * 0.05F,
								(rand.nextFloat() - rand.nextFloat()) * 0.1F)
						);
					}
				});

				entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);

				replacementMob.copyPosition(entity);
				UUID mobUUID = replacementMob.getUUID();
				replacementMob.restoreFrom(entity);
				replacementMob.setUUID(mobUUID);

				entity.remove(Entity.RemovalReason.DISCARDED);
				level.addFreshEntity(replacementMob);
				if (hasHeat) {
					replacementMob.igniteForSeconds(SET_FIRE_TIME);
				}

				ForceCriteriaTriggers.FORCE_SHEAR_ENTITY.get().trigger((ServerPlayer) player, entity);

				stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
				return InteractionResult.SUCCESS;
			}
		}

		return super.interactLivingEntity(stack, player, entity, hand);
	}
}
