package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import mrbysco.forcecraft.entities.ColdChickenEntity;
import mrbysco.forcecraft.entities.ColdCowEntity;
import mrbysco.forcecraft.entities.ColdPigEntity;
import mrbysco.forcecraft.entities.IColdMob;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceShearsItem extends ShearsItem {

	private static final int SET_FIRE_TIME = 10;
	private static final int SHEARS_DMG = 238; // vanilla shears have this max damage
	public ForceShearsItem(Item.Properties properties) {
		super(properties.maxStackSize(1).maxDamage(SHEARS_DMG * 4));
	}

	private static final Item[] WOOL = { Items.RED_WOOL, Items.BLUE_WOOL, Items.BLACK_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL, Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL,
			Items.CYAN_WOOL, Items.PURPLE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL };

	private ItemStack getRandomWool(World world) {
		return new ItemStack(WOOL[MathHelper.nextInt(world.rand, 0, WOOL.length)]);
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand) {
		World world = entity.world;
		if (world.isRemote) {
			return ActionResultType.PASS;
		}

		Random rand = world.rand;

		IToolModifier toolModifier = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);

		// dont drop wool from ALL IForgeShearable mobs, only sheep
		// for example: snow golems and Mooshroom are both IForgeShearable, but they
		// should not drop rainbow wool
		if (toolModifier != null && toolModifier.hasRainbow() && entity instanceof SheepEntity) {

			SheepEntity target = (SheepEntity) entity;
			BlockPos pos = new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ());

			if (target.isShearable(stack, world, pos)) {
				List<ItemStack> drops = target.onSheared(playerIn, stack, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

				// get drops to test and see the COUNT of how many we should drop
				for (int i = 0; i < drops.size(); i++) {
					ItemEntity ent = entity.entityDropItem(getRandomWool(world), 1.0F);
					ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				}

				stack.damageItem(1, entity, e -> e.sendBreakAnimation(hand));

				return ActionResultType.SUCCESS;
			}
		}
		boolean hasHeat = toolModifier != null && toolModifier.hasHeat();

		// part 2 :
		if (!(entity instanceof IColdMob)) {
			if (entity instanceof CowEntity && !(entity instanceof MooshroomEntity)) {
				CowEntity originalCow = (CowEntity) entity;

				int i = 1 + rand.nextInt(3);

				List<ItemStack> drops = new ArrayList<>();
				for (int j = 0; j < i; ++j)
					drops.add(new ItemStack(Items.LEATHER, 1));

				drops.forEach(d -> {
					ItemEntity ent = entity.entityDropItem(d, 1.0F);
					ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				});

				entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);

				MobEntity replacementMob = new ColdCowEntity(world, originalCow.getType().getRegistryName());
				replacementMob.copyLocationAndAnglesFrom(originalCow);
				UUID mobUUID = replacementMob.getUniqueID();
				replacementMob.copyDataFromOld(originalCow);
				replacementMob.setUniqueId(mobUUID);

				originalCow.remove(false);
				world.addEntity(replacementMob);
				if (hasHeat) {
					replacementMob.setFire(SET_FIRE_TIME);
				}

				return ActionResultType.SUCCESS;
			}
			if (entity instanceof ChickenEntity) {
				ChickenEntity originalChicken = (ChickenEntity) entity;
				World worldIn = originalChicken.world;

				int i = 1 + rand.nextInt(3);

				java.util.List<ItemStack> drops = new ArrayList<>();
				for (int j = 0; j < i; ++j) {
					drops.add(new ItemStack(Items.FEATHER, 1));
				}

				drops.forEach(d -> {
					ItemEntity ent = entity.entityDropItem(d, 1.0F);
					ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				});

				entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);

				MobEntity replacementMob = new ColdChickenEntity(worldIn, originalChicken.getType().getRegistryName());
				replacementMob.copyLocationAndAnglesFrom(originalChicken);
				UUID mobUUID = replacementMob.getUniqueID();
				replacementMob.copyDataFromOld(originalChicken);
				replacementMob.setUniqueId(mobUUID);

				originalChicken.remove(false);
				worldIn.addEntity(replacementMob);
				if (hasHeat) {
					replacementMob.setFire(SET_FIRE_TIME);
				}

				return ActionResultType.SUCCESS;
			}
			if (entity instanceof PigEntity) {
				PigEntity originalPig = (PigEntity) entity;
				World worldIn = originalPig.world;

				int i = 1 + rand.nextInt(2);

				java.util.List<ItemStack> drops = new ArrayList<>();
				for (int j = 0; j < i; ++j)
					drops.add(new ItemStack(hasHeat ? ForceRegistry.COOKED_BACON.get() : ForceRegistry.RAW_BACON.get(), 1));

				drops.forEach(d -> {
					net.minecraft.entity.item.ItemEntity ent = entity.entityDropItem(d, 1.0F);
					ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
				});

				entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);

				MobEntity replacementMob = new ColdPigEntity(worldIn, originalPig.getType().getRegistryName());
				replacementMob.copyLocationAndAnglesFrom(originalPig);
				UUID mobUUID = replacementMob.getUniqueID();
				replacementMob.copyDataFromOld(originalPig);
				replacementMob.setUniqueId(mobUUID);

				originalPig.remove(false);
				worldIn.addEntity(replacementMob);
				if (hasHeat) {
					replacementMob.setFire(SET_FIRE_TIME);
				}

				return ActionResultType.SUCCESS;
			}
		}

		return super.itemInteractionForEntity(stack, playerIn, entity, hand);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		if (CAPABILITY_TOOLMOD == null) {
			return null;
		}
		return new ToolModProvider();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
		ToolModStorage.attachInformation(stack, lores);
		super.addInformation(stack, worldIn, lores, flagIn);
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}    
	
	@Override
    public CompoundNBT getShareTag(ItemStack stack) {
    	CompoundNBT nbt = super.getShareTag(stack);
    	
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			CompoundNBT shareTag = ToolModStorage.serializeNBT(cap);
			nbt.put(Reference.MOD_ID, shareTag);
		}
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
    	super.readShareTag(stack, nbt); 
    	if(nbt == null || !nbt.contains(Reference.MOD_ID)) { 
    		return;
    	}
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
	    	INBT shareTag = nbt.get(Reference.MOD_ID);
	    	ToolModStorage.deserializeNBT(cap, shareTag);
		}
    }
}
