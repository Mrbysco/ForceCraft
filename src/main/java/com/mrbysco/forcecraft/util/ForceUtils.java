package com.mrbysco.forcecraft.util;

import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.blocks.ForceLogBlock;
import com.mrbysco.forcecraft.networking.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Map;
import java.util.Stack;

public class ForceUtils {

    public static void removeEnchant(Enchantment enchantment, ItemStack stack){
        Map<Enchantment, Integer> enchantMap = EnchantmentHelper.getEnchantments(stack);
        if(enchantMap.containsKey(enchantment)){
            enchantMap.remove(enchantment);
        }

        EnchantmentHelper.setEnchantments(enchantMap, stack);
    }

    //Credit to Slimeknights for this code until I can logic through it on my own
    public static boolean isTree(World world, BlockPos origin){
        BlockPos pos = null;
        Stack<BlockPos> candidates = new Stack<>();
        candidates.add(origin);

        while(!candidates.isEmpty()) {
            BlockPos candidate = candidates.pop();
            if((pos == null || candidate.getY() > pos.getY()) && isLog(world, candidate)) {
                pos = candidate.above();
                // go up
                while(isLog(world, pos)) {
                    pos = pos.above();
                }
                // check if we still have a way diagonally up
                candidates.add(pos.north());
                candidates.add(pos.east());
                candidates.add(pos.south());
                candidates.add(pos.west());
            }
        }

        // not even one match, so there were no logs.
        if(pos == null) {
            return false;
        }

        // check if there were enough leaves around the last position
        // pos now contains the block above the topmost log
        // we want at least 5 leaves in the surrounding 26 blocks
        int d = 3;
        int o = -1; // -(d-1)/2
        int leaves = 0;
        for(int x = 0; x < d; x++) {
            for(int y = 0; y < d; y++) {
                for(int z = 0; z < d; z++) {
                    BlockPos leaf = pos.offset(o + x, o + y, o + z);
                    BlockState state = world.getBlockState(leaf);
                    if(state.getBlock().is(BlockTags.LEAVES)) {
                        if(++leaves >= 5) {
                            return true;
                        }
                    }
                }
            }
        }

        // not enough leaves. sorreh
        return false;
    }

    public static boolean isLog(World world, BlockPos pos){
        if(world.getBlockState(pos).getBlock().is(BlockTags.LOGS) || world.getBlockState(pos).getBlock() instanceof ForceLogBlock)
            return true;
        else
            return false;
    }

    public static void breakExtraBlock(ItemStack stack, World world, PlayerEntity player, BlockPos pos, BlockPos refPos) {
        if(!canBreakExtraBlock(stack, world, player, pos, refPos)) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);
        Block block = state.getBlock();

        // callback to the tool the player uses. Called on both sides. This damages the tool n stuff.
        stack.mineBlock(world, state, pos, player);

        // server sided handling
        if(!world.isClientSide) {
            // send the blockbreak event
            int xp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).gameMode.getGameModeForPlayer(), (ServerPlayerEntity) player, pos);
            if(xp == -1) {
                return;
            }

            // serverside we reproduce ItemInWorldManager.tryHarvestBlock

            TileEntity tileEntity = world.getBlockEntity(pos);
            // ItemInWorldManager.removeBlock
            if(block.removedByPlayer(state, world, pos, player, true, fluidState)) { // boolean is if block can be harvested, checked above
                block.playerWillDestroy(world, pos, state, player);
                block.playerDestroy(world, player, pos, state, tileEntity, stack);
                block.popExperience((ServerWorld) world, pos, xp);
            }

            // always send block update to client
            PacketHandler.sendPacket(player, new SChangeBlockPacket(world, pos));
        }
        // client sided handling
        else {
            // clientside we do a "this clock has been clicked on long enough to be broken" call. This should not send any new packets
            // the code above, executed on the server, sends a block-updates that give us the correct state of the block we destroy.

            // following code can be found in PlayerControllerMP.onPlayerDestroyBlock
            world.globalLevelEvent(2001, pos, Block.getId(state));
            if(block.removedByPlayer(state, world, pos, player, true, fluidState)) {
                block.playerWillDestroy(world, pos, state, player);
            }
            // callback to the tool
            stack.mineBlock(world, state, pos, player);

            if(stack.getCount() == 0 && stack == player.getMainHandItem()) {
                ForgeEventFactory.onPlayerDestroyItem(player, stack, Hand.MAIN_HAND);
                player.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            }

            // send an update to the server, so we get an update back
            //if(PHConstruct.extraBlockUpdates)
            ClientPlayNetHandler netHandlerPlayClient = Minecraft.getInstance().getConnection();
            assert netHandlerPlayClient != null;
            netHandlerPlayClient.send(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN)); //.getInstance().objectMouseOver.sideHit
        }
    }

    private static boolean canBreakExtraBlock(ItemStack stack, World world, PlayerEntity player, BlockPos pos, BlockPos refPos) {
        // prevent calling that stuff for air blocks, could lead to unexpected behaviour since it fires events
        if(world.isEmptyBlock(pos)) {
            return false;
        }

        // check if the block can be broken, since extra block breaks shouldn't instantly break stuff like obsidian
        // or precious ores you can't harvest while mining stone
        BlockState state = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);
        Block block = state.getBlock();

        // only effective materials
        //TODO: Check for Effective

        BlockState refState = world.getBlockState(refPos);
        float refStrength = refState.getDestroyProgress( player, world, refPos);
        float strength = state.getDestroyProgress(player, world, pos);

        // only harvestable blocks that aren't impossibly slow to harvest
        if(!ForgeHooks.canHarvestBlock(state, player, world, pos) || refStrength / strength > 10f) {
            return false;
        }

        // From this point on it's clear that the player CAN break the block

        if(player.abilities.instabuild) {
            block.playerWillDestroy(world, pos, state, player);
            if(block.removedByPlayer(state, world, pos, player, false, fluidState)) {
                block.playerWillDestroy(world, pos, state, player);
            }

            // send update to client
            if(!world.isClientSide) {
                PacketHandler.sendPacket(player, new SChangeBlockPacket(world, pos));
            }
            return false;
        }
        return true;
    }

    public static boolean isFakePlayer(Entity player){
        return (player instanceof FakePlayer);
    }

    public static String resource(String res) {
        return String.format("%s:%s", Reference.MOD_ID, res);
    }

    public static ResourceLocation getResource(String res) {
        return new ResourceLocation(Reference.MOD_ID, res);
    }

    public static void teleportRandomly(LivingEntity livingEntity) {
        if (!livingEntity.level.isClientSide() && livingEntity.isAlive() && !livingEntity.isInWaterOrBubble()) {
            double d0 = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
            double d1 = livingEntity.getY() + (double) (livingEntity.getRandom().nextInt(32) - 16);
            double d2 = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
            teleportTo(livingEntity, d0, d1, d2);
        }
    }

    public static void teleportTo(LivingEntity living, double x, double y, double z) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

        while(blockpos$mutable.getY() > 0 && !living.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = living.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(living, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
            boolean flag2 = living.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !living.isSilent()) {
                living.level.playSound((PlayerEntity)null, living.xo, living.yo, living.zo, SoundEvents.ENDERMAN_TELEPORT, living.getSoundSource(), 1.0F, 1.0F);
                living.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
    }

}
