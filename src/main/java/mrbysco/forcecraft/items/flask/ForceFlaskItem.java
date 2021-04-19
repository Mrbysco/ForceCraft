package mrbysco.forcecraft.items.flask;

import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ForceFlaskItem extends BaseItem {

    public ForceFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand) {
        World world = entity.world;
        if (world.isRemote)
            return ActionResultType.PASS;

        if(entity instanceof CowEntity && !entity.isChild()) {
            playerIn.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            ItemStack milkStack = ForceRegistry.MILK_FORCE_FLASK.get().getDefaultInstance();
            if(!playerIn.inventory.addItemStackToInventory(milkStack)) {
                playerIn.entityDropItem(milkStack, 0F);
            }
            if(!playerIn.abilities.isCreativeMode)
                stack.shrink(1);

            return ActionResultType.SUCCESS;
        }
        return super.itemInteractionForEntity(stack, playerIn, entity, hand);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @javax.annotation.Nullable net.minecraft.nbt.CompoundNBT nbt) {
        return new FlaskFluidHandler(stack);
    }
}
