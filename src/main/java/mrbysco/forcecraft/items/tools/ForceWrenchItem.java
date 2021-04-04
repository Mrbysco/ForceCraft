package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.capablilities.forcewrench.ForceWrenchProvider;
import mrbysco.forcecraft.capablilities.forcewrench.IForceWrench;
import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.items.infuser.ForceToolData;
import mrbysco.forcecraft.items.infuser.IForceChargingTool;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEWRENCH;

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
            IForceWrench wrenchCap = stack.getCapability(CAPABILITY_FORCEWRENCH).orElse(null);
            if(wrenchCap != null) {
                if (world.getTileEntity(pos) instanceof TileEntity && !wrenchCap.canStoreBlock()) {
                    return serializeNBT(world, pos, player, hand);
                } else if(wrenchCap.canStoreBlock())
                    placeBlockFromWrench(world, pos, player, hand, context.getFace());
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
        BlockState state = world.getBlockState(pos);

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
            return ActionResultType.SUCCESS;
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(tooltip);
    	stack.getCapability(CAPABILITY_FORCEWRENCH).ifPresent((cap) -> {
            if(cap.getStoredName() != null){ // idk what this is
                tooltip.add(new StringTextComponent("Stored: ").mergeStyle(TextFormatting.GOLD)
                        .appendSibling(new TranslationTextComponent(cap.getStoredName()).mergeStyle(TextFormatting.GRAY)));
            }
        });
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}
}
