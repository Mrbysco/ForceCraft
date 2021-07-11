package mrbysco.forcecraft.items;

import mrbysco.forcecraft.config.ConfigHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FortuneItem extends BaseItem {

    public FortuneItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT nbt;
        if(stack.hasTag()) {
            nbt = stack.getTag();
        } else {
            nbt = new CompoundNBT();
        }

        if(!nbt.contains("message")) {
            addMessage(stack, nbt);
        }

        if(!worldIn.isRemote) {
            if(playerIn != null && playerIn.isSneaking()) {
                if(!playerIn.abilities.isCreativeMode) {
                    stack.shrink(1);
                }
                ItemStack paperStack = new ItemStack(Items.PAPER);
                if(!playerIn.addItemStackToInventory(paperStack)) {
                    playerIn.entityDropItem(paperStack);
                }
            } else {
                playerIn.sendMessage(new StringTextComponent(nbt.getString("message")), Util.DUMMY_UUID);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void addMessage(ItemStack stack, CompoundNBT nbt) {
        List<String> messages = new ArrayList<>(ConfigHandler.COMMON.fortuneMessages.get());
        String message = "No fortune for you";
        if(!messages.isEmpty()) {
            int idx = random.nextInt(messages.size());
            message = messages.get(idx);
        }

        nbt.putString("message", message);
        stack.setTag(nbt);
    }
}
