package mrbysco.forcecraft.items.tools;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import mrbysco.forcecraft.entities.ColdChickenEntity;
import mrbysco.forcecraft.entities.ColdCowEntity;
import mrbysco.forcecraft.entities.ColdPigEntity;
import mrbysco.forcecraft.entities.IColdMob;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;

public class ForceShearsItem extends ShearsItem {

    public ForceShearsItem(Item.Properties properties){
        super(properties);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand) {
        if (entity.world.isRemote) {
            return ActionResultType.PASS;
        }

        IToolModifier toolModifier = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
        if(toolModifier != null) {
            if (toolModifier.hasRainbow()) {
                if (entity instanceof net.minecraftforge.common.IForgeShearable) {
                    net.minecraftforge.common.IForgeShearable target = (net.minecraftforge.common.IForgeShearable) entity;
                    BlockPos pos = new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                    if (target.isShearable(stack, entity.world, pos)) {
                        java.util.List<ItemStack> drops = target.onSheared(playerIn, stack, entity.world, pos,
                                net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantments.FORTUNE, stack));
                        java.util.Random rand = new java.util.Random();
                        drops.forEach(d -> {
                            net.minecraft.entity.item.ItemEntity ent = entity.entityDropItem(d, 1.0F);
                            ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                        });
                        stack.damageItem(1, entity, e -> e.sendBreakAnimation(hand));
                    }
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
        }
        boolean hasHeat = toolModifier != null && toolModifier.hasHeat();

        if(!(entity instanceof IColdMob)) {
            if(entity instanceof CowEntity && !(entity instanceof MooshroomEntity)) {
                CowEntity originalCow = (CowEntity)entity;
                World worldIn = originalCow.world;

                java.util.Random rand = new java.util.Random();
                int i = 1 + rand.nextInt(3);

                java.util.List<ItemStack> drops = new ArrayList<>();
                for (int j = 0; j < i; ++j)
                    drops.add(new ItemStack(Items.LEATHER, 1));

                drops.forEach(d -> {
                    net.minecraft.entity.item.ItemEntity ent = entity.entityDropItem(d, 1.0F);
                    ent.setMotion(ent.getMotion().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });

                entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);

                MobEntity replacementMob = new ColdCowEntity(worldIn, originalCow.getType().getRegistryName());
                replacementMob.copyLocationAndAnglesFrom(originalCow);
                UUID mobUUID = replacementMob.getUniqueID();
                replacementMob.copyDataFromOld(originalCow);
                replacementMob.setUniqueId(mobUUID);

                originalCow.remove(false);
                worldIn.addEntity(replacementMob);
                if(hasHeat) {
                    replacementMob.setFire(10);
                }

                return ActionResultType.SUCCESS;
            }
            if(entity instanceof ChickenEntity) {
                ChickenEntity originalChicken = (ChickenEntity)entity;
                World worldIn = originalChicken.world;

                java.util.Random rand = new java.util.Random();
                int i = 1 + rand.nextInt(3);

                java.util.List<ItemStack> drops = new ArrayList<>();
                for (int j = 0; j < i; ++j)
                    drops.add(new ItemStack(Items.FEATHER, 1));

                drops.forEach(d -> {
                    net.minecraft.entity.item.ItemEntity ent = entity.entityDropItem(d, 1.0F);
                    ent.setMotion(ent.getMotion().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });

                entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);

                MobEntity replacementMob = new ColdChickenEntity(worldIn, originalChicken.getType().getRegistryName());
                replacementMob.copyLocationAndAnglesFrom(originalChicken);
                UUID mobUUID = replacementMob.getUniqueID();
                replacementMob.copyDataFromOld(originalChicken);
                replacementMob.setUniqueId(mobUUID);

                originalChicken.remove(false);
                worldIn.addEntity(replacementMob);
                if(hasHeat) {
                    replacementMob.setFire(10);
                }

                return ActionResultType.SUCCESS;
            }
            if(entity instanceof PigEntity) {
                PigEntity originalPig = (PigEntity)entity;
                World worldIn = originalPig.world;

                java.util.Random rand = new java.util.Random();
                int i = 1 + rand.nextInt(2);

                java.util.List<ItemStack> drops = new ArrayList<>();
                for (int j = 0; j < i; ++j)
                    drops.add(new ItemStack(hasHeat ? ForceRegistry.COOKED_BACON.get() : ForceRegistry.RAW_BACON.get(), 1));

                drops.forEach(d -> {
                    net.minecraft.entity.item.ItemEntity ent = entity.entityDropItem(d, 1.0F);
                    ent.setMotion(ent.getMotion().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });

                entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);

                MobEntity replacementMob = new ColdPigEntity(worldIn, originalPig.getType().getRegistryName());
                replacementMob.copyLocationAndAnglesFrom(originalPig);
                UUID mobUUID = replacementMob.getUniqueID();
                replacementMob.copyDataFromOld(originalPig);
                replacementMob.setUniqueId(mobUUID);

                originalPig.remove(false);
                worldIn.addEntity(replacementMob);
                if(hasHeat) {
                    replacementMob.setFire(10);
                }

                return ActionResultType.SUCCESS;
            }
        }

        return super.itemInteractionForEntity(stack, playerIn, entity, hand);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModProvider();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        stack.getCapability(CAPABILITY_TOOLMOD).ifPresent((cap) -> {
            if(cap.hasRainbow()) {
                tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".toolmod.rainbow"));
            }
        });
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
