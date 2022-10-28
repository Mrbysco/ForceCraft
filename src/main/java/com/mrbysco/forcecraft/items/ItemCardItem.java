package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.container.ItemCardContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class ItemCardItem extends BaseItem {
	public ItemCardItem(Properties properties) {
		super(properties);
	}


	private static final ITextComponent BAD_READ = new StringTextComponent("BAD READ. TRY AGAIN.").withStyle(TextFormatting.RED);
	private static final ITextComponent TOO_SLOW = new StringTextComponent("TOO SLOW. TRY AGAIN.").withStyle(TextFormatting.RED);
	private static final ITextComponent TOO_FAST = new StringTextComponent("TOO FAST. TRY AGAIN.").withStyle(TextFormatting.RED);

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide) {
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(worldIn, playerIn.blockPosition()), playerIn.blockPosition());
			}
		}
		return super.use(worldIn, playerIn, handIn);
	}

	@Nullable
	public INamedContainerProvider getContainer(World worldIn, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inventory, player) -> {
			return new ItemCardContainer(id, inventory, IWorldPosCallable.create(worldIn, pos));
		}, new TranslationTextComponent(Reference.MOD_ID + ".container.card"));
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		World worldIn = playerIn.level;
		worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.NOTE_BLOCK_DIDGERIDOO, SoundCategory.NEUTRAL, 1.0F, 1.0F);

		if (worldIn.isClientSide) {
			int rand = random.nextInt(3);
			ITextComponent message;
			switch (rand) {
				default:
					message = BAD_READ;
					break;
				case 1:
					message = TOO_SLOW;
					break;
				case 2:
					message = TOO_FAST;
					break;
			}
			playerIn.displayClientMessage(message, true);
		}

		return super.interactLivingEntity(stack, playerIn, target, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT tag = stack.getOrCreateTag();
		if (tag.contains("RecipeContents")) {
			CompoundNBT recipeContents = tag.getCompound("RecipeContents");
			ItemStack resultStack = ItemStack.of(recipeContents.getCompound("result"));
			tooltip.add(new TranslationTextComponent("forcecraft.item_card.recipe_output",
					new StringTextComponent(resultStack.getCount() + " " + resultStack.getHoverName().getString()).withStyle(TextFormatting.GRAY)).withStyle(TextFormatting.YELLOW));
		} else {
			tooltip.add(new TranslationTextComponent("forcecraft.item_card.unset").withStyle(TextFormatting.RED));
		}
		tooltip.add(new StringTextComponent(" "));
		tooltip.add(new TranslationTextComponent("forcecraft.item_card.recipe_set").withStyle(TextFormatting.BOLD));

		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
