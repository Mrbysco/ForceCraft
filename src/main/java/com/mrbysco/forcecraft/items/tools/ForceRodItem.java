package com.mrbysco.forcecraft.items.tools;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capabilities.forcerod.ForceRodCapability;
import com.mrbysco.forcecraft.capabilities.forcerod.IForceRodModifier;
import com.mrbysco.forcecraft.items.BaseItem;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.items.nonburnable.InertCoreItem;
import com.mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import com.mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_ENDER;
import static com.mrbysco.forcecraft.Reference.MODIFIERS.MOD_HEALING;
import static com.mrbysco.forcecraft.capabilities.CapabilityHandler.CAPABILITY_FORCEROD;

public class ForceRodItem extends BaseItem implements IForceChargingTool {

	public List<Reference.MODIFIERS> applicableModifiers = new ArrayList<>();

	public ForceRodItem(Item.Properties properties) {
		super(properties.durability(75));
		setApplicableModifiers();
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getClickedFace();
		Player player = context.getPlayer();
		InteractionHand handIn = context.getHand();
		ItemStack stack = context.getItemInHand();
		if (!level.isClientSide && player != null) {
			if (level.getBlockState(pos).getBlock() instanceof FireBlock) {
				level.removeBlock(pos, false);
				List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class,
						new AABB(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).expandTowards(0.5, 1, 0.5));
				boolean bw = false;
				for (ItemEntity itemEntity : list) {
					if (itemEntity != null) {
						if (itemEntity.getItem().getItem() instanceof InertCoreItem) {
							ItemEntity bottledWither = new NonBurnableItemEntity(level, pos.getX(), pos.getY() + 1,
									pos.getZ(), new ItemStack(ForceRegistry.BOTTLED_WITHER.get(),
									itemEntity.getItem().getCount()));
							level.addFreshEntity(bottledWither);
							stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
							bw = true;
						}
					}
				}
				if (bw) {
					for (ItemEntity itemEntity : list) {
						if (itemEntity != null) {
							if (itemEntity.getItem().getItem() instanceof InertCoreItem) {
								itemEntity.discard();
							}
						}
					}
				}
			} else {
				List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class,
						new AABB(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).expandTowards(0.5, 1, 0.5));
				// If it is a subset of items, it will drop swap an item
				for (ItemEntity itemEntity : list) {
					if (itemEntity != null) {
						// Armor
						if (itemEntity.getItem().getItem() instanceof ArmorItem) {
							if (((ArmorItem) itemEntity.getItem().getItem())
									.getSlot() == EquipmentSlot.CHEST) {
								if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.IRON) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 6)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.GOLD) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 6)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.LEATHER) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_CHEST.get(), 1)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								}
							}
							if (((ArmorItem) itemEntity.getItem().getItem())
									.getSlot() == EquipmentSlot.LEGS) {
								if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.IRON) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 5)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.GOLD) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 5)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.LEATHER) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_LEGS.get(), 1)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								}
							}
							if (((ArmorItem) itemEntity.getItem().getItem())
									.getSlot() == EquipmentSlot.FEET) {
								if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.IRON) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 3)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.GOLD) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 3)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.LEATHER) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_BOOTS.get(), 1)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								}
							}
							if (((ArmorItem) itemEntity.getItem().getItem())
									.getSlot() == EquipmentSlot.HEAD) {
								if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.IRON) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 4)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.GOLD) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 4)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								} else if (((ArmorItem) itemEntity.getItem().getItem())
										.getMaterial() == ArmorMaterials.LEATHER) {
									itemEntity.discard();
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_HELMET.get(), 1)));
									stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
								}
							}
						}
					}
				}
			}
		}

		stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
			if (cap.hasEnderModifier()) {
				if (player.isShiftKeyDown()) {
					cap.setHomeLocation(GlobalPos.of(player.level.dimension(), player.blockPosition()));
					if (!level.isClientSide) {
						player.displayClientMessage(new TranslatableComponent("forcecraft.ender_rod.location.set").withStyle(ChatFormatting.DARK_PURPLE), true);
					}
				} else {
					if (cap.getHomeLocation() != null) {
						cap.teleportPlayerToLocation(player, cap.getHomeLocation());
						stack.hurtAndBreak(1, player, (playerIn) -> player.broadcastBreakEvent(handIn));
						player.getCooldowns().addCooldown(this, 10);
						level.playSound((Player) null, player.xo, player.yo, player.zo, SoundEvents.ENDERMAN_TELEPORT, player.getSoundSource(), 1.0F, 1.0F);
						player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					}
				}
			}
		});

		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn != null) {
			stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
				if (cap.getHealingLevel() > 0) {
					int healingLevel = cap.getHealingLevel();
					playerIn.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, healingLevel - 1, false, false));
					stack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
					playerIn.getCooldowns().addCooldown(this, 10);
				}

				if (cap.hasCamoModifier()) {
					playerIn.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1000, 0, false, false));
					stack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
					playerIn.getCooldowns().addCooldown(this, 10);
				}

				if (cap.hasEnderModifier()) {
					if (playerIn.isShiftKeyDown()) {
						cap.setHomeLocation(GlobalPos.of(playerIn.level.dimension(), playerIn.blockPosition()));
					} else {
						if (cap.getHomeLocation() != null) {
							cap.teleportPlayerToLocation(playerIn, cap.getHomeLocation());
							stack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
							playerIn.getCooldowns().addCooldown(this, 10);
							level.playSound((Player) null, playerIn.xo, playerIn.yo, playerIn.zo, SoundEvents.ENDERMAN_TELEPORT, playerIn.getSoundSource(), 1.0F, 1.0F);
							playerIn.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
						}
					}
				}

				if (cap.hasSightModifier()) {
					playerIn.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000, 0, false, false));
					stack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
					playerIn.getCooldowns().addCooldown(this, 10);
				}

				if (cap.hasLight()) {
					playerIn.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1000, 0, false, false));
					stack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
					playerIn.getCooldowns().addCooldown(this, 10);
				}
				if (cap.getSpeedLevel() > 0) {
					playerIn.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10 * 20, cap.getSpeedLevel() - 1, false, false));
					stack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
					playerIn.getCooldowns().addCooldown(this, 10);
				}
			});
		}

		return super.use(level, playerIn, handIn);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand handIn) {
		if (playerIn != null) {
			stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
				if (cap.hasLight()) {
					target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2400, 0, false, false));
					stack.hurtAndBreak(1, playerIn, (player) -> player.broadcastBreakEvent(handIn));
					playerIn.getCooldowns().addCooldown(this, 10);
				}
			});
		}
		return super.interactLivingEntity(stack, playerIn, target, handIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if (CAPABILITY_FORCEROD == null) {
			return null;
		}
		return new ForceRodCapability();
	}

	public void setApplicableModifiers() {
		applicableModifiers.add(MOD_HEALING);
		applicableModifiers.add(MOD_ENDER);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}

	// ShareTag for server->client capability data sync
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = super.getShareTag(stack);

		IForceRodModifier cap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
		if (cap != null) {
			CompoundTag shareTag = ForceRodCapability.writeNBT(cap);
			nbt.put(Reference.MOD_ID, shareTag);
		}
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		if (nbt == null || !nbt.contains(Reference.MOD_ID)) {
			return;
		}

		IForceRodModifier cap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
		if (cap != null) {
			CompoundTag shareTag = nbt.getCompound(Reference.MOD_ID);
			ForceRodCapability.readNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lores, TooltipFlag flagIn) {
		ForceToolData fd = new ForceToolData(stack);
		fd.attachInformation(lores);
		ForceRodCapability.attachInformation(stack, lores);
		super.appendHoverText(stack, level, lores, flagIn);
	}
}