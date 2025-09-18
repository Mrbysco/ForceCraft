package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.forcewrench.ForceWrenchData;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.components.ForceComponents.WRENCH;

public class ForceWrenchItem extends BaseItem implements IForceChargingTool {

	public ForceWrenchItem(Item.Properties name) {
		super(name.stacksTo(1).component(WRENCH, ForceWrenchData.EMPTY));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		InteractionHand hand = context.getHand();
		if (!stack.has(ForceComponents.FORCE_INFUSED))
			stack.set(ForceComponents.FORCE_INFUSED, false);
		if (player != null && player.isCrouching()) {
			ForceWrenchData attachment = stack.getOrDefault(WRENCH, ForceWrenchData.EMPTY);
			if (level.getBlockEntity(pos) instanceof BlockEntity && !attachment.canStoreBlock()) {
				return serializeNBT(level, pos, player, hand);
			} else if (attachment.canStoreBlock())
				placeBlockFromWrench(level, pos, player, hand, context.getClickedFace());
		} else {
			ForceToolData fd = new ForceToolData(stack);
			if (fd.getForce() >= 10) {
				BlockState state = level.getBlockState(pos);
				if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
					level.setBlockAndUpdate(pos, state.rotate(level, pos, Rotation.CLOCKWISE_90));
					fd.setForce(fd.getForce() - 10);
				}
			} else {
				if (player != null)
					player.displayClientMessage(Component.translatable("forcecraft.wrench_rotate.insufficient", 10).withStyle(ChatFormatting.RED), true);
			}
		}
		return super.onItemUseFirst(stack, context);
	}

	private InteractionResult serializeNBT(Level level, BlockPos pos, Player player, InteractionHand hand) {
		ItemStack heldWrench = player.getItemInHand(hand);
		ForceToolData fd = new ForceToolData(heldWrench);
		if (fd.getForce() >= 250) {
			BlockState state = level.getBlockState(pos);
			if (state.getPistonPushReaction() == PushReaction.BLOCK) {
				return InteractionResult.FAIL;
			}

			String blockName = state.getBlock().getDescriptionId();
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if (blockEntity != null) {
				CompoundTag tag = blockEntity.saveWithoutMetadata(level.registryAccess());
				ForceWrenchData attachment = new ForceWrenchData(tag, state, blockName);
				level.removeBlockEntity(pos);

				heldWrench.set(WRENCH, attachment);
			}
			fd.setForce(fd.getForce() - 250);
			BlockState airState = Blocks.AIR.defaultBlockState();
			level.setBlockAndUpdate(pos, airState);
			return InteractionResult.SUCCESS;
		} else {
			player.displayClientMessage(Component.translatable("forcecraft.wrench_transport.insufficient", 250).withStyle(ChatFormatting.RED), true);
		}
		return InteractionResult.FAIL;
	}

	private InteractionResult placeBlockFromWrench(Level level, BlockPos pos, Player player, InteractionHand hand, Direction side) {
		ItemStack heldWrench = player.getItemInHand(hand);
		ForceWrenchData attachment = heldWrench.get(WRENCH);
		if (attachment.storedBlockState() != null) {
			BlockState state = attachment.storedBlockState();
			BlockPos offPos = pos.relative(side);
			level.setBlockAndUpdate(offPos, state);

			if (attachment.storedBlockNBT() != null && state.getBlock() instanceof EntityBlock entityBlock) {
				CompoundTag blockTag = attachment.storedBlockNBT();
				BlockEntity be = entityBlock.newBlockEntity(offPos, state);
				if (be != null) {
					be.loadWithComponents(blockTag, level.registryAccess());
					be.setChanged();
					level.setBlockEntity(be);
					level.blockEntityChanged(offPos);
				} else {
					if (blockTag != null) {
						ForceCraft.LOGGER.error("Was unable to load block entity");
					}
				}
			}

			heldWrench.remove(WRENCH);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
		if (stack.has(ForceComponents.WRENCH)) {
			ForceWrenchData attachment = stack.getOrDefault(ForceComponents.WRENCH, ForceWrenchData.EMPTY);
			if (attachment.name() != null && !attachment.name().isEmpty()) { // idk what this is
				tooltip.add(Component.literal("Stored: ").withStyle(ChatFormatting.GOLD)
						.append(Component.translatable(attachment.name()).withStyle(ChatFormatting.GRAY)));
			}
		}
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		//TODO: Make the wrench only work when it has Force. Only pickup a block when shift-right click. Using 100 force. Else rotate a block for 25 force
		return this.damageItem(stack, amount);
	}
}
