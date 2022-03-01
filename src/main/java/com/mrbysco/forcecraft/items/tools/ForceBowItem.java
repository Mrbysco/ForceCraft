package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capabilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capabilities.toolmodifier.ToolModCapability;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceBowItem extends BowItem implements IForceChargingTool {
	public static final Predicate<ItemStack> FORCE_ARROWS = (stack) -> {
		return stack.getItem() instanceof ForceArrowItem;
	};

    public ForceBowItem(Properties properties) {
        super(properties.stacksTo(1).durability(332));
    }

	public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player playerentity) {
			boolean flag = playerentity.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
			ItemStack itemstack = playerentity.getProjectile(stack);

			int i = this.getUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, playerentity, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(ForceRegistry.FORCE_ARROW.get());
				}

				float f = getPowerForTime(i);
				if (!((double)f < 0.1D)) {
					boolean flag1 = playerentity.getAbilities().instabuild || (itemstack.getItem() instanceof ForceArrowItem && ((ForceArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
					if (!level.isClientSide) {
						ForceArrowItem arrowitem = (ForceArrowItem)(itemstack.getItem() instanceof ForceArrowItem ? itemstack.getItem() : ForceRegistry.FORCE_ARROW.get());
						AbstractArrow abstractarrowentity = arrowitem.createArrow(level, itemstack, playerentity);
						abstractarrowentity = customArrow(abstractarrowentity);
						abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, f * 3.0F, 1.0F);
						if (f == 1.0F) {
							abstractarrowentity.setCritArrow(true);
						}

						int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
						if (j > 0) {
							abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + (double)j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
						if (k > 0) {
							abstractarrowentity.setKnockback(k);
						}

						if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
							abstractarrowentity.setSecondsOnFire(100);
						}

						stack.hurtAndBreak(1, playerentity, (player) -> {
							player.broadcastBreakEvent(playerentity.getUsedItemHand());
						});
						if (flag1 || playerentity.getAbilities().instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
							abstractarrowentity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}

						level.addFreshEntity(abstractarrowentity);
					}

					level.playSound((Player)null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
							1.0F, 1.0F / (playerentity.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !playerentity.getAbilities().instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							playerentity.getInventory().removeItem(itemstack);
						}
					}

					playerentity.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    	if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModCapability();
    }
    
    // ShareTag for server->client capability data sync
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
    	CompoundTag normal = super.getShareTag(stack);

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			if(normal == null) {
				normal = new CompoundTag();
			}
			CompoundTag newTag = ToolModCapability.writeNBT(cap);
			normal.put(Reference.MOD_ID, newTag);
		}

        return normal;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
    	if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
    		return;
    	}

		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			CompoundTag shareTag = nbt.getCompound(Reference.MOD_ID);
			ToolModCapability.readNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(lores);
    	ToolModCapability.attachInformation(stack, lores);
        super.appendHoverText(stack, level, lores, flagIn);
    }

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}
	
	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return FORCE_ARROWS;
	}
}
