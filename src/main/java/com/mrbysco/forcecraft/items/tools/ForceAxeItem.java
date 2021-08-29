package com.mrbysco.forcecraft.items.tools;

import com.google.common.collect.Lists;
import com.mrbysco.forcecraft.Reference;
import com.mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModProvider;
import com.mrbysco.forcecraft.capablilities.toolmodifier.ToolModStorage;
import com.mrbysco.forcecraft.items.infuser.ForceToolData;
import com.mrbysco.forcecraft.items.infuser.IForceChargingTool;
import com.mrbysco.forcecraft.registry.material.ModToolMaterial;
import com.mrbysco.forcecraft.util.ForceUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
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
        super(ModToolMaterial.FORCE, 0F, -3.1F, properties);
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
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
        IToolModifier toolModifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
        if(toolModifierCap != null) {
            if(toolModifierCap.hasLumberjack()) {
                if (player != null) {
                    if (ForceUtils.isTree(player.getEntityWorld(), pos)) {
                        return fellTree(stack, pos, player);
                    }
                }
            }
        }

        return false;
    }

    public static boolean fellTree(ItemStack stack, BlockPos pos, PlayerEntity player){
        if(player.getEntityWorld().isRemote) {
            return true;
        }
        MinecraftForge.EVENT_BUS.register(new TreeChopTask(stack, pos, player, 10));
        return true;
    }

    public static class TreeChopTask{
        public final World world;
        public final PlayerEntity player;
        public final ItemStack tool;
        public final int blocksPerTick;

        public Queue<BlockPos> blocks = Lists.newLinkedList();
        public Set<BlockPos> visited = new HashSet<>();

        public TreeChopTask(ItemStack tool, BlockPos start, PlayerEntity player, int blocksPerTick) {
            this.world = player.getEntityWorld();
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
            if(event.world.getDimensionKey().getLocation().equals(world.getDimensionKey().getLocation())) {
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
                    BlockPos pos2 = pos.offset(facing);
                    if(!visited.contains(pos2)) {
                        blocks.add(pos2);
                    }
                }

                // also add the layer above.. stupid acacia trees
                for(int x = 0; x < 3; x++) {
                    for(int z = 0; z < 3; z++) {
                        BlockPos pos2 = pos.add(-1 + x, 1, -1 + z);
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
    	if(nbt == null || !nbt.contains(Reference.MOD_ID)) {
    		return;
    	}
		IToolModifier cap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
		if(cap != null) {
	    	INBT shareTag = nbt.get(Reference.MOD_ID);
	    	ToolModStorage.deserializeNBT(cap, shareTag);
		}
        super.readShareTag(stack, nbt);
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> lores, ITooltipFlag flagIn) {
    	ForceToolData fd = new ForceToolData(stack);
    	fd.attachInformation(lores);
    	ToolModStorage.attachInformation(stack, lores);
        super.addInformation(stack, worldIn, lores, flagIn);
    }
    
	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return this.damageItem(stack, amount);
	}

}
