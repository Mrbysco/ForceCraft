package com.mrbysco.forcecraft.items.tools;

import com.google.common.collect.Lists;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.material.ModToolTiers;
import com.mrbysco.forcecraft.util.ForceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import static com.mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;
import static com.mrbysco.forcecraft.util.ForceUtils.isLog;

public class ForceAxeItem extends AxeItem implements IForceChargingTool {

    public ForceAxeItem(Item.Properties properties) {
        super(ModToolTiers.FORCE, 0F, -3.1F, properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    	if(CAPABILITY_TOOLMOD == null) {
            return null;
        }
        return new ToolModProvider();
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        IToolModifier toolModifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
        if(toolModifierCap != null) {
            if(toolModifierCap.hasLumberjack()) {
                if (player != null) {
                    if (ForceUtils.isTree(player.getCommandSenderWorld(), pos)) {
                        return fellTree(stack, pos, player);
                    }
                }
            }
        }

        return false;
    }

    public static boolean fellTree(ItemStack stack, BlockPos pos, Player player){
        if(player.getCommandSenderWorld().isClientSide) {
            return true;
        }
        MinecraftForge.EVENT_BUS.register(new TreeChopTask(stack, pos, player, 10));
        return true;
    }

    public static class TreeChopTask{
        public final Level world;
        public final Player player;
        public final ItemStack tool;
        public final int blocksPerTick;

        public Queue<BlockPos> blocks = Lists.newLinkedList();
        public Set<BlockPos> visited = new HashSet<>();

        public TreeChopTask(ItemStack tool, BlockPos start, Player player, int blocksPerTick) {
            this.world = player.getCommandSenderWorld();
            this.player = player;
            this.tool = tool;
            this.blocksPerTick = blocksPerTick;

            this.blocks.add(start);
        }

        @SubscribeEvent
        public void chop(TickEvent.WorldTickEvent event) {
            if(event.side.isClient()) {
                finish();
                return;
            }
            // only if same dimension
            if(event.world.dimension().location().equals(world.dimension().location())) {
                return;
            }

            // setup
            int left = blocksPerTick;

            // continue running
            BlockPos pos;
            while(left > 0) {
                // completely done or can't do our job anymore?!
                if(blocks.isEmpty()) {
                    finish();
                    return;
                }

                pos = blocks.remove();
                if(!visited.add(pos)) {
                    continue;
                }

                // can we harvest the block and is effective?
                if(!isLog(world, pos)) {
                    continue;
                }

                // save its neighbours
                for(Direction facing : new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST }) {
                    BlockPos pos2 = pos.relative(facing);
                    if(!visited.contains(pos2)) {
                        blocks.add(pos2);
                    }
                }

                // also add the layer above.. stupid acacia trees
                for(int x = 0; x < 3; x++) {
                    for(int z = 0; z < 3; z++) {
                        BlockPos pos2 = pos.offset(-1 + x, 1, -1 + z);
                        if(!visited.contains(pos2)) {
                            blocks.add(pos2);
                        }
                    }
                }

                // break it, wooo!
                ForceUtils.breakExtraBlock(tool, world, player, pos, pos);
                left--;
            }
        }

        private void finish() {
            // goodbye cruel world
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
    
    // ShareTag for server->client capability data sync
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
    	CompoundTag nbt = super.getShareTag(stack);
    	
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
			CompoundTag shareTag = ToolModStorage.serializeNBT(cap);
			nbt.put(Reference.MOD_ID, shareTag);
		}
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
    	if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
    		return;
    	}
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
	    	Tag shareTag = nbt.get(Reference.MOD_ID);
	    	ToolModStorage.deserializeNBT(cap, shareTag);
		}
        super.readShareTag(stack, nbt);
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> lores, TooltipFlag flagIn) {
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(lores);
    	ToolModStorage.attachInformation(stack, lores);
        super.appendHoverText(stack, worldIn, lores, flagIn);
    }
    
	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}

}
