package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.forcewrench.ForceWrenchProvider;
import com.mrbysco.forcecraft.capablilities.forcewrench.ForceWrenchStorage;
import com.mrbysco.forcecraft.capablilities.forcewrench.IForceWrench;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEWRENCH;

public class ForceWrenchItem extends BaseItem implements IForceChargingTool {

    public ForceWrenchItem(Item.Properties name){
        super(name.maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Hand hand = context.getHand();
        if (stack.getItem() instanceof ForceWrenchItem) {
            if(player.isCrouching()) {
                IForceWrench wrenchCap = stack.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
                if(wrenchCap != null) {
                    if (world.getTileEntity(pos) instanceof TileEntity && !wrenchCap.canStoreBlock()) {
                        return serializeNBT(world, pos, player, hand);
                    } else if(wrenchCap.canStoreBlock())
                        placeBlockFromWrench(world, pos, player, hand, context.getFace());
                }
            } else {
                ForceToolData fd = new ForceToolData(stack);
                if(fd.getForce() >= 10) {
                    BlockState state = world.getBlockState(pos);
                    if(state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        world.setBlockState(pos, state.rotate(world, pos, Rotation.CLOCKWISE_90));
                        fd.setForce(fd.getForce() - 10);
                        fd.write(stack);
                    }
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("forcecraft.wrench_rotate.insufficient", 10).mergeStyle(TextFormatting.RED), true);
                }
            }
        }
        return super.onItemUseFirst(stack, context);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_FORCEWRENCH == null) {
            return null;
        }
        return new ForceWrenchProvider();
    }

    private ActionResultType serializeNBT(World world, BlockPos pos, PlayerEntity player, Hand hand){
        ItemStack heldWrench = player.getHeldItem(hand);
        ForceToolData fd = new ForceToolData(heldWrench);
        if(fd.getForce() >= 250) {
            BlockState state = world.getBlockState(pos);
            if(state.getPushReaction() == PushReaction.BLOCK) {
                return ActionResultType.FAIL;
            }

            IForceWrench wrenchCap = heldWrench.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
            if(wrenchCap != null) {
                String blockName = state.getBlock().getTranslationKey();
                TileEntity tileEntity = world.getTileEntity(pos);

                if(tileEntity != null){
                    CompoundNBT nbt = tileEntity.write(new CompoundNBT());
                    wrenchCap.storeBlockNBT(nbt);
                    wrenchCap.storeBlockState(state);
                    wrenchCap.setBlockName(blockName);
                    world.removeTileEntity(pos);
                    BlockState airState = Blocks.AIR.getDefaultState();
                    world.removeBlock(pos, false);
                    world.markBlockRangeForRenderUpdate(pos, state, airState);
                }
                fd.setForce(fd.getForce() - 250);
                fd.write(heldWrench);
                return ActionResultType.SUCCESS;
            }
        } else {
            player.sendStatusMessage(new TranslationTextComponent("forcecraft.wrench_transport.insufficient", 250).mergeStyle(TextFormatting.RED), true);
        }
        return ActionResultType.FAIL;
    }

    private ActionResultType placeBlockFromWrench(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side) {
        ItemStack heldWrench = player.getHeldItem(hand);
        IForceWrench wrenchCap = heldWrench.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
        if(wrenchCap != null) {
            if(wrenchCap.getStoredBlockState() != null) {
                CompoundNBT tileCmp = wrenchCap.getStoredBlockNBT();
                BlockState state = wrenchCap.getStoredBlockState();
                TileEntity te = TileEntity.readTileEntity(state, tileCmp);
                BlockPos offPos = pos.offset(side);
                if(state != null) {
                    world.setBlockState(offPos, state);
                }
                if(te != null) {
                    te.read(state, tileCmp);
                    te.setPos(offPos);
                    world.setTileEntity(offPos, te);
                }
                wrenchCap.clearBlockStorage();
            }
        }

        return ActionResultType.SUCCESS;
    }

    // ShareTag for server->client capability data sync
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT nbt = super.getShareTag(stack);

        IForceWrench cap = stack.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
        if(cap != null) {
            CompoundNBT shareTag = ForceWrenchStorage.serializeNBT(cap);
            if(nbt == null) {
                nbt = new CompoundNBT();
            }
            nbt.put(Reference.MOD_ID, shareTag);
        }
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
            return;
        }

        IForceWrench cap = stack.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
        if(cap != null) {
            INBT shareTag = nbt.get(Reference.MOD_ID);
            ForceWrenchStorage.deserializeNBT(cap, shareTag);
        }
        super.readShareTag(stack, nbt);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
        ForceWrenchStorage.attachInformation(stack, lores);
        super.addInformation(stack, worldIn, lores, flagIn);
    }

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        //TODO: Make the wrench only work when it has Force. Only pickup a block when shift-right click. Using 100 force. Else rotate a block for 25 force
		return this.damageItem(stack, amount);
	}
}
