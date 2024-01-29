package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.attachment.toolmodifier.ToolModifierAttachment;
import com.mrbysco.forcecraft.entities.ColdChickenEntity;
import com.mrbysco.forcecraft.entities.ColdCowEntity;
import com.mrbysco.forcecraft.entities.ColdPigEntity;
import com.mrbysco.forcecraft.entities.IColdMob;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.attachment.CapabilityHandler.TOOL_MODIFIER;

public class ForceShearsItem extends ShearsItem implements IForceChargingTool {

	private static final int SET_FIRE_TIME = 10;
	private static final int SHEARS_DMG = 238; // vanilla shears have this max damage

	public ForceShearsItem(Item.Properties properties) {
		super(properties.stacksTo(1).durability(SHEARS_DMG * 4));
	}

	private static final Item[] WOOL = {Items.RED_WOOL, Items.BLUE_WOOL, Items.BLACK_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL, Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL,
			Items.CYAN_WOOL, Items.PURPLE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL};

	private ItemStack getRandomWool(Level level) {
		return new ItemStack(WOOL[Mth.nextInt(level.random, 0, WOOL.length)]);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand) {
		Level world = entity.level();
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}

		RandomSource rand = world.random;

		ToolModifierAttachment attachment = stack.getData(TOOL_MODIFIER);
		// dont drop wool from ALL IForgeShearable mobs, only sheep
		// for example: snow golems and Mooshroom are both IForgeShearable, but they
		// should not drop rainbow wool
		if (attachment.hasRainbow() && entity instanceof Sheep target) {

			BlockPos pos = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());

			if (target.isShearable(stack, world, pos)) {
				List<ItemStack> drops = target.onSheared(playerIn, stack, world, pos, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack));

				// get drops to test and see the COUNT of how many we should drop
				for (int i = 0; i < drops.size(); i++) {
					ItemEntity ent = entity.spawnAtLocation(getRandomWool(world), 1.0F);
					ent.setDeltaMovement(ent.getDeltaMovement().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				}

				stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(hand));
				return InteractionResult.SUCCESS;
			}
		}
		boolean hasHeat = attachment.hasHeat();

		// part 2 :
		if (!(entity instanceof IColdMob)) {
			if (entity instanceof Cow originalCow && !(entity instanceof MushroomCow)) {

				int i = 1 + rand.nextInt(3);

				List<ItemStack> drops = new ArrayList<>();
				for (int j = 0; j < i; ++j)
					drops.add(new ItemStack(Items.LEATHER, 1));

				drops.forEach(d -> {
					ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
					ent.setDeltaMovement(ent.getDeltaMovement().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				});

				entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);

				Mob replacementMob = new ColdCowEntity(world, BuiltInRegistries.ENTITY_TYPE.getKey(originalCow.getType()));
				replacementMob.copyPosition(originalCow);
				UUID mobUUID = replacementMob.getUUID();
				replacementMob.restoreFrom(originalCow);
				replacementMob.setUUID(mobUUID);

				originalCow.remove(RemovalReason.DISCARDED);
				world.addFreshEntity(replacementMob);
				if (hasHeat) {
					replacementMob.setSecondsOnFire(SET_FIRE_TIME);
				}

				stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(hand));
				return InteractionResult.SUCCESS;
			}
			if (entity instanceof Chicken originalChicken) {
				Level level = originalChicken.level();

				int i = 1 + rand.nextInt(3);

				java.util.List<ItemStack> drops = new ArrayList<>();
				for (int j = 0; j < i; ++j) {
					drops.add(new ItemStack(Items.FEATHER, 1));
				}

				drops.forEach(d -> {
					ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
					ent.setDeltaMovement(ent.getDeltaMovement().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				});

				entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);

				Mob replacementMob = new ColdChickenEntity(level, BuiltInRegistries.ENTITY_TYPE.getKey(originalChicken.getType()));
				replacementMob.copyPosition(originalChicken);
				UUID mobUUID = replacementMob.getUUID();
				replacementMob.restoreFrom(originalChicken);
				replacementMob.setUUID(mobUUID);

				originalChicken.remove(RemovalReason.DISCARDED);
				level.addFreshEntity(replacementMob);
				if (hasHeat) {
					replacementMob.setSecondsOnFire(SET_FIRE_TIME);
				}

				stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(hand));
				return InteractionResult.SUCCESS;
			}
			if (entity instanceof Pig originalPig) {
				Level level = originalPig.level();

				int i = 1 + rand.nextInt(2);

				java.util.List<ItemStack> drops = new ArrayList<>();
				for (int j = 0; j < i; ++j)
					drops.add(new ItemStack(hasHeat ? ForceRegistry.COOKED_BACON.get() : ForceRegistry.RAW_BACON.get(), 1));

				drops.forEach(d -> {
					net.minecraft.world.entity.item.ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
					ent.setDeltaMovement(ent.getDeltaMovement().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				});

				entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);

				Mob replacementMob = new ColdPigEntity(level, BuiltInRegistries.ENTITY_TYPE.getKey(originalPig.getType()));
				replacementMob.copyPosition(originalPig);
				UUID mobUUID = replacementMob.getUUID();
				replacementMob.restoreFrom(originalPig);
				replacementMob.setUUID(mobUUID);

				originalPig.remove(RemovalReason.DISCARDED);
				level.addFreshEntity(replacementMob);
				if (hasHeat) {
					replacementMob.setSecondsOnFire(SET_FIRE_TIME);
				}

				stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(hand));
				return InteractionResult.SUCCESS;
			}
		}

		return super.interactLivingEntity(stack, playerIn, entity, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
		ForceToolData fd = new ForceToolData(stack);
		fd.attachInformation(lores);
		ToolModifierAttachment.attachInformation(stack, lores);
		super.appendHoverText(stack, level, lores, flagIn);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
}
