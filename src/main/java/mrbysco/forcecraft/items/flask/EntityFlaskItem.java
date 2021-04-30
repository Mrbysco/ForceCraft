package mrbysco.forcecraft.items.flask;

import mrbysco.forcecraft.entities.projectile.FlaskEntity;
import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.registry.ForceTags;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Credit to Buuz135 for the Mob Imprisonment Tool code <3
 */
public class EntityFlaskItem extends BaseItem {
	public EntityFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World worldIn = context.getWorld();
		ItemStack stack = context.getItem();
		PlayerEntity playerIn = context.getPlayer();
		if (worldIn.isRemote || hasEntityStored(stack)) return ActionResultType.FAIL;

		Entity storedEntity = getStoredEntity(stack, worldIn);
		BlockPos pos = context.getPos().offset(context.getFace());
		storedEntity.setPositionAndRotation(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
		worldIn.addEntity(storedEntity);

		CompoundNBT tag = stack.getOrCreateTag();
		tag.remove("StoredEntity");
		tag.remove("EntityData");
		stack.setTag(tag);

		stack.shrink(1);
		ItemStack emptyFlask = new ItemStack(ForceRegistry.FORCE_FLASK.get());
		if(!playerIn.inventory.addItemStackToInventory(emptyFlask)) {
			playerIn.entityDropItem(emptyFlask, 0F);
		}
		return super.onItemUse(context);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if(playerIn.isSneaking()) {
			worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
			if (!worldIn.isRemote) {
				FlaskEntity flaskEntity = new FlaskEntity(worldIn, playerIn);
				flaskEntity.setItem(itemstack);
				flaskEntity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
				worldIn.addEntity(flaskEntity);
			}

			if (!playerIn.abilities.isCreativeMode) {
				itemstack.shrink(1);
			}
		}

		return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
	}

	public boolean hasEntityStored(ItemStack stack) {
		return stack.getOrCreateTag().contains("StoredEntity");
	}

	public Entity getStoredEntity(ItemStack stack, World worldIn) {
		EntityType type = ForgeRegistries.ENTITIES.getValue(ResourceLocation.tryCreate(stack.getTag().getString("StoredEntity")));
		if (type != null) {
			Entity entity = type.create(worldIn);
			entity.read(stack.getTag().getCompound("EntityData"));
			return entity;
		}
		return null;
	}

	public void storeEntity(ItemStack stack, LivingEntity livingEntity) {
		CompoundNBT tag = stack.getOrCreateTag();
		tag.putString("StoredEntity", EntityType.getKey(livingEntity.getType()).toString());

		CompoundNBT entityTag = new CompoundNBT();
		livingEntity.writeWithoutTypeId(entityTag);
		tag.put("EntityData", entityTag);

		stack.setTag(tag);
		livingEntity.remove(true);
	}

	public boolean isBlacklisted(LivingEntity livingEntity) {
		return ForceTags.FLASK_BLACKLIST.contains(livingEntity.getType());
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		if (hasEntityStored(stack))
			return new TranslationTextComponent(super.getTranslationKey(stack), stack.getTag().getString("StoredEntity"));
		return new TranslationTextComponent(super.getTranslationKey(stack), "Empty");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (hasEntityStored(stack)) {
			CompoundNBT tag = stack.getOrCreateTag();
			tooltip.add(new TranslationTextComponent("item.entity_flask.tooltip").mergeStyle(TextFormatting.GOLD).appendSibling(
					new StringTextComponent(String.format("[%s]", tag.getString("StoredEntity"))).mergeStyle(TextFormatting.GRAY)));
			if(tag.contains("EntityData")) {
				tooltip.add(new TranslationTextComponent("item.entity_flask.tooltip2").mergeStyle(TextFormatting.GOLD).appendSibling(
						new StringTextComponent(String.format("[%s]", tag.getCompound("EntityData").getDouble("Health"))).mergeStyle(TextFormatting.GRAY)));
			}
		} else {
			tooltip.add(new TranslationTextComponent("item.entity_flask.empty").mergeStyle(TextFormatting.RED));
			tooltip.add(new TranslationTextComponent("item.entity_flask.empty2").mergeStyle(TextFormatting.RED));
		}
	}
}
