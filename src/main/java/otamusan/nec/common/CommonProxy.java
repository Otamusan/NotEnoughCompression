package otamusan.nec.common;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import otamusan.nec.blocks.CompressedBlockDiversity.BlockCompressed;
import otamusan.nec.blocks.CompressedBlockDiversity.BlockCompressedCrops;
import otamusan.nec.blocks.CompressedBlockDiversity.BlockCompressedFurnace;
import otamusan.nec.blocks.CompressedBlockDiversity.BlockCompressedSapling;
import otamusan.nec.common.automaticcompression.CompressionInChest;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemBlockCompressed;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.items.CompressedItemDiversity.ItemPlantableCompressed;
import otamusan.nec.recipes.*;
import otamusan.nec.tileentity.TileCompressed;
import otamusan.nec.tileentity.TileCompressedFurnace;
import otamusan.nec.tileentity.TileCompressedHopper;
import otamusan.nec.world.CompressedGenerator;

public class CommonProxy {
    public static IRecipe compression = new Compression();
    public static IRecipe uncompression = new Decompression();
    public static IRecipe uncompressionwithpiston = new DecompressionWithPiston();
    public static IRecipe compressedcrafting = new CompressedCrafting();

    public static void addShapedRecipe(ItemStack item, Object... param) {

        ForgeRegistries.RECIPES
                .register(new ShapedOreRecipe(new ResourceLocation(Lib.MOD_ID, getName(item, param)), item, param)
                        .setRegistryName(Lib.MOD_ID, getName(item, param)));
    }

    public static String getName(Object ob, Object... param) {
        String name = ob.toString() + param.toString();
        return name;
    }

    public void preInit() {
        SupplyItems.capRegister();

        if(NECConfig.CONFIG_TYPES.world.isReplaceVanillaRecipe){
            RecipeReplacer.replaceNECRecipe();
            RecipeReplacer.replaceAll();
        }else{
            compressedcrafting.setRegistryName(new ResourceLocation(Lib.MOD_ID, "compressedcrafting"));
            ForgeRegistries.RECIPES.register(compressedcrafting);
        }

        compression.setRegistryName(new ResourceLocation(Lib.MOD_ID, "compression"));
        ForgeRegistries.RECIPES.register(compression);

        uncompression.setRegistryName(new ResourceLocation(Lib.MOD_ID, "uncompression"));
        ForgeRegistries.RECIPES.register(uncompression);

        uncompressionwithpiston.setRegistryName(new ResourceLocation(Lib.MOD_ID, "uncompressionwithpiston"));
        ForgeRegistries.RECIPES.register(uncompressionwithpiston);

        BLOCKBASE = new BlockCompressed(Material.BARRIER);
        BLOCKBASE.setRegistryName("compressedblock");
        BLOCKBASE.setUnlocalizedName("compressedblock");
        ForgeRegistries.BLOCKS.register(BLOCKBASE);

        BLOCKFURNECE = new BlockCompressedFurnace();
        BLOCKSAPLING = new BlockCompressedSapling();
        BLOCKCROP = new BlockCompressedCrops();

        if (NECConfig.CONFIG_TYPES.using.isSpecializeFurnace) {
            BLOCKBASE.addChildren(BLOCKFURNECE);
        }

        if (NECConfig.CONFIG_TYPES.using.isSpecializeSaplings) {

            BLOCKBASE.addChildren(BLOCKSAPLING);
        }

        if (NECConfig.CONFIG_TYPES.using.isSpecializeCrops) {

            BLOCKBASE.addChildren(BLOCKCROP);
        }

        GameRegistry.registerWorldGenerator(new CompressedGenerator(), 1000);

        ITEMBASE = new ItemCompressed();

        ((Item) ITEMBASE).setRegistryName(Lib.MOD_ID + ":compresseditem_base");
        ((Item) ITEMBASE).setUnlocalizedName("compresseditem");

        ForgeRegistries.ITEMS.register((Item) ITEMBASE);

        ITEMBLOCK = new ItemBlockCompressed();
        ITEMBASE.addChildren(ITEMBLOCK);

        ITEMPLANTABLE = new ItemPlantableCompressed();
        ITEMBASE.addChildren(ITEMPLANTABLE);
    }

    public void init() {

    }

    public void postInit() {

    }

    public World getClientWorld() {
        return null;
    }

    public void registerTileEntity() {
        GameRegistry.registerTileEntity(TileCompressed.class, new ResourceLocation(Lib.MOD_ID, "tilecompressed"));
        GameRegistry.registerTileEntity(TileCompressedFurnace.class,
                new ResourceLocation(Lib.MOD_ID, "tilecompressedfurnace"));
        GameRegistry.registerTileEntity(TileCompressedHopper.class,
                new ResourceLocation(Lib.MOD_ID, "tilecompressedhopper"));

    }

    public static BlockCompressedFurnace BLOCKFURNECE;
    public static BlockCompressedSapling BLOCKSAPLING;
    public static BlockCompressedCrops BLOCKCROP;

    public static BlockCompressed BLOCKBASE;
    public static ItemCompressed ITEMBASE;
    public static ItemBlockCompressed ITEMBLOCK;
    public static ItemPlantableCompressed ITEMPLANTABLE;

    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(new CompressionInChest());
        MinecraftForge.EVENT_BUS.register(new BreakCompressedBlock());
        MinecraftForge.EVENT_BUS.register(new SupplyItems());
    }
}