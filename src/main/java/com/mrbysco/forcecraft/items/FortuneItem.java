package com.mrbysco.forcecraft.items;

import com.mrbysco.forcecraft.config.ConfigHandler;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FortuneItem extends BaseItem {
    private static final Random random = new Random();

    public FortuneItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        CompoundTag nbt;
        if(stack.hasTag()) {
            nbt = stack.getTag();
        } else {
            nbt = new CompoundTag();
        }

        if(!nbt.contains("message")) {
            addMessage(stack, nbt);
        }

        if(!level.isClientSide) {
            if(playerIn != null && playerIn.isShiftKeyDown()) {
                if(!playerIn.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                ItemStack paperStack = new ItemStack(Items.PAPER);
                if(!playerIn.addItem(paperStack)) {
                    playerIn.spawnAtLocation(paperStack);
                }
            } else {
                playerIn.sendMessage(new TextComponent(nbt.getString("message")), Util.NIL_UUID);
            }
        }
        return super.use(level, playerIn, handIn);
    }

    public static void addMessage(ItemStack stack, CompoundTag nbt) {
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
