package mrbysco.forcecraft.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import mrbysco.forcecraft.networking.PacketHandler;
import mrbysco.forcecraft.registry.ForceSounds;
import mrbysco.forcecraft.registry.material.ModToolMaterial;
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
        super(8.0F, 8.0F, ModToolMaterial.FORCE, Collections.emptySet(), properties.maxDamage(1000));
        this.attackDamage = 3.0F;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if(entityLiving instanceof PlayerEntity && state.getMaterial() == Material.LEAVES) {
            PlayerEntity player = (PlayerEntity)entityLiving;
            BlockRayTraceResult traceResult = rayTrace(worldIn, player, FluidMode.NONE);
            switch (traceResult.getFace().getAxis()) {
                case X:
                    breakBlockAt(player, worldIn, stack, pos.up());
                    breakBlockAt(player, worldIn, stack, pos.down());
                    breakBlockAt(player, worldIn, stack, pos.north());
                    breakBlockAt(player, worldIn, stack, pos.north().up());
                    breakBlockAt(player, worldIn, stack, pos.north().down());
                    breakBlockAt(player, worldIn, stack, pos.south());
                    breakBlockAt(player, worldIn, stack, pos.south().up());
                    breakBlockAt(player, worldIn, stack, pos.south().down());
                    break;
                case Z:
                    breakBlockAt(player, worldIn, stack, pos.up());
                    breakBlockAt(player, worldIn, stack, pos.down());
                    breakBlockAt(player, worldIn, stack, pos.west());
                    breakBlockAt(player, worldIn, stack, pos.west().up());
                    breakBlockAt(player, worldIn, stack, pos.west().down());
                    breakBlockAt(player, worldIn, stack, pos.east());
                    breakBlockAt(player, worldIn, stack, pos.east().up());
                    breakBlockAt(player, worldIn, stack, pos.east().down());
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
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    public void breakBlockAt(PlayerEntity player, World worldIn, ItemStack stack, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(worldIn.getBlockState(pos).getMaterial() == Material.LEAVES) {
            BlockState state = worldIn.getBlockState(pos);
            if(!ForgeHooks.canHarvestBlock(state, player, worldIn, pos)) return;

            if(!worldIn.isRemote) {
                int xp = ForgeHooks.onBlockBreakEvent(worldIn, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, pos);
                if(xp == -1) {
                    return;
                }

                FluidState fluidState = worldIn.getFluidState(pos);
                Block block = state.getBlock();

                if(block.removedByPlayer(state, worldIn, pos, player, true, fluidState)) {
                    block.onBlockHarvested(worldIn, pos, state, player);
                    block.harvestBlock(worldIn, player, pos, state, tileEntity, stack);
                    block.dropXpOnBlockBreak((ServerWorld) worldIn, pos, xp);
                }

                ((ServerWorld)worldIn).spawnParticle(ParticleTypes.SWEEP_ATTACK, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1, 0, 0, 0, 0.0D);
                PacketHandler.sendPacket(player, new SChangeBlockPacket(worldIn, pos));
            }
        }
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if(tool == ToolType.PICKAXE) {
            return itemTier.getHarvestLevel();
        } else if(tool == ToolType.AXE) {
            return itemTier.getHarvestLevel();
        } else if(tool == ToolType.SHOVEL) {
            return itemTier.getHarvestLevel();
        } else if(tool == ToolType.get("sword")) {
            return itemTier.getHarvestLevel();
        }
        return super.getHarvestLevel(stack, tool, player, blockState);
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
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        if (context.getFace() != Direction.DOWN && world.isAirBlock(blockpos.up())) {
            BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, context.getPlayer(), context.getItem(), net.minecraftforge.common.ToolType.HOE);
            if (blockstate != null) {
                PlayerEntity playerentity = context.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote) {
                    world.setBlockState(blockpos, blockstate, 11);
                    if (playerentity != null) {
                        context.getItem().damageItem(1, playerentity, (player) -> player.sendBreakAnimation(context.getHand()));
                    }
                }

                return ActionResultType.func_233537_a_(world.isRemote);
            }
        }

        BlockState blockstate = world.getBlockState(blockpos);
        BlockState block = blockstate.getToolModifiedState(world, blockpos, context.getPlayer(), context.getItem(), net.minecraftforge.common.ToolType.AXE);
        if (block != null) {
            PlayerEntity playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isRemote) {
                world.setBlockState(blockpos, block, 11);
                if (playerentity != null) {
                    context.getItem().damageItem(1, playerentity, (p_220040_1_) -> {
                        p_220040_1_.sendBreakAnimation(context.getHand());
                    });
                }
            }

            return ActionResultType.func_233537_a_(world.isRemote);
        }

        return ActionResultType.PASS;
    }

    private static final Set<Material> EFFECTIVE_MATERIALS = ImmutableSet.of(
            // Pickaxe
            Material.ROCK, Material.IRON, Material.ICE, Material.GLASS, Material.PISTON, Material.ANVIL, Material.MISCELLANEOUS,

            // Axe
            Material.WOOD, Material.NETHER_WOOD, Material.PLANTS, Material.TALL_PLANTS, Material.BAMBOO, Material.GOURD,

            // Shovel
            Material.SEA_GRASS, Material.EARTH, Material.SAND, Material.SNOW, Material.SNOW_BLOCK, Material.CLAY,

            // Shear / Mitt
            Material.LEAVES
    );

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return EFFECTIVE_MATERIALS.contains(material) ? 14.0F : super.getDestroySpeed(stack, state);
    }

    public float getAttackDamage() {
        return this.itemTier.getAttackDamage();
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, (attackerEntity) -> attackerEntity.sendBreakAnimation(Hand.MAIN_HAND));
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            if(multimap != null) {
                Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon damage modifier", (double)this.attackDamage, Operation.ADDITION));
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon speed modifier", -2.4000000953674316D,  Operation.ADDITION));
                multimap = builder.build();
            }
        }
        return multimap;
    }

    @Override
    public boolean canHarvestBlock(BlockState blockIn) {
        return EFFECTIVE_MATERIALS.contains(blockIn.getMaterial()) || blockIn.isIn(Blocks.COBWEB);
    }
}
