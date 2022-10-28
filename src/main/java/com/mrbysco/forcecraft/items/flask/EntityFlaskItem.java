package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.entities.projectile.FlaskEntity;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

/**
 * Credit to Buuz135 for the Mob Imprisonment Tool code <3
 */
public class EntityFlaskItem extends BaseItem {
	public EntityFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World worldIn = context.getLevel();
		ItemStack stack = context.getItemInHand();
		PlayerEntity playerIn = context.getPlayer();
		if (worldIn.isClientSide) return ActionResultType.FAIL;

		if (hasEntityStored(stack)) {
			Entity storedEntity = getStoredEntity(stack, worldIn);
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
			storedEntity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
			worldIn.addFreshEntity(storedEntity);

			CompoundNBT tag = stack.getOrCreateTag();
			tag.remove("StoredEntity");
			tag.remove("EntityData");
			stack.setTag(tag);
		} else {
			playerIn.sendMessage(new TranslationTextComponent("item.entity_flask.empty2").withStyle(TextFormatting.RED), Util.NIL_UUID);
		}

		stack.shrink(1);
		ItemStack emptyFlask = new ItemStack(ForceRegistry.FORCE_FLASK.get());
		if (!playerIn.inventory.add(emptyFlask)) {
			playerIn.spawnAtLocation(emptyFlask, 0F);
		}
		return super.useOn(context);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (playerIn.isShiftKeyDown()) {
			if (!hasEntityStored(itemstack)) {
				worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
				if (!worldIn.isClientSide) {
					FlaskEntity flaskEntity = new FlaskEntity(worldIn, playerIn);
					flaskEntity.setItem(itemstack);
					flaskEntity.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, -20.0F, 0.5F, 1.0F);
					worldIn.addFreshEntity(flaskEntity);
				}

				if (!playerIn.abilities.instabuild) {
					itemstack.shrink(1);
				}
			} else {
				playerIn.sendMessage(new TranslationTextComponent("item.entity_flask.empty").withStyle(TextFormatting.RED), Util.NIL_UUID);
			}
		}

		return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
	}

	public boolean hasEntityStored(ItemStack stack) {
		return stack.getOrCreateTag().contains("StoredEntity");
	}

	public Entity getStoredEntity(ItemStack stack, World worldIn) {
		EntityType<?> type = ForgeRegistries.ENTITIES.getValue(ResourceLocation.tryParse(stack.getTag().getString("StoredEntity")));
		if (type != null) {
			Entity entity = type.create(worldIn);
			entity.load(stack.getTag().getCompound("EntityData"));
			return entity;
		}
		return null;
	}

	public void storeEntity(ItemStack stack, LivingEntity livingEntity) {
		CompoundNBT tag = stack.getOrCreateTag();
		tag.putString("StoredEntity", EntityType.getKey(livingEntity.getType()).toString());

		CompoundNBT entityTag = new CompoundNBT();
		livingEntity.saveWithoutId(entityTag);
		tag.put("EntityData", entityTag);

		stack.setTag(tag);
		livingEntity.remove(true);
	}

	public boolean isBlacklisted(LivingEntity livingEntity) {
		return ForceTags.FLASK_BLACKLIST.contains(livingEntity.getType());
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		if (hasEntityStored(stack))
			return new TranslationTextComponent(super.getDescriptionId(stack), stack.getTag().getString("StoredEntity"));
		return new TranslationTextComponent(super.getDescriptionId(stack), "Empty");
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (hasEntityStored(stack)) {
			CompoundNBT tag = stack.getOrCreateTag();
			tooltip.add(new TranslationTextComponent("item.entity_flask.tooltip").withStyle(TextFormatting.GOLD).append(
					new StringTextComponent(String.format("[%s]", tag.getString("StoredEntity"))).withStyle(TextFormatting.GRAY)));
			if (tag.contains("EntityData")) {
				tooltip.add(new TranslationTextComponent("item.entity_flask.tooltip2").withStyle(TextFormatting.GOLD).append(
						new StringTextComponent(String.format("[%s]", tag.getCompound("EntityData").getDouble("Health"))).withStyle(TextFormatting.GRAY)));
			}
		}
	}
}
