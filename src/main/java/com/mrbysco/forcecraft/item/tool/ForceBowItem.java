package com.mrbysco.forcecraft.item.tool;

import com.mrbysco.forcecraft.data.BowMode;
import com.mrbysco.forcecraft.entity.ForceArrow;
import com.mrbysco.forcecraft.registry.ForceComponents;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class ForceBowItem extends BowItem {
	private static final Predicate<ItemStack> ARROW_ONLY = itemStack -> itemStack.is(ForceRegistry.FORCE_ARROW);

	public ForceBowItem(Properties properties) {
		super(properties.component(ForceComponents.BOW_MODE, BowMode.NORMAL));
	}

	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit) {
		Projectile projectileEntity = super.createProjectile(level, shooter, weapon, projectile, isCrit);
		if (projectileEntity instanceof ForceArrow arrow) {
			BowMode mode = weapon.getOrDefault(ForceComponents.BOW_MODE, BowMode.NORMAL);
			if (mode == BowMode.ICE) {
				arrow.setIce(true);
			} else if (mode == BowMode.TORCH) {
				arrow.setTorch(takeTorch(shooter));
			}

			if (weapon.has(ForceComponents.BANE))
				arrow.setBane(true);
			if (weapon.has(ForceComponents.BLEED))
				arrow.setBleed(weapon.getOrDefault(ForceComponents.BLEED, 0));
			if (weapon.has(ForceComponents.HEAT))
				arrow.setHeat(true);
		}
		return projectileEntity;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			if (player.isShiftKeyDown()) {
				ItemStack bowStack = player.getItemInHand(hand);
				BowMode mode = bowStack.getOrDefault(ForceComponents.BOW_MODE, BowMode.NORMAL);
				BowMode nextMode = mode.getNext(bowStack);
				bowStack.set(ForceComponents.BOW_MODE, nextMode);
				player.sendSystemMessage(Component.translatable("forcecraft.bow.switch_mode", nextMode.getName()));
				return InteractionResult.FAIL;
			}

			BowMode currentMode = player.getItemInHand(hand).getOrDefault(ForceComponents.BOW_MODE, BowMode.NORMAL);
			if (!player.isShiftKeyDown() && currentMode == BowMode.TORCH && findTorch(player).isEmpty()) {
				if (!level.isClientSide()) {
					player.sendSystemMessage(Component.translatable("forcecraft.bow.no_torch"));
				}
				return InteractionResult.FAIL;
			}
		}

		return super.use(level, player, hand);
	}

	private static boolean isTorch(ItemStack stack) {
		return stack.is(ForceTags.Items.FORCE_TORCHES) || stack.is(Items.TORCH);
	}

	private static ItemStack findTorch(Player player) {
		if (player.hasInfiniteMaterials()) return new ItemStack(Items.TORCH);
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (isTorch(stack)) return stack;
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack takeTorch(LivingEntity shooter) {
		if (!(shooter instanceof Player player)) return ItemStack.EMPTY;
		ItemStack found = findTorch(player);
		if (found.isEmpty()) return ItemStack.EMPTY;
		if (player.hasInfiniteMaterials()) return found; // creative: nothing consumed
		return found.split(1);
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return ARROW_ONLY;
	}
}
