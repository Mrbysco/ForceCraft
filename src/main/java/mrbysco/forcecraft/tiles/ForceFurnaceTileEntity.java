package mrbysco.forcecraft.tiles;

import mrbysco.forcecraft.Reference;
import mrbysco.forcecraft.container.ForceFurnaceContainer;
import mrbysco.forcecraft.registry.ForceRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ForceFurnaceTileEntity extends AbstractForceFurnaceTile {
	//2 times faster
	//Auto output to nearest IInventory that isn't a hopper
	//Stores the fuel between operations
	//Once a core is inserted it can't be taken out!!
	//Heat Core makes it smelt double the amount of the fuel item
	//Cold Core has separate recipes
	//Grinding Core has separate recipes
	//Experience Core = Double xp
	//Any fuel (Golden Power Source smelts 10)
	public ForceFurnaceTileEntity() {
		super(ForceRegistry.FURNACE_TILE.get());
	}

	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(Reference.MOD_ID + ".container.force_furnace");
	}

	protected Container createMenu(int id, PlayerInventory player) {
		return new ForceFurnaceContainer(id, player, this, this.furnaceData);
	}

	@Override
	protected int getBurnTime(ItemStack fuel) {
		return super.getBurnTime(fuel);
	}

	public static boolean isFuel(ItemStack stack) {
		return stack.getItem() == ForceRegistry.FORCE_GEM.get();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 1) {
			return true;
		} else {
			return isFuel(stack);
		}
	}
}
