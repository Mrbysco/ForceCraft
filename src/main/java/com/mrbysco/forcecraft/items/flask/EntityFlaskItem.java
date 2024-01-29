package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.entities.projectile.FlaskEntity;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Credit to Buuz135 for the Mob Imprisonment Tool code <3
 */
public class EntityFlaskItem extends BaseItem {
	public EntityFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		ItemStack stack = context.getItemInHand();
		Player playerIn = context.getPlayer();
		if (level.isClientSide) return InteractionResult.FAIL;

		if (hasEntityStored(stack)) {
			Entity storedEntity = getStoredEntity(stack, level);
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
			storedEntity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
			level.addFreshEntity(storedEntity);

			CompoundTag tag = stack.getOrCreateTag();
			tag.remove("StoredEntity");
			tag.remove("EntityData");
			stack.setTag(tag);
		} else {
			playerIn.sendSystemMessage(Component.translatable("item.entity_flask.empty2").withStyle(ChatFormatting.RED));
		}

		stack.shrink(1);
		ItemStack emptyFlask = new ItemStack(ForceRegistry.FORCE_FLASK.get());
		if (!playerIn.getInventory().add(emptyFlask)) {
			playerIn.spawnAtLocation(emptyFlask, 0F);
		}
		return super.useOn(context);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (playerIn.isShiftKeyDown()) {
			if (!hasEntityStored(itemstack)) {
				level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
						SoundEvents.SPLASH_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
				if (!level.isClientSide) {
					FlaskEntity flaskEntity = new FlaskEntity(level, playerIn);
					flaskEntity.setItem(itemstack);
					flaskEntity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -20.0F, 0.5F, 1.0F);
					level.addFreshEntity(flaskEntity);
				}

				if (!playerIn.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
			} else {
				playerIn.sendSystemMessage(Component.translatable("item.entity_flask.empty").withStyle(ChatFormatting.RED));
			}
		}

		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}

	public boolean hasEntityStored(ItemStack stack) {
		return stack.getOrCreateTag().contains("StoredEntity");
	}

	public Entity getStoredEntity(ItemStack stack, Level level) {
		EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(stack.getTag().getString("StoredEntity")));
		if (type != null) {
			Entity entity = type.create(level);
			entity.load(stack.getTag().getCompound("EntityData"));
			return entity;
		}
		return null;
	}

	public void storeEntity(ItemStack stack, LivingEntity livingEntity) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putString("StoredEntity", EntityType.getKey(livingEntity.getType()).toString());

		CompoundTag entityTag = new CompoundTag();
		livingEntity.saveWithoutId(entityTag);
		tag.put("EntityData", entityTag);

		stack.setTag(tag);
		livingEntity.discard();
	}

	public boolean isBlacklisted(LivingEntity livingEntity) {
		return livingEntity.getType().is(ForceTags.FLASK_BLACKLIST);
	}

	@Override
	public Component getName(ItemStack stack) {
		if (hasEntityStored(stack))
			return Component.translatable(super.getDescriptionId(stack), stack.getTag().getString("StoredEntity"));
		return Component.translatable(super.getDescriptionId(stack), "Empty");
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, level, tooltip, flagIn);
		if (hasEntityStored(stack)) {
			CompoundTag tag = stack.getOrCreateTag();
			tooltip.add(Component.translatable("item.entity_flask.tooltip").withStyle(ChatFormatting.GOLD).append(
					Component.literal(String.format("[%s]", tag.getString("StoredEntity"))).withStyle(ChatFormatting.GRAY)));
			if (tag.contains("EntityData")) {
				tooltip.add(Component.translatable("item.entity_flask.tooltip2").withStyle(ChatFormatting.GOLD).append(
						Component.literal(String.format("[%s]", tag.getCompound("EntityData").getDouble("Health"))).withStyle(ChatFormatting.GRAY)));
			}
		}
	}
}
