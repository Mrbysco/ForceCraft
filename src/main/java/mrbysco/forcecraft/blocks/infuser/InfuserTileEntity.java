package mrbysco.forcecraft.blocks.infuser;

import mrbysco.forcecraft.ForceCraft;
import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.forcerod.IForceRodModifier;
import mrbysco.forcecraft.capablilities.pack.PackItemStackHandler;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.items.CustomArmorItem;
import mrbysco.forcecraft.items.ForcePackItem;
import mrbysco.forcecraft.items.infuser.ForceToolData;
import mrbysco.forcecraft.items.infuser.UpgradeBookData;
import mrbysco.forcecraft.items.infuser.UpgradeTomeItem;
import mrbysco.forcecraft.items.tools.ForceAxeItem;
import mrbysco.forcecraft.items.tools.ForceBowItem;
import mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import mrbysco.forcecraft.items.tools.ForceRodItem;
import mrbysco.forcecraft.items.tools.ForceShearsItem;
import mrbysco.forcecraft.items.tools.ForceShovelItem;
import mrbysco.forcecraft.items.tools.ForceSwordItem;
import mrbysco.forcecraft.recipe.ForceRecipes;
import mrbysco.forcecraft.recipe.InfuseRecipe;
import mrbysco.forcecraft.registry.ForceFluids;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.registry.ForceTags;
import mrbysco.forcecraft.tiles.energy.ForceEnergyStorage;
import mrbysco.forcecraft.util.EnchantUtils;
import mrbysco.forcecraft.util.ItemHandlerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class InfuserTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IInventory {

    private static final int FLUID_CHARGE = 1000;

	public boolean canWork = false;

    public int processTime = 0;
    public int maxProcessTime = 17;

    public int fluidContained;
	//modifiers cap out
	private static final int MAX_MODIFIER = 10;
	// slots [0,7] are the surround
	public static final int SLOT_TOOL = 8;
    public static final int SLOT_GEM = 9;
	public static final int SLOT_BOOK = 10;
	//currently these costs are fixed PER infusing a thing once
	public static final int ENERGY_COST_PER = 20;
    public static final int FLUID_COST_PER = 1000;
    //ratio o f gem slot to fluid tank
	private static final int FLUID_PER_GEM = 500;

	protected FluidTank tank = new FluidTank(50000) {
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (!isFluidEqual(resource)) {
                return FluidStack.EMPTY;
            }
            if (action.simulate()) {
                int amount = tank.getFluidAmount() - resource.getAmount() < 0 ? tank.getFluidAmount() : resource.getAmount();
                return new FluidStack(tank.getFluid(), amount);
            }
            return super.drain(resource.getAmount(), action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return super.drain(maxDrain, action);
        }

		@Override
		public boolean isFluidValid(FluidStack stack) {
			for (Fluid fluid : FluidTags.getCollection().get(new ResourceLocation("forge","force")).getAllElements()){
				if (fluid == stack.getFluid()) {
					return true;
				}
			}
			return false;
		}
	};
    private LazyOptional<IFluidHandler> tankHolder = LazyOptional.of(() -> tank);

    public final ItemStackHandler handler = new ItemStackHandler(11) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }


        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
        	if(slot < SLOT_TOOL) {
        		//is valid modifier TODO : fixing this check will fix placing and shift-clicking
        		// non ingredients into the circle
        	}
        	else if(slot == SLOT_TOOL) {
        		//dont hardcode validation here, check recipe "center" tag or item
                return true;// stack.getItem().isIn(ForceTags.VALID_INFUSER_TOOLS);
        	}
        	else if(slot == SLOT_BOOK) {
        		return stack.getItem() == ForceRegistry.UPGRADE_TOME.get();
        	}
        	else if(slot == SLOT_GEM) {
        		return stack.getItem() == ForceRegistry.FORCE_GEM.get();
        	}
        	// else tool or something for the around
        	return true;
        }
    };
    private LazyOptional<IItemHandler> handlerHolder = LazyOptional.of(() -> handler);

    public ForceEnergyStorage energyStorage = new ForceEnergyStorage(64000, 1000);
    private LazyOptional<ForceEnergyStorage> energyHolder = LazyOptional.of(() -> energyStorage);

    private NonNullList<ItemStack> infuserContents = NonNullList.create();


    public InfuserTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public InfuserTileEntity() {
        this(ForceRegistry.INFUSER_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {

    	this.processTime = nbt.getInt("processTime");
    	this.maxProcessTime = nbt.getInt("maxProcessTime");
        //Items
    	canWork = nbt.getBoolean("canWork");
        handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
        ItemStackHelper.loadAllItems(nbt, this.infuserContents);
        energyStorage.setEnergy(nbt.getInt("EnergyHandler"));
        tank.readFromNBT(nbt);

        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);

        compound.putInt("processTime", this.processTime);
        compound.putInt("maxProcessTime", this.maxProcessTime);
        //Items
        compound.putBoolean("canWork", canWork);
        compound.put("ItemStackHandler", handler.serializeNBT());
        compound.putInt("EnergyHandler", energyStorage.getEnergyStored());
        ItemStackHelper.saveAllItems(compound, this.infuserContents);
        tank.writeToNBT(compound);

        return compound;
    }

    @Override
    public void tick() {
    	if(world.isRemote) {
    		return;
    	}
        fluidContained = tank.getFluidAmount();

        if (handler.getStackInSlot(SLOT_GEM).getItem() == ForceRegistry.FORCE_GEM.get()) {
        	processForceGems();
        }

        if (canWork) {

        	processTime++;
        	if(processTime < this.maxProcessTime) {
        		return;
        	}
        	processTime = 0;
        	//working with No progress for now, just is valid and did i turn it on

        	//once we have a valid tool and have waited. go do the thing
        	if(hasTool() && hasValidBook()) {
        		if(areAllModifiersEmpty()) {
        			processForceCharging();
        		}
        		else {
        			processTool();
        		}
        	}
        	// auto turn off when done
        	//even if tool or book slot become empty, dont auto run next insert
            canWork = false;
        	processTime = 0;

            refreshClient();
        }
        else {
        	processTime = 0;
        }
    }

    public void startWork() {
    	canWork = true;
    	processTime = 0;
    	setMaxTimeFromRecipes();
    	if(maxProcessTime <= 0) {
    		//no valid recipes
        	canWork = false;
        	maxProcessTime = 0;
    	}
        refreshClient();
    }

	private void setMaxTimeFromRecipes() {
		maxProcessTime = 0;
		for (int i = 0; i < SLOT_TOOL; i++) {

			ItemStack modifier = getModifier(i);
			UpgradeBookData bd = new UpgradeBookData(this.getBookInSlot());
			int bookTier = bd.getTier().ordinal();
			// if the recipe level does not exceed what the book has
			// test the ingredient of this recipe, if it matches me

			List<InfuseRecipe> recipes = world.getRecipeManager().getRecipesForType(ForceRecipes.INFUSER_TYPE);
			for (InfuseRecipe recipeCurrent : recipes) {
				if (recipeCurrent.getTier().ordinal() > bookTier) {
					continue;
				}
				ItemStack tool = getFromToolSlot();
				if (recipeCurrent.getCenter().test(tool) == false) {
					// doesnt match the "center" tool test from recipe
					continue;
				}
				if (recipeCurrent.input.test(modifier)) {
					// tool, book, and modifier all valid
//					ForceCraft.LOGGER.info(recipeCurrent.getId() + " increase  "+ recipeCurrent.getTime());
					maxProcessTime += recipeCurrent.getTime();
				}
			}
		}
	}

	private void refreshClient() {
		markDirty();
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 2);
	}

    //Processes force Gems in the force infuser slot
    private void processForceGems() {
        FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), FLUID_PER_GEM);

        if (tank.getFluidAmount() < tank.getCapacity() - force.getAmount()) {
            fill(force, FluidAction.EXECUTE);
            handler.getStackInSlot(SLOT_GEM).shrink(1);

            refreshClient();
        }
    }

    private boolean areAllModifiersEmpty() {

    	int emptySlots = 0;
        for (int i = 0; i < SLOT_TOOL; i++) {
        	if(handler.getStackInSlot(i).isEmpty()) {
        		emptySlots++;
        	}
        }

    	return emptySlots == 8;
    }

    private void processForceCharging() {
        ItemStack tool = getFromToolSlot();

        if(!tool.getItem().isIn(ForceTags.VALID_INFUSER_CHARGE) || tool.getCount() != 1
        		|| tank.getFluidAmount() < FLUID_CHARGE) {
        	//halt if empty. halt if stack larger than 1, only upgrade a single item
        	return;
        }
        //its a valid force tool non stacked, so charging is ok

        ForceToolData force = new ForceToolData(tool);
        force.charge(FLUID_CHARGE);
        tank.drain(FLUID_CHARGE, FluidAction.EXECUTE);
        force.write(tool);
    }

    private void processTool() {
    	//recipes were pre-validated in setMaxTimeFromRecipes
    	//but do it again here in case any slots changed contents
        for (int i = 0; i < SLOT_TOOL; i++) {
        	//halt if no power
        	if(energyStorage.getEnergyStored() < ENERGY_COST_PER) {
        		break;
        	}
            if (hasValidModifer(i)) {
                ItemStack modifier = getModifier(i);
                ItemStack tool = getFromToolSlot();
                if(tool.getCount() != 1) {
                	//halt if empty. halt if stack larger than 1, only upgrade a single item
                	continue;
                }
                boolean success = applyModifier(tool, modifier);
                if (success) {
                	// for EACH modifier
                    handler.getStackInSlot(i).shrink(1);
                    tank.drain(FLUID_COST_PER, FluidAction.EXECUTE);
                    energyStorage.consumePower(ENERGY_COST_PER);
                    UpgradeTomeItem.onModifierApplied(this.getBookInSlot(), modifier, tool);
         			ForceCraft.LOGGER.info("process tool success {}, {}", this.getBookInSlot().getTag() , energyStorage.getEnergyStored());
                }
            }
        }
        // TODO: is this notfiy needed?
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.read(getBlockState(), packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public CompoundNBT getTileData() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handlerHolder.cast();
        }
        if (capability == FLUID_HANDLER_CAPABILITY) {
            return tankHolder.cast();
        }
        if (capability == CapabilityEnergy.ENERGY) {
            return energyHolder.cast();
        }

        return super.getCapability(capability, facing);
    }

    private boolean hasTool() {
        ItemStack tool = getFromToolSlot();
		return !tool.isEmpty();
    }

    private boolean hasValidBook() {
        ItemStack tool = getBookInSlot();
		if (!tool.isEmpty()) {
            return tool.getItem() == ForceRegistry.UPGRADE_TOME.get();
        }
        return false;
    }

    public ItemStack getFromToolSlot() {
		return handler.getStackInSlot(SLOT_TOOL);
	}

	public ItemStack getBookInSlot() {
		return handler.getStackInSlot(SLOT_BOOK);
	}

	public ItemStack getModifier(int slot) {
    	if(slot >= 0 && slot <= SLOT_TOOL - 1) {
			return handler.getStackInSlot(slot);
    	}
        return ItemStack.EMPTY;
    }

	public boolean hasValidModifer(int slot) {
        return getModifier(slot).isEmpty() == false;
    }
    /**
     * Loop on all modifiers and apply first one that matches the input test.
     * @param tool
     * @param modifier
     * @return
     */
    private boolean applyModifier(ItemStack tool, ItemStack modifier) {

		UpgradeBookData bd = new UpgradeBookData(this.getBookInSlot());
		int bookTier = bd.getTier().ordinal();
		// if the recipe level does not exceed what the book has
		//test the ingredient of this recipe, if it matches me

		List<InfuseRecipe> recipes = world.getRecipeManager().getRecipesForType(ForceRecipes.INFUSER_TYPE);
    	for (InfuseRecipe recipeCurrent : recipes) {

    		if(recipeCurrent.getTier().ordinal() > bookTier) {

    			ForceCraft.LOGGER.info("recipe tier {} > book Tier {}", recipeCurrent.getTier().ordinal(), bookTier);
    			continue;
    		}
    		if(recipeCurrent.getCenter().test(tool) == false) {
    			//doesnt match the "center" tool test from recipe
				continue;
    		}
//    		ForceCraft.LOGGER.info("CENTER and tier both match {} on tool {}", recipeCurrent.resultModifier, tool);

    		//force pack upgrade can be applied multiple times depending on many things
    		if (recipeCurrent.input.test(modifier)) {

    			if (recipeCurrent.resultModifier.apply(tool, modifier, bd)) {

                    if(recipeCurrent.resultModifier == InfuserModifierType.ITEM && recipeCurrent.hasOutput()) {
                    	//overwrite / convert item
                    	handler.setStackInSlot(SLOT_TOOL, recipeCurrent.getRecipeOutput().copy());
                    }
                    else {
                    	//sync item changes
                        handler.setStackInSlot(SLOT_TOOL, tool);
                    }

    				return true;
    			}
    			else {
        			ForceCraft.LOGGER.info(" apply returned false on {} to the tool {}", recipeCurrent.resultModifier, tool);
    			}
    		}
    	}

        return false;
    }

    // TODO: refactor these static below into another place, possibly InfuserModifier class

	static boolean applyCamo(ItemStack tool, ItemStack mod) {
		List<EffectInstance> effects = PotionUtils.getEffectsFromStack(mod);
		for (EffectInstance e : effects) {
		    if (e.getPotion() == Effects.NIGHT_VISION) {
		        return addSightModifier(tool);
		    }
		    if (e.getPotion() == Effects.INVISIBILITY) {
		        return addCamoModifier(tool);
		    }
		}
		return false;
	}

    static boolean addLightModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.hasLight()) {
                rodCap.setLight(true);
                return true;
            }
        }
        return false;
    }

    private static boolean addCamoModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.hasCamoModifier()) {
                rodCap.setCamoModifier(true);
                return true;
            }
        }
        return false;
    }

    private static boolean addSightModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.hasSightModifier()) {
                rodCap.setSightModifier(true);
                return true;
            }
        }
        return false;
    }

    static boolean addWingModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceSwordItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null && modifierCap.hasWing()) {
                modifierCap.setWing(true);
                return true;
            }
        }
        else if (stack.getItem() instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null && modifierCap.hasWing()) {
                modifierCap.setWing(true);
                return true;
            }
        }

        return false;
    }

    static boolean addBaneModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null && modifierCap.getBaneLevel() < 4) {
                modifierCap.incrementBane();
                return true;
            }
        }
        else if (st instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null) {
                if(modifierCap.getBaneLevel() == 0) {
                    modifierCap.incrementBane();
                    return true;
                } else if (modifierCap.getBaneLevel() < 4) {
                    modifierCap.incrementBane();
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addBleedingModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null && modifierCap.getBleedLevel() < 2) {
                modifierCap.incrementBleed();
                return true;
            }
        }
        else if (st instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null) {
                if(modifierCap.getBleedLevel() == 0) {
                    modifierCap.incrementBleed();
                    return true;
                } else if(modifierCap.getBleedLevel() < 2) {
                    modifierCap.incrementBleed();
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addEnderModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.isRodofEnder()) {
                rodCap.setEnderModifier(true);
                return true;
            }
        }
        return false;
    }

    static boolean addHealingModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null) {
                if(rodCap.isRodOfHealing(1)) {
                    rodCap.setRodOfHealing(true, 1);
                    return true;
                } else if (!rodCap.isRodOfHealing(2) && rodCap.isRodOfHealing(1)) {
                    rodCap.setRodOfHealing(true, 2);
                    return true;
                } else if (!rodCap.isRodOfHealing(3) && rodCap.isRodOfHealing(2) && rodCap.isRodOfHealing(1)) {
                    rodCap.setRodOfHealing(true, 3);
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addLumberjackModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null && !modifierCap.hasLumberjack()) {
                modifierCap.setLumberjack(true);
                return true;
            }
        }
        return false;
    }

    static boolean addRainbowModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceShearsItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                modifierCap.setRainbow(true);
                return true;
            }
        }
        return false;
    }

    static boolean addTreasureModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceSwordItem || stack.getItem() instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                modifierCap.setTreasure(true);
                return true;
            }
        }
        return false;
    }

    static boolean upgradeBag(ItemStack stack, UpgradeBookData bd) {
        if (stack.getItem() instanceof ForcePackItem) {
            IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
            if(handler instanceof PackItemStackHandler) {
                PackItemStackHandler packHandler = (PackItemStackHandler)handler;

                if(packHandler.canUpgrade(bd)) {
                    packHandler.applyUpgrade();

                    CompoundNBT tag = stack.getOrCreateTag();
                    tag.putInt(ForcePackItem.SLOTS_USED, ItemHandlerUtils.getUsedSlots(packHandler));

                    stack.setTag(tag);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    static boolean addTimeModifier(ItemStack stack,ItemStack mod) {
        if(stack.getItem() == ForceRegistry.FORCE_TORCH_ITEM.get()) {
        	//convert to time torch, overwrite
        	mod = new ItemStack(ForceRegistry.TIME_TORCH.get());
            return true;
        }
        return false;
    }

    static boolean addSturdyModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem || st instanceof ForceAxeItem || st instanceof ForceShovelItem || st instanceof ForcePickaxeItem || st instanceof ForceRodItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSturdyLevel() == 0) {
                    modifierCap.incrementSturdy();
                    stack.addEnchantment(Enchantments.UNBREAKING, 1);
                    return true;
                }
                else if(modifierCap.getSturdyLevel() < MAX_MODIFIER) {
                    modifierCap.incrementSturdy();
                    EnchantUtils.incrementLevel(stack, Enchantments.UNBREAKING);
                    return true;
                }
            }
        }
        else if (stack.getItem() instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSturdyLevel() == 0) {
                    modifierCap.incrementSturdy();
                    return true;
                }
                else if(modifierCap.getSturdyLevel() < MAX_MODIFIER) {
                    modifierCap.incrementSturdy();
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addLuckModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForcePickaxeItem || st instanceof ForceShovelItem || st instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getLuckLevel() == 0) {
                    modifierCap.incrementLuck();
                    stack.addEnchantment(Enchantments.FORTUNE, 1);
                    return true;
                }
                else if(modifierCap.getLuckLevel() < MAX_MODIFIER) {
                    modifierCap.incrementLuck();
                    EnchantUtils.incrementLevel(stack, Enchantments.FORTUNE);
                    return true;
                }
            }
        }
        else if(st instanceof ForceSwordItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getLuckLevel() == 0) {
                    modifierCap.incrementLuck();
                    stack.addEnchantment(Enchantments.LOOTING, 1);
                    return true;
                }
                else if(modifierCap.getLuckLevel() < MAX_MODIFIER) {
                    modifierCap.incrementLuck();
                    EnchantUtils.incrementLevel(stack, Enchantments.LOOTING);
                    return true;
                }
            }
        }
        else if (st instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getLuckLevel() == 0) {
                    modifierCap.incrementLuck();
                    return true;
                }
                else if(modifierCap.getLuckLevel() < MAX_MODIFIER) {
                    modifierCap.incrementLuck();
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addDamageModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSharpLevel() == 0) {
                    modifierCap.incrementSharp();
                    stack.addEnchantment(Enchantments.SHARPNESS, 2);
                    return true;
                }
                else if(modifierCap.getSharpLevel() < MAX_MODIFIER) {
                    modifierCap.incrementSharp();
                    EnchantUtils.incrementLevel(stack, Enchantments.SHARPNESS);
                    return true;
                }
            }
        }
        else if(st instanceof ForceBowItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSharpLevel() < MAX_MODIFIER) {
                    modifierCap.incrementSharp();
                    EnchantUtils.incrementLevel(stack, Enchantments.POWER);
                    return true;
                }
            }
        }
        else if (st instanceof CustomArmorItem) {

            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null) {
                if(modifierCap.getSharpLevel() == 0) {
                    modifierCap.incrementSharp();
                    return true;
                }
                else if(modifierCap.getSharpLevel() < MAX_MODIFIER) {
                    modifierCap.incrementSharp();
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addSilkTouchModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceAxeItem || st instanceof ForceShovelItem || st instanceof ForcePickaxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(!modifierCap.hasSilk()) {
                    modifierCap.setSilk(true);
                    stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addForceModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem || st instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null) {
                if(modifierCap.getForceLevel() == 0) {
                    modifierCap.incrementForce();
                    stack.addEnchantment(Enchantments.KNOCKBACK, 1);
                    return true;
                }
                else if(modifierCap.getForceLevel() < MAX_MODIFIER) {
                    modifierCap.incrementForce();
                    EnchantUtils.incrementLevel(stack, Enchantments.KNOCKBACK);
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addHeatModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem || st instanceof ForceAxeItem || st instanceof ForceShovelItem || st instanceof ForcePickaxeItem || st instanceof ForceShearsItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(!modifierCap.hasHeat()) {
                    modifierCap.setHeat(true);
                    return true;
                }
            }
        }
        else if (stack.getItem() instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(!modifierCap.hasHeat()) {
                    modifierCap.setHeat(true);
                    return true;
                }
            }
        }
        return false;
    }

    static boolean addSpeedModifier(ItemStack stack) {
        if(stack.getItem() instanceof ForceShovelItem || stack.getItem() instanceof ForcePickaxeItem || stack.getItem() instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSpeedLevel() == 0) {
                    modifierCap.incrementSpeed();
                    stack.addEnchantment(Enchantments.EFFICIENCY, 1);
                    return true;
                }
                else if(modifierCap.getSpeedLevel() < MAX_MODIFIER) {
                    modifierCap.incrementSpeed();
                    EnchantUtils.incrementLevel(stack, Enchantments.EFFICIENCY);
                    return true;
                }
            }
        }
        else if (stack.getItem() instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSpeedLevel() < MAX_MODIFIER) {
                    modifierCap.incrementSpeed();
                    return true;
                }
            }
        }
        else if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier modifierCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSpeedLevel() < 3) {
                    modifierCap.incrementSpeed();
                    return true;
                }
            }
        }
        return false;
    }

    public int fill(FluidStack resource, FluidAction action) {
        FluidStack resourceCopy = resource.copy();

        if(action.execute()) {
            if (tank.getFluid() != null) {
                if (tank.getFluid().getFluid() == ForceFluids.FORCE_FLUID_SOURCE.get() || tank.getFluidAmount() == 0) {
                    tank.fill(resourceCopy, action);
                }
            }
            if (tank.getFluid() == null) {
                tank.fill(resourceCopy, action);
            }
        }
        return resource.getAmount();
    }

    public Fluid getFluid() {
        if (!tank.isEmpty()) {
            return tank.getFluid().getFluid();
        }
        return null;
    }

    public FluidStack getFluidStack() {
        if (!tank.isEmpty()) {
            return tank.getFluid();
        }
        return null;
    }

    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public float getFluidPercentage() {
        return (float) tank.getFluidAmount() / (float) tank.getCapacity();
    }

    public int getFluidGuiHeight(int maxHeight) {
        return (int) Math.ceil(getFluidPercentage() * (float) maxHeight);
    }

    protected boolean isFluidEqual(FluidStack fluid) {
        return isFluidEqual(fluid.getFluid());
    }

    protected boolean isFluidEqual(Fluid fluid) {
        return tank.getFluid().getFluid().equals(fluid);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(Reference.MOD_ID + ".container.infuser");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new InfuserContainer(id, playerInv, this);
    }

    /******** Fakeout stuff for IRecipe *********************/
    @Override
    public void clear() {
    }

    @Override
    public ItemStack decrStackSize(int arg0, int arg1) {
      return ItemStack.EMPTY;
    }

    @Override
    public int getSizeInventory() {
      return 0;
    }

    @Override
    public ItemStack getStackInSlot(int arg0) {
      return ItemStack.EMPTY;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity arg0) {
      return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int arg0) {
      return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int arg0, ItemStack arg1) {}
}