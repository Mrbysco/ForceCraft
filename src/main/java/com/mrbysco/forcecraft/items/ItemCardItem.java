package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.menu.ItemCardMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCardItem extends BaseItem {
	public ItemCardItem(Properties properties) {
		super(properties);
	}


	private static final Component BAD_READ = Component.literal("BAD READ. TRY AGAIN.").withStyle(ChatFormatting.RED);
	private static final Component TOO_SLOW = Component.literal("TOO SLOW. TRY AGAIN.").withStyle(ChatFormatting.RED);
	private static final Component TOO_FAST = Component.literal("TOO FAST. TRY AGAIN.").withStyle(ChatFormatting.RED);

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		if (playerIn.isShiftKeyDown()) {
			if (!level.isClientSide) {
				playerIn.openMenu(getContainer(level, playerIn.blockPosition()), playerIn.blockPosition());
			}
		}
		return super.use(level, playerIn, handIn);
	}

	@Nullable
	public MenuProvider getContainer(Level level, BlockPos pos) {
		return new SimpleMenuProvider((id, inventory, player) -> {
			return new ItemCardMenu(id, inventory, ContainerLevelAccess.create(level, pos));
		}, Component.translatable(Reference.MOD_ID + ".container.card"));
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
		Level level = playerIn.level();
		level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), SoundSource.NEUTRAL, 1.0F, 1.0F);

		if (level.isClientSide) {
			int rand = playerIn.getRandom().nextInt(3);
			Component message = switch (rand) {
				default -> BAD_READ;
				case 1 -> TOO_SLOW;
				case 2 -> TOO_FAST;
			};
			playerIn.displayClientMessage(message, true);
		}

		return super.interactLivingEntity(stack, playerIn, target, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("RecipeContents")) {
			CompoundTag recipeContents = tag.getCompound("RecipeContents");
			ItemStack resultStack = ItemStack.of(recipeContents.getCompound("result"));
			tooltip.add(Component.translatable("forcecraft.item_card.recipe_output",
					Component.literal(resultStack.getCount() + " " + resultStack.getHoverName().getString()).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.YELLOW));
		} else {
			tooltip.add(Component.translatable("forcecraft.item_card.unset").withStyle(ChatFormatting.RED));
		}
		tooltip.add(Component.literal(" "));
		tooltip.add(Component.translatable("forcecraft.item_card.recipe_set").withStyle(ChatFormatting.BOLD));

		super.appendHoverText(stack, level, tooltip, flagIn);
	}
}
