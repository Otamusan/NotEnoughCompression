package otamusan.nec.client.jei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import otamusan.nec.common.Lib;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class FakeRecipes {

    public static List<IRecipe> getCompressionFakeRecipe() {
        List<IRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < NECConfig.CONFIG_TYPES.jei.JEIshowCompressionTime; i++) {
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                if (new ItemStack(item).isEmpty() || !NECConfig.isCompressible(item)
                        || ItemCompressed.isCompressedItem(item))
                    continue;
                if (NECConfig.isCompressionCatalyst(item)) {
                    continue;
                }
                NonNullList<Ingredient> nonNullList = NonNullList.create();
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                nonNullList.add(Ingredient.fromItem(NECConfig.getCompressionCatalyst()));
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i)));
                recipes.add(new ShapedRecipes(Lib.MOD_ID + "_compression", 3, 3, nonNullList,
                        ItemCompressed.createCompressedItem(new ItemStack(item), i + 1)));
            }
        }
        return recipes;
    }

    public static List<IRecipe> getUncompressionFakeRecipe() {
        List<IRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < NECConfig.CONFIG_TYPES.jei.JEIshowCompressionTime; i++) {
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                if (new ItemStack(item).isEmpty() || !NECConfig.isCompressible(item)
                        || ItemCompressed.isCompressedItem(item))
                    continue;
                NonNullList<Ingredient> nonNullList = NonNullList.create();
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i + 1)));
                ItemStack un = ItemCompressed.createCompressedItem(new ItemStack(item), i);
                un.setCount(8);
                recipes.add(new ShapelessRecipes(Lib.MOD_ID + "_uncompression", un, nonNullList));
            }
        }
        return recipes;
    }

    public static List<IRecipe> getUncompressionWithPistonFakeRecipe() {
        List<IRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < NECConfig.CONFIG_TYPES.jei.JEIshowCompressionTime; i++) {
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                if (new ItemStack(item).isEmpty() || !NECConfig.isCompressible(item)
                        || ItemCompressed.isCompressedItem(item))
                    continue;
                NonNullList<Ingredient> nonNullList = NonNullList.create();
                nonNullList.add(Ingredient.fromStacks(ItemCompressed.createCompressedItem(new ItemStack(item), i + 1)));
                nonNullList.add(Ingredient.fromItem(NECConfig.getDecompressionCatalyst()));

                ItemStack un = ItemCompressed.createCompressedItem(new ItemStack(item), i);
                un.setCount(8);
                recipes.add(new ShapelessRecipes(Lib.MOD_ID + "_uncompressionwithpiston", un, nonNullList));
            }
        }
        return recipes;
    }

    public static List<IRecipe> getCompressedCraftFakeRecipe() {
        List<IRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < NECConfig.CONFIG_TYPES.jei.JEIshowCompressionTime; i++) {
            for (IRecipe recipe : ForgeRegistries.RECIPES.getValues()) {
                ItemStack out = ItemCompressed.createCompressedItem(recipe.getRecipeOutput(), i);
                out.setCount(recipe.getRecipeOutput().getCount());
                NonNullList<Ingredient> in = NonNullList.create();
                for (Ingredient ingredient : recipe.getIngredients()) {
                    if (ingredient.getMatchingStacks().length == 0) {
                        in.add(Ingredient.EMPTY);
                        continue;
                    }
                    in.add(Ingredient
                            .fromStacks(ItemCompressed.createCompressedItem(ingredient.getMatchingStacks()[0], i)));
                }
                if (recipe.getClass() == ShapedRecipes.class) {
                    recipes.add(new ShapedRecipes(Lib.MOD_ID + "_compressedcraft", ((ShapedRecipes) recipe).recipeWidth,
                            ((ShapedRecipes) recipe).recipeHeight, in, out));
                } else if (recipe.getClass() == ShapelessRecipes.class) {
                    recipes.add(new ShapelessRecipes(Lib.MOD_ID + "_compressedcraft", out, in));
                }
            }
        }
        return recipes;
    }
}
