package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.forcerod.ForceRodProvider;
import mrbysco.forcecraft.capablilities.forcerod.ForceRodStorage;
import mrbysco.forcecraft.capablilities.forcerod.IForceRodModifier;
import mrbysco.forcecraft.items.BaseItem;
import mrbysco.forcecraft.items.infuser.ForceToolData;
import mrbysco.forcecraft.items.infuser.IForceChargingTool;
import mrbysco.forcecraft.items.nonburnable.InertCoreItem;
import mrbysco.forcecraft.items.nonburnable.NonBurnableItemEntity;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.block.FireBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_ENDER;
import static mrbysco.forcecraft.Reference.MODIFIERS.MOD_HEALING;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;

public class ForceRodItem extends BaseItem implements IForceChargingTool {

	public List<Reference.MODIFIERS> applicableModifiers = new ArrayList<>();

	public ForceRodItem(Item.Properties properties) {
		super(properties.maxDamage(75));
		setApplicableModifiers();
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		Direction facing = context.getFace();
		PlayerEntity player = context.getPlayer();
		Hand handIn = context.getHand();
		ItemStack stack = context.getItem();
		if (!worldIn.isRemote && player != null) {
			if (worldIn.getBlockState(pos).getBlock() instanceof FireBlock) {
				worldIn.removeBlock(pos, false);
				List<Entity> list = worldIn.getEntitiesWithinAABB(ItemEntity.class,
						new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).expand(0.5, 1, 0.5));
				boolean bw = false;
				for (Entity i : list) {
					if (i instanceof ItemEntity) {
						if (((ItemEntity) i).getItem().getItem() instanceof InertCoreItem) {
							ItemEntity bottledWither = new NonBurnableItemEntity(worldIn, pos.getX(), pos.getY() + 1,
									pos.getZ(), new ItemStack(ForceRegistry.BOTTLED_WITHER.get(),
											((ItemEntity) i).getItem().getCount()));
							worldIn.addEntity(bottledWither);
							stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
							bw = true;
						}
					}
				}
				if (bw) {
					for (Entity i : list) {
						if (i instanceof ItemEntity) {
							if (((ItemEntity) i).getItem().getItem() instanceof InertCoreItem) {
								i.remove();
							}
						}
					}
				}
			} else {
				List<Entity> list = worldIn.getEntitiesWithinAABB(ItemEntity.class,
						new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).expand(0.5, 1, 0.5));
				// If it is a subset of items, it will drop swap an item
				for (Entity i : list) {
					if (i instanceof ItemEntity) {
						// Armor
						if (((ItemEntity) i).getItem().getItem() instanceof ArmorItem) {
							if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
									.getEquipmentSlot() == EquipmentSlotType.CHEST) {
								if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.IRON) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 6)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.GOLD) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 6)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.LEATHER) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_CHEST.get(), 1)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								}
							}
							if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
									.getEquipmentSlot() == EquipmentSlotType.LEGS) {
								if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.IRON) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 5)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.GOLD) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 5)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.LEATHER) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_LEGS.get(), 1)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								}
							}
							if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
									.getEquipmentSlot() == EquipmentSlotType.FEET) {
								if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.IRON) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 3)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.GOLD) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 3)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.LEATHER) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_BOOTS.get(), 1)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								}
							}
							if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
									.getEquipmentSlot() == EquipmentSlotType.HEAD) {
								if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.IRON) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.IRON_INGOT, 4)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.GOLD) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(Items.GOLD_INGOT, 4)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								} else if (((ArmorItem) ((ItemEntity) i).getItem().getItem())
										.getArmorMaterial() == ArmorMaterial.LEATHER) {
									i.remove();
									worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
											new ItemStack(ForceRegistry.FORCE_HELMET.get(), 1)));
									stack.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
								}
							}
						}
					}
				}
			}
		}

		stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
			if (cap.hasEnderModifier()) {
				if(player.isSneaking()) {
					cap.setHomeLocation(GlobalPos.getPosition(player.world.getDimensionKey(), player.getPosition()));
					if(!worldIn.isRemote) {
						player.sendStatusMessage(new TranslationTextComponent("forcecraft.ender_rod.location.set").mergeStyle(TextFormatting.DARK_PURPLE), true);
					}
				} else {
					if(cap.getHomeLocation() != null) {
						cap.teleportPlayerToLocation(player, cap.getHomeLocation());
						stack.damageItem(1, player, (playerIn) -> player.sendBreakAnimation(handIn));
						player.getCooldownTracker().setCooldown(this, 10);
						worldIn.playSound((PlayerEntity)null, player.prevPosX, player.prevPosY, player.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, player.getSoundCategory(), 1.0F, 1.0F);
						player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
					}
				}
			}
		});

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if(playerIn != null) {
			stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
				if(cap.getHealingLevel() > 0) {
					int healingLevel = cap.getHealingLevel();
					playerIn.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, healingLevel - 1, false, false));
					stack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
					playerIn.getCooldownTracker().setCooldown(this, 10);
				}

				if (cap.hasCamoModifier()) {
					playerIn.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 1000, 0, false, false));
					stack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
					playerIn.getCooldownTracker().setCooldown(this, 10);
				}

				if (cap.hasEnderModifier()) {
					if(playerIn.isSneaking()) {
						cap.setHomeLocation(GlobalPos.getPosition(playerIn.world.getDimensionKey(), playerIn.getPosition()));
					} else {
						if(cap.getHomeLocation() != null) {
							cap.teleportPlayerToLocation(playerIn, cap.getHomeLocation());
							stack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
							playerIn.getCooldownTracker().setCooldown(this, 10);
							worldIn.playSound((PlayerEntity)null, playerIn.prevPosX, playerIn.prevPosY, playerIn.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, playerIn.getSoundCategory(), 1.0F, 1.0F);
							playerIn.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
						}
					}
				}

				if (cap.hasSightModifier()) {
					playerIn.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1000, 0, false, false));
					stack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
					playerIn.getCooldownTracker().setCooldown(this, 10);
				}

				if (cap.hasLight()) {
					playerIn.addPotionEffect(new EffectInstance(Effects.GLOWING, 1000, 0, false, false));
					stack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
					playerIn.getCooldownTracker().setCooldown(this, 10);
				}
				if (cap.getSpeedLevel() > 0) {
					playerIn.addPotionEffect(new EffectInstance(Effects.SPEED, 10 * 20, cap.getSpeedLevel() - 1, false, false));
					stack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
					playerIn.getCooldownTracker().setCooldown(this, 10);
				}
			});
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand handIn) {
		if(playerIn != null) {
			stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
				if(cap.hasLight()) {
					target.addPotionEffect(new EffectInstance(Effects.GLOWING, 2400, 0, false, false));
					stack.damageItem(1, playerIn, (player) -> player.sendBreakAnimation(handIn));
					playerIn.getCooldownTracker().setCooldown(this, 10);
				}
			});
		}
		return super.itemInteractionForEntity(stack, playerIn, target, handIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (CAPABILITY_FORCEROD == null) {
			return null;
		}
		return new ForceRodProvider();
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
	public CompoundNBT getShareTag(ItemStack stack) {
		CompoundNBT nbt = super.getShareTag(stack);

		IForceRodModifier cap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
		if(cap != null) {
			CompoundNBT shareTag = ForceRodStorage.serializeNBT(cap);
			nbt.put(Reference.MOD_ID, shareTag);
		}
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
			return;
		}

		IForceRodModifier cap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
		if(cap != null) {
			INBT shareTag = nbt.get(Reference.MOD_ID);
			ForceRodStorage.deserializeNBT(cap, shareTag);
		}
		super.readShareTag(stack, nbt);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
		ForceToolData fd = new ForceToolData(stack);
		fd.attachInformation(lores);
		ForceRodStorage.attachInformation(stack, lores);
		super.addInformation(stack, worldIn, lores, flagIn);
	}
}