package mrbysco.forcecraft.tiles;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.capablilities.forcerod.IForceRodModifier;
import mrbysco.forcecraft.capablilities.toolmodifier.IToolModifier;
import mrbysco.forcecraft.container.InfuserContainer;
import mrbysco.forcecraft.items.CustomArmorItem;
import mrbysco.forcecraft.items.tools.ForceAxeItem;
import mrbysco.forcecraft.items.tools.ForcePickaxeItem;
import mrbysco.forcecraft.items.tools.ForceRodItem;
import mrbysco.forcecraft.items.tools.ForceShearsItem;
import mrbysco.forcecraft.items.tools.ForceShovelItem;
import mrbysco.forcecraft.items.tools.ForceSwordItem;
import mrbysco.forcecraft.registry.ForceFluids;
import mrbysco.forcecraft.registry.ForceRegistry;
import mrbysco.forcecraft.registry.ForceTags;
import mrbysco.forcecraft.tiles.energy.ForceEnergyStorage;
import mrbysco.forcecraft.util.EnchantUtils;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_FORCEROD;
import static mrbysco.forcecraft.capablilities.CapabilityHandler.CAPABILITY_TOOLMOD;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class InfuserTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    protected FluidTank tank = new FluidTank(50000) {
        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (!isFluidEqual(resource))
                return null;
            if (action.simulate()) {
                int amount = tank.getFluidAmount() - resource.getAmount() < 0 ? tank.getFluidAmount() : resource.getAmount();
                return new FluidStack(tank.getFluid(), amount);
            }
            return super.drain(resource.getAmount(), action);
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return super.drain(maxDrain, action);
        }
    };
    private LazyOptional<IFluidHandler> tankHolder = LazyOptional.of(() -> tank);

    public final ItemStackHandler handler = new ItemStackHandler(11) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }
    };
    private LazyOptional<IItemHandler> handlerHolder = LazyOptional.of(() -> handler);

    public ForceEnergyStorage energyStorage = new ForceEnergyStorage(MAX_POWER, 1000);
    private LazyOptional<ForceEnergyStorage> energyHolder = LazyOptional.of(() -> energyStorage);

    private NonNullList<ItemStack> infuserContents = NonNullList.create();

    public static List<Item> validToolList = new ArrayList<>();
    public static List<Item> validModifierList = new ArrayList<>();
    public boolean canWork = false;

    public int processTime = 0;
    public int maxProcessTime = 17;

    public static int MAX_POWER = 1000000;
    public static int RF_PER_TICK = 20;
    public static int PR_PER_TICK_INPUT = 200;

    public int fluidContained;

    public InfuserTileEntity(TileEntityType<?> type) {
        super(type);
        populateToolList();
        populateModifierList();
    }

    public InfuserTileEntity() {
        this(ForceRegistry.INFUSER_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        //Items
        handler.deserializeNBT(nbt.getCompound("ItemStackHandler"));
        ItemStackHelper.loadAllItems(nbt, this.infuserContents);
        energyStorage.setEnergy(nbt.getInt("EnergyHandler"));
        tank.readFromNBT(nbt);

        super.markDirty(); //TODO: Is this markdirty needed?
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);

        //Items
        compound.put("ItemStackHandler", handler.serializeNBT());
        compound.putInt("EnergyHandler", energyStorage.getEnergyStored());
        ItemStackHelper.saveAllItems(compound, this.infuserContents);
        tank.writeToNBT(compound);

        return compound;
    }

    @Override
    public void tick() {
        fluidContained = tank.getFluidAmount();
        if (world != null) {
            if (!world.isRemote) {
                processForceGems();
                if (!canWork) {
                    if (processTime == maxProcessTime) {
                        this.markDirty();
                        processTool();
                    }
                    processTime++;
                }
            }
        }
    }

    //Processes force Gems in the force infuser slot
    private void processForceGems() {
        if (handler.getStackInSlot(9).getItem() == ForceRegistry.FORCE_GEM.get()) {
            FluidStack force = new FluidStack(ForceFluids.FORCE_FLUID_SOURCE.get(), 500);

            if (tank.getFluidAmount() < tank.getCapacity() - 100) {
                fill(force, FluidAction.EXECUTE);
                handler.getStackInSlot(9).shrink(1);

                markDirty();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
            }
        }
    }

    private void processTool() {
        if (hasValidTool()) {
            for (int i = 0; i < 8; i++) {
                if (hasValidModifer(i)) {
                    ItemStack mod = getModifier(i);
                    ItemStack stack = handler.getStackInSlot(8);
                    boolean success = applyModifier(stack, mod);
                    if (success) {
                        handler.setStackInSlot(i, ItemStack.EMPTY);
                        tank.drain(1000, FluidAction.EXECUTE);
                        energyStorage.consumePower(RF_PER_TICK);
                        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
                    }
                }
            }
        }
        canWork = false;
        processTime = 0;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        final CompoundNBT compound = new CompoundNBT();
        markDirty(); //TODO: Another markdirty
        write(compound);
        return new SUpdateTileEntityPacket(this.pos, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.read(getBlockState(), packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        markDirty(); //TODO: Another markdirty
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.handlerHolder == null)
                this.handlerHolder = LazyOptional.of(() -> handler);
            return handlerHolder.cast();
        }
        if (capability == FLUID_HANDLER_CAPABILITY) {
            if (this.tankHolder == null)
                this.tankHolder = LazyOptional.of(() -> tank);
            return tankHolder.cast();
        }
        if (capability == CapabilityEnergy.ENERGY) {
            if (this.energyHolder == null)
                this.energyHolder = LazyOptional.of(() -> energyStorage);
            return energyHolder.cast();
        }

        return super.getCapability(capability, facing);
    }

    private boolean hasValidTool() {
        if (!handler.getStackInSlot(8).isEmpty()) {
            return handler.getStackInSlot(8).getItem().isIn(ForceTags.VALID_INFUSER_TOOLS);
        }
        return false;
    }

    private boolean hasValidModifer(int slot) {
        if (!handler.getStackInSlot(8).isEmpty()) {
            for (int j = 0; j < Reference.numModifiers - 1; j++) {
                if (handler.getStackInSlot(slot).getItem() == validModifierList.get(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void populateToolList() {
        validToolList.add(ForceRegistry.FORCE_PICKAXE.get());
        validToolList.add(ForceRegistry.FORCE_AXE.get());
        validToolList.add(ForceRegistry.FORCE_SHOVEL.get());
        validToolList.add(ForceRegistry.FORCE_SWORD.get());
        validToolList.add(ForceRegistry.FORCE_ROD.get());
        validToolList.add(ForceRegistry.FORCE_SHEARS.get());
        validToolList.add(ForceRegistry.FORCE_HELMET.get());
        validToolList.add(ForceRegistry.FORCE_CHEST.get());
        validToolList.add(ForceRegistry.FORCE_LEGS.get());
        validToolList.add(ForceRegistry.FORCE_BOOTS.get());
        validToolList.add(ForceRegistry.FORCE_TORCH_ITEM.get());
    }

    private void populateModifierList() {
        validModifierList.add(ForceRegistry.FORCE_NUGGET.get());
        validModifierList.add(ForceRegistry.CLAW.get());
        validModifierList.add(ForceRegistry.FORTUNE.get());
        validModifierList.add(Items.SUGAR);
        validModifierList.add(Items.COAL);
        validModifierList.add(ForceRegistry.GOLDEN_POWER_SOURCE.get());
        validModifierList.add(ForceRegistry.FORTUNE_COOKIE.get());
        validModifierList.add(Items.FLINT);
        validModifierList.add(Items.WHITE_DYE);
        validModifierList.add(Items.ORANGE_DYE);
        validModifierList.add(Items.MAGENTA_DYE);
        validModifierList.add(Items.LIGHT_BLUE_DYE);
        validModifierList.add(Items.YELLOW_DYE);
        validModifierList.add(Items.LIME_DYE);
        validModifierList.add(Items.PINK_DYE);
        validModifierList.add(Items.GRAY_DYE);
        validModifierList.add(Items.LIGHT_GRAY_DYE);
        validModifierList.add(Items.CYAN_DYE);
        validModifierList.add(Items.PURPLE_DYE);
        validModifierList.add(Items.BLUE_DYE);
        validModifierList.add(Items.BROWN_DYE);
        validModifierList.add(Items.GREEN_DYE);
        validModifierList.add(Items.RED_DYE);
        validModifierList.add(Items.BLACK_DYE);
        validModifierList.add(Items.EXPERIENCE_BOTTLE);
        validModifierList.add(Items.SPIDER_EYE);
        validModifierList.add(Items.ARROW);
        validModifierList.add(Items.GHAST_TEAR);
        validModifierList.add(ForceRegistry.SOUL_WAFER.get());
        validModifierList.add(Items.FEATHER);
        validModifierList.add(Items.ENDER_PEARL);
        validModifierList.add(Items.GLOWSTONE_DUST);
        validModifierList.add(Items.POTION);
        validModifierList.add(Items.CLOCK);
        validModifierList.add(Items.CRAFTING_TABLE);
        validModifierList.add(ForceRegistry.FORCE_LOG_ITEM.get());
        validModifierList.add(Items.COBWEB);
        validModifierList.add(Items.OBSIDIAN);
        validModifierList.add(Items.BRICKS);
    }

    private ItemStack getModifier(int slot) {
        if (!handler.getStackInSlot(8).isEmpty()) {
            for (int j = 0; j < Reference.numModifiers - 1; j++) {
                if (handler.getStackInSlot(slot).getItem() == validModifierList.get(j)) {
                    return handler.getStackInSlot(slot);
                }
            }
        }
        return null;
    }

    private boolean applyModifier(ItemStack stack, ItemStack mod) {
        Item modifier = mod.getItem();
        //Speed
        if (modifier == Items.SUGAR)
            return addSpeedModifier(stack);
        //Heat
        if (modifier == Items.COAL)
            return addHeatModifier(stack);
        //Grinding
//        if (modifier == Items.FLINT)
//            return addGrindingModifier(stack);
        //Knockback
        if (modifier == ForceRegistry.FORCE_NUGGET.get())
            return addForceModifier(stack);
        //Touch
        if (modifier == Items.COBWEB)
            return addSilkTouchModifier(stack);
        //Damage
        if (modifier == ForceRegistry.CLAW.get())
            return addDamageModifier(stack);
        //Luck
        if (modifier == ForceRegistry.FORTUNE.get())
            return addLuckModifier(stack);
        //Light
        if (modifier == Items.GLOWSTONE_DUST)
            return addLightModifier(stack);
        //Sturdy
        if (modifier == Items.BRICKS || modifier == Items.OBSIDIAN)
            return addSturdyModifier(stack);
        //Lumberjack
        if (modifier == ForceRegistry.FORCE_LOG_ITEM.get())
            return addLumberjackModifier(stack);
        //Healing
        if (modifier == Items.GHAST_TEAR)
            return addHealingModifier(stack);
        //Ender
        if (modifier == Items.ENDER_PEARL)
            return addEnderModifier(stack);
        //Bleeding
        if (modifier == Items.ARROW)
            return addBleedingModifier(stack);
        //Bane
        if (modifier == Items.SPIDER_EYE)
            return addBaneModifier(stack);
        //Wing
        if (modifier == Items.FEATHER)
            return addWingModifier(stack);
        //Sight/Camo
        if (modifier == Items.POTION) {
            List<EffectInstance> effects = PotionUtils.getEffectsFromStack(mod);
            for (EffectInstance e : effects) {
                if (e.getPotion() == Effects.NIGHT_VISION) {
                    return addSightModifier(stack);
                }
                if (e.getPotion() == Effects.INVISIBILITY) {
                    return addCamoModifier(stack);
                }
            }
        }
        //Rainbow
        if (modifier == Items.BLUE_DYE) {
            return addRainbowModifier(stack);
        }
        //Time
        if(modifier == Items.CLOCK)
            return addTimeModifier(stack);

        return false;
    }

    private boolean addLightModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.hasLight()) {
                rodCap.setLight(true);
                return true;
            }
        }
        return false;
    }

    private boolean addCamoModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.hasCamoModifier()) {
                rodCap.setCamoModifier(true);
                return true;
            }
        }
        return false;
    }

    private boolean addSightModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.hasSightModifier()) {
                rodCap.setSightModifier(true);
                return true;
            }
        }
        return false;
    }

    private boolean addWingModifier(ItemStack stack) {
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

    private boolean addBaneModifier(ItemStack stack) {
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

    private boolean addBleedingModifier(ItemStack stack) {
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

    private boolean addEnderModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceRodItem) {
            IForceRodModifier rodCap = stack.getCapability(CAPABILITY_FORCEROD).orElse(null);
            if(rodCap != null && rodCap.isRodofEnder()) {
                rodCap.setEnderModifier(true);
                return true;
            }
        }
        return false;
    }

    private boolean addHealingModifier(ItemStack stack) {
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

    private boolean addLumberjackModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null && modifierCap.hasLumberjack()) {
                modifierCap.setLumberjack(true);
                return true;
            }
        }
        return false;
    }

    private boolean addRainbowModifier(ItemStack stack) {
        if (stack.getItem() instanceof ForceShearsItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                modifierCap.setRainbow(true);
                return true;
            }
        }
        return false;
    }

    private boolean addTimeModifier(ItemStack stack) {
        if(stack.getItem() == ForceRegistry.FORCE_TORCH_ITEM.get()) {
            handler.setStackInSlot(8, new ItemStack(ForceRegistry.TIME_TORCH.get(), 1));
            return true;
        }
        return false;
    }

    private boolean addSturdyModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem || st instanceof ForceAxeItem || st instanceof ForceShovelItem || st instanceof ForcePickaxeItem || st instanceof ForceRodItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSturdyLevel() == 0) {
                    modifierCap.incrementSturdy();
                    stack.addEnchantment(Enchantments.UNBREAKING, 1);
                    return true;
                }
                else if(modifierCap.getSturdyLevel() < 10) {
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
                else if(modifierCap.getSturdyLevel() < 10) {
                    modifierCap.incrementSturdy();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean addLuckModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForcePickaxeItem || st instanceof ForceShovelItem || st instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getLuckLevel() == 0) {
                    modifierCap.incrementLuck();
                    stack.addEnchantment(Enchantments.FORTUNE, 1);
                    return true;
                }
                else if(modifierCap.getLuckLevel() < 10) {
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
                else if(modifierCap.getLuckLevel() < 10) {
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
                else if(modifierCap.getLuckLevel() < 10) {
                    modifierCap.incrementLuck();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean addDamageModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSharpLevel() == 0) {
                    modifierCap.incrementSharp();
                    stack.addEnchantment(Enchantments.SHARPNESS, 1);
                    return true;
                }
                else if(modifierCap.getSharpLevel() < 10) {
                    modifierCap.incrementSharp();
                    EnchantUtils.incrementLevel(stack, Enchantments.SHARPNESS);
                    return true;
                }
            }
        }
        else if (st instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSharpLevel() == 0) {
                    modifierCap.incrementSharp();
                    return true;
                }
                else if(modifierCap.getSharpLevel() < 10) {
                    modifierCap.incrementSharp();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean addSilkTouchModifier(ItemStack stack) {
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

    private boolean addForceModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceSwordItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getForceLevel() == 0) {
                    modifierCap.incrementForce();
                    stack.addEnchantment(Enchantments.KNOCKBACK, 1);
                    return true;
                }
                else if(modifierCap.getForceLevel() < 10) {
                    modifierCap.incrementForce();
                    EnchantUtils.incrementLevel(stack, Enchantments.KNOCKBACK);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean addHeatModifier(ItemStack stack) {
        Item st = stack.getItem();
        if(st instanceof ForceAxeItem || st instanceof ForceShovelItem || st instanceof ForcePickaxeItem) {
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

    private boolean addSpeedModifier(ItemStack stack) {
        if(stack.getItem() instanceof ForceShovelItem || stack.getItem() instanceof ForcePickaxeItem || stack.getItem() instanceof ForceAxeItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSpeedLevel() == 0) {
                    modifierCap.incrementSpeed();
                    stack.addEnchantment(Enchantments.EFFICIENCY, 1);
                    return true;
                }
                else if(modifierCap.getSpeedLevel() < 10) {
                    modifierCap.incrementSpeed();
                    EnchantUtils.incrementLevel(stack, Enchantments.EFFICIENCY);
                    return true;
                }
            }
        }
        else if (stack.getItem() instanceof CustomArmorItem) {
            IToolModifier modifierCap = stack.getCapability(CAPABILITY_TOOLMOD).orElse(null);
            if(modifierCap != null ) {
                if(modifierCap.getSpeedLevel() == 0) {
                    modifierCap.incrementSpeed();
                    return true;
                }
                else if(modifierCap.getSpeedLevel() < 10) {
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
        return tank.getFluid().equals(fluid);
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if (this.handlerHolder != null) {
            this.handlerHolder.invalidate();
            this.handlerHolder = null;
        }
        if (this.energyHolder != null) {
            this.energyHolder.invalidate();
            this.energyHolder = null;
        }
        if (this.tankHolder != null) {
            this.tankHolder.invalidate();
            this.tankHolder = null;
        }
    }

    @Override
    public void remove() {
        super.remove();

        if(handlerHolder != null) {
            handlerHolder.invalidate();
        }
        if(energyHolder != null) {
            energyHolder.invalidate();
        }
        if(tankHolder != null) {
            tankHolder.invalidate();
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(Reference.MOD_ID + ".container.infuser");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new InfuserContainer(id, playerInv, this);
    }
}