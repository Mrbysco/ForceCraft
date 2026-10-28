package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@EventBusSubscriber
public class TreasureHandler {
	@SubscribeEvent
	public static void onTreasureDrops(LivingDropsEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player)) return;

		ItemStack held = player.getMainHandItem();
		if (!held.is(ForceTags.Items.VALID_TREASURE_ITEMS) || !held.has(ForceComponents.TREASURE.get())) return;
		int lootLevel = EnchantmentHelper.getEnchantmentLevel(player.level().holderOrThrow(Enchantments.LOOTING), player);

		LivingEntity entity = event.getEntity();
		float chance = 0.05F + 0.05F * lootLevel;
		if (entity.getRandom().nextFloat() >= chance) return;

		if (entity.isInvertedHealAndHarm()) {
			event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ForceRegistry.UNDEATH_CARD.get(), entity.getRandom().nextInt(Math.max(1, lootLevel)) + 1)));
		} else {
			if (entity instanceof Enemy) {
				event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ForceRegistry.DARKNESS_CARD.get(), entity.getRandom().nextInt(Math.max(1, lootLevel)) + 1)));
			} else {
				event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ForceRegistry.LIFE_CARD.get(), entity.getRandom().nextInt(Math.max(1, lootLevel)) + 1)));
			}
		}
	}
}
