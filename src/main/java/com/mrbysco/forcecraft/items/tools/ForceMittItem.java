package com.mrbysco.forcecraft.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.mrbysco.forcecraft.networking.PacketHandler;
import com.mrbysco.forcecraft.registry.ForceSounds;
import com.mrbysco.forcecraft.registry.material.ModToolMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class ForceMittItem extends ToolItem {

    private final float attackDamage;
    private final IItemTier itemTier = ModToolMaterial.FORCE;

    public ForceMittItem(Item.Properties properties) {
        super(8.0F, 8.0F, ModToolMaterial.FORCE, Collections.emptySet(), properties.durability(1000));
        this.attackDamage = 3.0F;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if(entityLiving instanceof PlayerEntity && state.getMaterial() == Material.LEAVES) {
            PlayerEntity player = (PlayerEntity)entityLiving;
            BlockRayTraceResult traceResult = getPlayerPOVHitResult(worldIn, player, FluidMode.NONE);
            switch (traceResult.getDirection().getAxis()) {
                case X:
                    breakBlockAt(player, worldIn, stack, pos.above());
                    breakBlockAt(player, worldIn, stack, pos.below());
                    breakBlockAt(player, worldIn, stack, pos.north());
                    breakBlockAt(player, worldIn, stack, pos.north().above());
                    breakBlockAt(player, worldIn, stack, pos.north().below());
                    breakBlockAt(player, worldIn, stack, pos.south());
                    breakBlockAt(player, worldIn, stack, pos.south().above());
                    breakBlockAt(player, worldIn, stack, pos.south().below());
                    break;
                case Z:
                    breakBlockAt(player, worldIn, stack, pos.above());
                    breakBlockAt(player, worldIn, stack, pos.below());
                    breakBlockAt(player, worldIn, stack, pos.west());
                    breakBlockAt(player, worldIn, stack, pos.west().above());
                    breakBlockAt(player, worldIn, stack, pos.west().below());
                    breakBlockAt(player, worldIn, stack, pos.east());
                    breakBlockAt(player, worldIn, stack, pos.east().above());
                    breakBlockAt(player, worldIn, stack, pos.east().below());
                    break;
                case Y:
                    breakBlockAt(player, worldIn, stack, pos.north());
                    breakBlockAt(player, worldIn, stack, pos.east());
                    breakBlockAt(player, worldIn, stack, pos.west());
                    breakBlockAt(player, worldIn, stack, pos.west().north());
                    breakBlockAt(player, worldIn, stack, pos.west().east());
                    breakBlockAt(player, worldIn, stack, pos.east());
                    breakBlockAt(player, worldIn, stack, pos.east().north());
                    breakBlockAt(player, worldIn, stack, pos.east().east());
                    break;
            }

            worldIn.playSound((PlayerEntity) null, pos, ForceSounds.WHOOSH.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        return super.mineBlock(stack, worldIn, state, pos, entityLiving);
    }

    public void breakBlockAt(PlayerEntity player, World worldIn, ItemStack stack, BlockPos pos) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if(worldIn.getBlockState(pos).getMaterial() == Material.LEAVES) {
            BlockState state = worldIn.getBlockState(pos);
            if(!ForgeHooks.canHarvestBlock(state, player, worldIn, pos)) return;

            if(!worldIn.isClientSide) {
                int xp = ForgeHooks.onBlockBreakEvent(worldIn, ((ServerPlayerEntity) player).gameMode.getGameModeForPlayer(), (ServerPlayerEntity) player, pos);
                if(xp == -1) {
                    return;
                }

                FluidState fluidState = worldIn.getFluidState(pos);
                Block block = state.getBlock();

                if(block.removedByPlayer(state, worldIn, pos, player, true, fluidState)) {
                    block.playerWillDestroy(worldIn, pos, state, player);
                    block.playerDestroy(worldIn, player, pos, state, tileEntity, stack);
                    block.popExperience((ServerWorld) worldIn, pos, xp);
                }

                ((ServerWorld)worldIn).sendParticles(ParticleTypes.SWEEP_ATTACK, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1, 0, 0, 0, 0.0D);
                PacketHandler.sendPacket(player, new SChangeBlockPacket(worldIn, pos));
            }
        }
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if(tool == ToolType.PICKAXE) {
            return itemTier.getLevel();
        } else if(tool == ToolType.AXE) {
            return itemTier.getLevel();
        } else if(tool == ToolType.SHOVEL) {
            return itemTier.getLevel();
        } else if(tool == ToolType.get("sword")) {
            return itemTier.getLevel();
        }
        return super.getHarvestLevel(stack, tool, player, blockState);
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @SuppressWarnings("deprecation")
	@Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
            BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), net.minecraftforge.common.ToolType.HOE);
            if (blockstate != null) {
                PlayerEntity playerentity = context.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide) {
                    world.setBlock(blockpos, blockstate, 11);
                    if (playerentity != null) {
                        context.getItemInHand().hurtAndBreak(1, playerentity, (player) -> player.broadcastBreakEvent(context.getHand()));
                    }
                }

                return ActionResultType.sidedSuccess(world.isClientSide);
            }
        }

        BlockState blockstate = world.getBlockState(blockpos);
        BlockState block = blockstate.getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), net.minecraftforge.common.ToolType.AXE);
        if (block != null) {
            PlayerEntity playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(blockpos, block, 11);
                if (playerentity != null) {
                    context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
                        p_220040_1_.broadcastBreakEvent(context.getHand());
                    });
                }
            }

            return ActionResultType.sidedSuccess(world.isClientSide);
        }

        return ActionResultType.PASS;
    }

    private static final Set<Material> EFFECTIVE_MATERIALS = ImmutableSet.of(
            // Pickaxe
            Material.STONE, Material.METAL, Material.ICE, Material.GLASS, Material.PISTON, Material.HEAVY_METAL, Material.DECORATION,

            // Axe
            Material.WOOD, Material.NETHER_WOOD, Material.PLANT, Material.REPLACEABLE_PLANT, Material.BAMBOO, Material.VEGETABLE,

            // Shovel
            Material.REPLACEABLE_WATER_PLANT, Material.DIRT, Material.SAND, Material.TOP_SNOW, Material.SNOW, Material.CLAY,

            // Shear / Mitt
            Material.LEAVES
    );

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return EFFECTIVE_MATERIALS.contains(material) ? 14.0F : super.getDestroySpeed(stack, state);
    }

    public float getAttackDamage() {
        return this.itemTier.getAttackDamageBonus();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (attackerEntity) -> attackerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<Attribute, AttributeModifier> multimap = super.getDefaultAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            if(multimap != null) {
                Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon damage modifier", (double)this.attackDamage, Operation.ADDITION));
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon speed modifier", -2.4000000953674316D,  Operation.ADDITION));
                multimap = builder.build();
            }
        }
        return multimap;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockIn) {
        return EFFECTIVE_MATERIALS.contains(blockIn.getMaterial()) || blockIn.is(Blocks.COBWEB);
    }
}
