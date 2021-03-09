package mrbysco.forcecraft.recipe;

import java.util.HashSet;
import java.util.Set;

import mrbysco.forcecraft.tiles.InfuserTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class InfuseRecipe implements IRecipe<InfuserTileEntity> {

	private static final Set<String> HASHES = new HashSet<>();
	public static final Set<InfuseRecipe> RECIPES = new HashSet<>();
	private final ResourceLocation id;
    public NonNullList<Ingredient> input = NonNullList.withSize(8, Ingredient.EMPTY);
    String result;
    

    public InfuseRecipe(ResourceLocation id, NonNullList<Ingredient> input, String result) {
      super();
      this.id = id;
      this.input = input;
      this.result = result;
    }
    
	@Override
	public boolean matches(InfuserTileEntity inv, World worldIn) {
		for(int i=0; i<input.size();i++) {
			Ingredient current = input.get(i);
			ItemStack stack = inv.handler.getStackInSlot(i);
			if(current.test(stack)) {
				//
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InfuserTileEntity inv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFit(int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceLocation getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRecipeType<?> getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
