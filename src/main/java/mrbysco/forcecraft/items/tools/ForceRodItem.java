package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.forcerod.ForceRodProvider;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
			if (worldIn.getBlockState(pos.offset(facing)).getBlock() instanceof FireBlock) {
				worldIn.removeBlock(pos.offset(facing), false);
				List<Entity> list = worldIn.getEntitiesWithinAABB(ItemEntity.class,
						new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())));
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
						new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())));
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
			if (cap.getHomeLocation() != null) {
				cap.teleportPlayerToLocation(player, cap.getHomeLocation());
			}
		});

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
			if (cap.isRodOfHealing(3)) {
				playerIn.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 2, false, false));
			} else if (cap.isRodOfHealing(2)) {
				playerIn.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 1, false, false));
			} else if (cap.isRodOfHealing(1)) {
				playerIn.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 0, false, false));
			}

			if (cap.hasCamoModifier())
				playerIn.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 1000, 0, false, false));

			if (cap.hasEnderModifier()) {
				if (cap.getHomeLocation() == null) {
					cap.setHomeLocation(playerIn.getPosition());
				} else {
					cap.teleportPlayerToLocation(playerIn, cap.getHomeLocation());
				}
			}

			if (cap.hasSightModifier()) {
				playerIn.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1000, 0, false, false));
			}

			if (cap.hasLight()) {
				playerIn.addPotionEffect(new EffectInstance(Effects.GLOWING, 1000, 0, false, false));
			}
			if (cap.getSpeedLevel() > 0) {
				playerIn.addPotionEffect(new EffectInstance(Effects.SPEED, 10 * 20, cap.getSpeedLevel() - 1, false, false));
			}
		});

		return super.onItemRightClick(worldIn, playerIn, handIn);
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
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(tooltip);
    	
		stack.getCapability(CAPABILITY_FORCEROD).ifPresent((cap) -> {
			if (cap.isRodOfHealing(3)) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.healing3").mergeStyle(TextFormatting.RED));
			} else if (cap.isRodOfHealing(2)) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.healing2").mergeStyle(TextFormatting.RED));
			} else if (cap.isRodOfHealing(1)) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.healing1").mergeStyle(TextFormatting.RED));
			}
			if (cap.getSpeedLevel() > 0) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.speed" + cap.getSpeedLevel()));
			}
			if (cap.hasCamoModifier()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.camo").mergeStyle(TextFormatting.DARK_GREEN));
			}
			if (cap.hasEnderModifier()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.ender").mergeStyle(TextFormatting.DARK_PURPLE));
			}
			if (cap.hasSightModifier()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.sight").mergeStyle(TextFormatting.LIGHT_PURPLE));
			}
			if (cap.hasLight()) {
				tooltip.add(new TranslationTextComponent("item.infuser.tooltip.light").mergeStyle(TextFormatting.YELLOW));
			}
		});
	}
}