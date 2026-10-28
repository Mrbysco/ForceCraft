package com.mrbysco.forcecraft.handler.infusion;

import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceDatamaps;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@EventBusSubscriber
public class SoulHandler {
	@SubscribeEvent
	public static void onTreasureDrops(LivingDropsEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player)) return;

		ItemStack held = player.getMainHandItem();
		if (!held.is(ForceTags.Items.VALID_SOUL_ITEMS) || !held.has(ForceComponents.SOUL.get())) return;

		LivingEntity entity = event.getEntity();
		Holder.Reference<EntityType<?>> holder = entity.getType().builtInRegistryHolder();
		DyeColor color = entity.getType().builtInRegistryHolder().getData(ForceDatamaps.MOB_COLORS);
		if (color == null) return;

		DeferredItem<Item> chunk = ForceRegistry.getMobChunks().get(color);
		if (chunk == null) return;

		int lootLevel = EnchantmentHelper.getEnchantmentLevel(player.level().holderOrThrow(Enchantments.LOOTING), player);
		float chance = 0.1F + 0.1F * lootLevel;
		if (entity.getRandom().nextFloat() >= chance) return;

		ItemStack stack = new ItemStack(chunk.get());
		stack.set(ForceComponents.BOUND_ID.get(), holder.key().identifier());
		event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack));
	}
}
