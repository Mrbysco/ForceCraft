package mrbysco.forcecraft.items;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ItemCardContainer;
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

public class ItemCardItem extends BaseItem {
	public ItemCardItem(Properties properties) {
		super(properties);
	}


	private static final ITextComponent BAD_READ = new StringTextComponent("BAD READ. TRY AGAIN.").mergeStyle(TextFormatting.RED);
	private static final ITextComponent TOO_SLOW = new StringTextComponent("TOO SLOW. TRY AGAIN.").mergeStyle(TextFormatting.RED);
	private static final ITextComponent TOO_FAST = new StringTextComponent("TOO FAST. TRY AGAIN.").mergeStyle(TextFormatting.RED);

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.isSneaking()) {
			if (!worldIn.isRemote) {
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(worldIn, playerIn.getPosition()), playerIn.getPosition());
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Nullable
	public INamedContainerProvider getContainer(World worldIn, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inventory, player) -> {
			return new ItemCardContainer(id, inventory, IWorldPosCallable.of(worldIn, pos));
		}, new TranslationTextComponent(Reference.MOD_ID + ".container.card"));
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		World worldIn = playerIn.world;
		worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.NEUTRAL, 1.0F, 1.0F);

		if(worldIn.isRemote) {
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
			playerIn.sendStatusMessage(message, true);
		}

		return super.itemInteractionForEntity(stack, playerIn, target, hand);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT tag = stack.getOrCreateTag();
		if(tag.contains("RecipeContents")) {
			CompoundNBT recipeContents = tag.getCompound("RecipeContents");
			ItemStack resultStack = ItemStack.read(recipeContents.getCompound("result"));
			tooltip.add(new TranslationTextComponent("forcecraft.item_card.recipe_output",
					new StringTextComponent(resultStack.getCount() + " " + resultStack.getDisplayName().getString()).mergeStyle(TextFormatting.GRAY)).mergeStyle(TextFormatting.YELLOW));
		} else {
			tooltip.add(new TranslationTextComponent("forcecraft.item_card.recipe_output", 1, "minecraft:air").mergeStyle(TextFormatting.YELLOW));
		}
		tooltip.add(new StringTextComponent(" "));
		tooltip.add(new TranslationTextComponent("forcecraft.item_card.recipe_set").mergeStyle(TextFormatting.BOLD));

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
