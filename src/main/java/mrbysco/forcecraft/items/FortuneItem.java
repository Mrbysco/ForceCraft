package mrbysco.forcecraft.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;

public class FortuneItem extends BaseItem {

    private String[] fortunes = new String[186];

    public FortuneItem(Item.Properties properties) {
        super(properties);

        Arrays.fill(fortunes, "0");

        for(int i = 0; i <= 185; i++) {
            fortunes[i] = "text.forcecraft.fortune" + i;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        CompoundNBT nbt;
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(stack.hasTag()) {
            nbt = stack.getTag();
        }
        else {
            nbt = new CompoundNBT();
        }

        if(!nbt.contains("message")) {
            addMessage(stack, nbt);
        }

        if(!worldIn.isRemote){
            playerIn.sendMessage(new StringTextComponent(I18n.format(nbt.getString("message"))), Util.DUMMY_UUID);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void addMessage(ItemStack stack, CompoundNBT nbt) {
        Random generator = new Random();
        int rand = generator.nextInt(fortunes.length);
        nbt.putString("message", fortunes[rand]);
        stack.setTag(nbt);
    }
}
