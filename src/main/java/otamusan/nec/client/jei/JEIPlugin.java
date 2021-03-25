package otamusan.nec.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class JEIPlugin implements IModPlugin {
    @mezz.jei.api.JEIPlugin
    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        for (Item item : ItemCompressed.getCompressedList()) {
            subtypeRegistry.registerSubtypeInterpreter(item, new ISubtypeInterpreter() {
                @Override
                public String apply(ItemStack itemStack) {
                    return ItemCompressed.getOriginal(itemStack).getUnlocalizedName() + ":"
                            + ItemCompressed.getTime(itemStack);
                }
            });
        }
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
    }

    @Override
    public void register(IModRegistry registry) {
        if (NECConfig.CONFIG_TYPES.jei.JEIshowCompression)
            registry.addRecipes(FakeRecipes.getCompressionFakeRecipe(), VanillaRecipeCategoryUid.CRAFTING);
        if (NECConfig.CONFIG_TYPES.jei.JEIshowUncompression)
            registry.addRecipes(FakeRecipes.getUncompressionFakeRecipe(), VanillaRecipeCategoryUid.CRAFTING);
        if (NECConfig.CONFIG_TYPES.jei.JEIshowUncompression)
            registry.addRecipes(FakeRecipes.getUncompressionWithPistonFakeRecipe(), VanillaRecipeCategoryUid.CRAFTING);
        if (NECConfig.CONFIG_TYPES.jei.JEIshowCompressedCraft)
            registry.addRecipes(FakeRecipes.getCompressedCraftFakeRecipe(), VanillaRecipeCategoryUid.CRAFTING);
    }
}
