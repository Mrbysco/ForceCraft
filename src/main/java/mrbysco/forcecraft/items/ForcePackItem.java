package mrbysco.forcecraft.items;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.pack.PackInventoryProvider;
import mrbysco.forcecraft.capablilities.pack.PackItemStackHandler;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import mrbysco.forcecraft.client.gui.pack.RenameAndRecolorScreen;
import mrbysco.forcecraft.container.ForcePackContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

import java.util.List;

public class ForcePackItem extends BaseItem {

    public static final String SLOTS_TOTAL = "SlotsTotal";
	public static final String SLOTS_USED = "SlotsUsed";

	public ForcePackItem(Item.Properties properties){
        super(properties.maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(handler instanceof PackItemStackHandler) {
            if(playerIn.isSneaking()) {
                if(worldIn.isRemote) {

                	ForceCraft.LOGGER.info("(CLIENT) pack:openScreen {}  ", stack.getTag());
                    RenameAndRecolorScreen.openScreen(stack, handIn);
                }
            } else {
            	ForceCraft.LOGGER.info("(SERVER) openContainer {}  ", stack.getTag());
                playerIn.openContainer(this.getContainer(stack));
                return new ActionResult<ItemStack>(ActionResultType.PASS, stack);
            }
        }

        //If it doesn't nothing bad happens
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItem();
        if(player != null && stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
            player.openContainer(this.getContainer(stack));
            return ActionResultType.PASS;
        }
        //If it doesn't nothing bad happens
        return super.onItemUse(context);
    }

    @Nullable
    public INamedContainerProvider getContainer(ItemStack stack) {
        return new SimpleNamedContainerProvider((id, inventory, player) -> {
            return new ForcePackContainer(id, inventory, stack);
        }, stack.hasDisplayName() ? ((TextComponent)stack.getDisplayName()).mergeStyle(TextFormatting.BLACK) : new TranslationTextComponent(Reference.MOD_ID + ".container.pack"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.contains(SLOTS_USED) &&  tag.contains(SLOTS_TOTAL)) {
            tooltip.add(new StringTextComponent(String.format("%s/%s Slots", tag.getInt(SLOTS_USED), tag.getInt(SLOTS_TOTAL))));
        } else {
            IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
            int defaultAmount = 8;
            if(handler != null) {
                defaultAmount = handler.getSlots();
            }
            tooltip.add(new StringTextComponent(String.format("0/%s Slots", defaultAmount)));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
    
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return ((TextComponent)super.getDisplayName(stack)).mergeStyle(TextFormatting.YELLOW);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new PackInventoryProvider();
    }
    
    // ShareTag for server->client capability data sync
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
    	CompoundNBT shareTag = stack.getOrCreateTag();
    	// no capability, use all of it
        return shareTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    	
    	if(nbt != null && nbt.contains(SLOTS_TOTAL)) {

    		stack.getOrCreateTag().putInt(SLOTS_TOTAL, nbt.getInt(SLOTS_TOTAL));
    		stack.getOrCreateTag().putInt(SLOTS_USED, nbt.getInt(SLOTS_USED));
//
//        	ForceCraft.LOGGER.info("(CLIENT) readShareTag : AFTER setting to stack {}  ", stack.getTag());
    	}
    }
}
