package mrbysco.forcecraft.recipe;

import mrbysco.forcecraft.Reference;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipeType<RECIPE_TYPE extends InfuseRecipe> implements IRecipeType<RECIPE_TYPE> {

  public static final ModRecipeType<InfuseRecipe> INFUSER = create("infuser");

  private static <RECIPE_TYPE extends InfuseRecipe> ModRecipeType<RECIPE_TYPE> create(String name) {
    ModRecipeType<RECIPE_TYPE> type = new ModRecipeType<>(name);
    return type;
  }

  private String registryName;

  private ModRecipeType(String name) {
    this.registryName = name;
  }

  @Override
  public String toString() {
    return Reference.MOD_ID + ":" + registryName;
  }
}
