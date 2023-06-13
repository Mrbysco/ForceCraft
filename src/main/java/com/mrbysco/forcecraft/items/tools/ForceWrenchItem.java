package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.ForceCraft;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capabilities.forcewrench.ForceWrenchCapability;
import com.mrbysco.forcecraft.capabilities.forcewrench.IForceWrench;
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
import net.minecraft.world.entity.Entity;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_FORCEWRENCH;

public class ForceWrenchItem extends BaseItem implements IForceChargingTool {

	public ForceWrenchItem(Item.Properties name) {
		super(name.stacksTo(1));
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level level, Player player) {
		super.onCraftedBy(stack, level, player);
		initializeTag(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, level, entityIn, itemSlot, isSelected);
		if (stack.getTag() == null) {
			initializeTag(stack);
		}
	}

	private void initializeTag(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean("ForceInfused", false);
		stack.setTag(tag);
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		InteractionHand hand = context.getHand();
		if (stack.getItem() instanceof ForceWrenchItem) {
			if (player.isCrouching()) {
				IForceWrench wrenchCap = stack.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
				if (wrenchCap != null) {
					if (level.getBlockEntity(pos) instanceof BlockEntity && !wrenchCap.canStoreBlock()) {
						return serializeNBT(level, pos, player, hand);
					} else if (wrenchCap.canStoreBlock())
						placeBlockFromWrench(level, pos, player, hand, context.getClickedFace());
				}
			} else {
				ForceToolData fd = new ForceToolData(stack);
				if (fd.getForce() >= 10) {
					BlockState state = level.getBlockState(pos);
					if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
						level.setBlockAndUpdate(pos, state.rotate(level, pos, Rotation.CLOCKWISE_90));
						fd.setForce(fd.getForce() - 10);
						fd.write(stack);
					}
				} else {
					player.displayClientMessage(Component.translatable("forcecraft.wrench_rotate.insufficient", 10).withStyle(ChatFormatting.RED), true);
				}
			}
		}
		return super.onItemUseFirst(stack, context);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if (CAPABILITY_FORCEWRENCH == null) {
			return null;
		}
		return new ForceWrenchCapability();
	}

	private InteractionResult serializeNBT(Level level, BlockPos pos, Player player, InteractionHand hand) {
		ItemStack heldWrench = player.getItemInHand(hand);
		ForceToolData fd = new ForceToolData(heldWrench);
		if (fd.getForce() >= 250) {
			BlockState state = level.getBlockState(pos);
			if (state.getPistonPushReaction() == PushReaction.BLOCK) {
				return InteractionResult.FAIL;
			}

			IForceWrench wrenchCap = heldWrench.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
			if (wrenchCap != null) {
				String blockName = state.getBlock().getDescriptionId();
				BlockEntity blockEntity = level.getBlockEntity(pos);

				if (blockEntity != null) {
					CompoundTag nbt = blockEntity.saveWithoutMetadata();
					wrenchCap.storeBlockNBT(nbt);
					wrenchCap.storeBlockState(state);
					wrenchCap.setBlockName(blockName);
					level.removeBlockEntity(pos);
				}
				fd.setForce(fd.getForce() - 250);
				fd.write(heldWrench);
				BlockState airState = Blocks.AIR.defaultBlockState();
				level.setBlockAndUpdate(pos, airState);
				return InteractionResult.SUCCESS;
			}
		} else {
			player.displayClientMessage(Component.translatable("forcecraft.wrench_transport.insufficient", 250).withStyle(ChatFormatting.RED), true);
		}
		return InteractionResult.FAIL;
	}

	private InteractionResult placeBlockFromWrench(Level level, BlockPos pos, Player player, InteractionHand hand, Direction side) {
		ItemStack heldWrench = player.getItemInHand(hand);
		IForceWrench wrenchCap = heldWrench.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
		if (wrenchCap != null) {
			if (wrenchCap.getStoredBlockState() != null) {
				BlockState state = wrenchCap.getStoredBlockState();
				BlockPos offPos = pos.relative(side);

				if (state != null) {
					level.setBlockAndUpdate(offPos, state);
				}
				if (wrenchCap.getStoredBlockNBT() != null && state.getBlock() instanceof EntityBlock entityBlock) {
					CompoundTag blockTag = wrenchCap.getStoredBlockNBT();
					BlockEntity be = entityBlock.newBlockEntity(offPos, state);
					if (be != null) {
						be.load(blockTag);
						be.setChanged();
						level.setBlockEntity(be);
						level.blockEntityChanged(offPos);
					} else {
						if(blockTag != null) {
							ForceCraft.LOGGER.error("Was unable to load block entity");
						}
					}
				}

				wrenchCap.clearBlockStorage();
			}
		}

		return InteractionResult.SUCCESS;
	}

	// ShareTag for server->client capability data sync
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = super.getShareTag(stack);

		IForceWrench cap = stack.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
		if (cap != null) {
			CompoundTag shareTag = ForceWrenchCapability.writeNBT(cap);
			if (nbt == null) {
				nbt = new CompoundTag();
			}
			nbt.put(Reference.MOD_ID, shareTag);
		}
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		if (nbt == null || !nbt.contains(Reference.MOD_ID)) {
			return;
		}

		IForceWrench cap = stack.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
		if (cap != null) {
			CompoundTag shareTag = nbt.getCompound(Reference.MOD_ID);
			ForceWrenchCapability.readNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
		ForceWrenchCapability.attachInformation(stack, lores);
		super.appendHoverText(stack, level, lores, flagIn);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		//TODO: Make the wrench only work when it has Force. Only pickup a block when shift-right click. Using 100 force. Else rotate a block for 25 force
		return this.damageItem(stack, amount);
	}
}
