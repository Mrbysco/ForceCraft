package com.mrbysco.forcecraft.items.flask;

import com.mrbysco.forcecraft.components.ForceComponents;
import com.mrbysco.forcecraft.components.flask.FlaskContent;
import com.mrbysco.forcecraft.entities.projectile.FlaskEntity;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import com.mrbysco.forcecraft.registry.ForceTags;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

/**
 * Credit to Buuz135 for the Mob Imprisonment Tool code <3
 */
public class EntityFlaskItem extends BaseItem {
	public EntityFlaskItem(Properties properties) {
		super(properties);
	}

	public EntityFlaskItem(Properties properties, EntityType<?> type) {
		this(properties.component(ForceComponents.FLASK_CONTENT, new FlaskContent(EntityType.getKey(type), new CompoundTag())));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		ItemStack stack = context.getItemInHand();
		Player playerIn = context.getPlayer();
		if (level.isClientSide()) return InteractionResult.FAIL;

		if (hasEntityStored(stack)) {
			Entity storedEntity = getStoredEntity(stack, level);
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
			storedEntity.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
			level.addFreshEntity(storedEntity);
			stack.remove(ForceComponents.FLASK_CONTENT);
		} else {
			if (playerIn != null)
				playerIn.sendSystemMessage(Component.translatable("item.entity_flask.empty2").withStyle(ChatFormatting.RED));
		}

		stack.shrink(1);
		ItemStack emptyFlask = new ItemStack(ForceRegistry.FORCE_FLASK.get());
		if (playerIn != null && !playerIn.getInventory().add(emptyFlask)) {
			playerIn.spawnAtLocation(emptyFlask, 0F);
		}
		return super.useOn(context);
	}

	@Override
	public InteractionResult use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (playerIn.isShiftKeyDown() && !level.isClientSide()) {
			if (hasEntityStored(itemstack)) {
				level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
						SoundEvents.SPLASH_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
				if (level.isClientSide()) {
					FlaskEntity flaskEntity = new FlaskEntity(level, playerIn, itemstack);
					flaskEntity.setItem(itemstack);
					flaskEntity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -20.0F, 0.5F, 1.0F);
					level.addFreshEntity(flaskEntity);
				}

				itemstack.consume(1, playerIn);
			} else {
				playerIn.sendSystemMessage(Component.translatable("item.entity_flask.empty").withStyle(ChatFormatting.RED));
			}
		}

		return InteractionResult.SUCCESS;
	}

	public boolean hasEntityStored(ItemStack stack) {
		return stack.has(ForceComponents.FLASK_CONTENT);
	}

	public Entity getStoredEntity(ItemStack stack, Level level) {
		FlaskContent content = stack.get(ForceComponents.FLASK_CONTENT);
		if (content == null) return null;
		Identifier resourceLocation = content.storedType();
		if (resourceLocation == null) return null;
		EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(resourceLocation);
		if (type != null) {
			Entity entity = type.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
			if (entity == null) return null;
			entity.load(content.entityData());
			return entity;
		}
		return null;
	}

	public void storeEntity(ItemStack stack, LivingEntity livingEntity) {
		CompoundTag entityData = livingEntity.saveWithoutId(new CompoundTag());
		FlaskContent content = new FlaskContent(EntityType.getKey(livingEntity.getType()), entityData);
		stack.set(ForceComponents.FLASK_CONTENT, content);
		livingEntity.discard();
	}

	public boolean isBlacklisted(LivingEntity livingEntity) {
		return livingEntity.is(ForceTags.FLASK_BLACKLIST);
	}

	@Override
	public Component getName(ItemStack stack) {
		FlaskContent content = stack.get(ForceComponents.FLASK_CONTENT);
		if (content != null) {
			String mobTranslation = Util.makeDescriptionId("entity", content.storedType());
			return Component.translatable(super.getDescriptionId(), Component.translatable(mobTranslation));
		}

		return Component.translatable(super.getDescriptionId(), "Empty");
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		if (hasEntityStored(itemStack)) {
			FlaskContent content = itemStack.get(ForceComponents.FLASK_CONTENT);
			builder.accept(Component.translatable("item.entity_flask.tooltip").withStyle(ChatFormatting.GOLD).append(
					Component.literal(String.format("[%s]", content.storedType().toString())).withStyle(ChatFormatting.GRAY)));
			if (content.entityData().contains("Health")) {
				builder.accept(Component.translatable("item.entity_flask.tooltip2").withStyle(ChatFormatting.GOLD).append(
						Component.literal(String.format("[%s]", content.entityData().getDouble("Health"))).withStyle(ChatFormatting.GRAY)));
			}
		}
	}
}
