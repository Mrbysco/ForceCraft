package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.util.TooltipUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ForceBowItem extends BowItem implements IForceChargingTool {
	public static final Predicate<ItemStack> FORCE_ARROWS = (stack) -> stack.getItem() instanceof ForceArrowItem;

	public ForceBowItem(Properties properties) {
		super(properties
				.stacksTo(1)
				.durability(332)
				.enchantable(0)
		);
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
		if (livingEntity instanceof Player player) {
			ItemStack itemstack = player.getProjectile(stack);
			if (!itemstack.isEmpty()) {
				int i = this.getUseDuration(stack, livingEntity) - timeLeft;
				i = net.neoforged.neoforge.event.EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
				if (i < 0) return false;
				float f = getPowerForTime(i);
				if (!((double) f < 0.1)) {
					List<ItemStack> list = draw(stack, itemstack, player);
					if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
						this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == 1.0F, null);
					}

					level.playSound(
							null,
							player.getX(),
							player.getY(),
							player.getZ(),
							SoundEvents.ARROW_SHOOT,
							SoundSource.PLAYERS,
							1.0F,
							1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
					);
					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
		return true;
	}

//	@Override
//	public int getEnchantmentValue() {
//		return 0;
//	}
//
//	@Override
//	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
//		return false;
//	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		TooltipUtil.addForceTooltips(itemStack, builder);
		ForceToolData fd = new ForceToolData(itemStack);
		fd.attachInformation(builder);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		return this.damageItem(stack, amount);
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return FORCE_ARROWS;
	}
}
