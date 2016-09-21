package com.SkyIsland.AgeOfTitans;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.SkyIsland.AgeOfTitans.blocks.HeartBlock;
import com.SkyIsland.AgeOfTitans.blocks.SaturatedLiquidEtherium;
import com.SkyIsland.AgeOfTitans.items.CR2;
import com.SkyIsland.AgeOfTitans.items.CrystalVinteum;
import com.SkyIsland.AgeOfTitans.items.ItemVectorSword;
import com.SkyIsland.AgeOfTitans.items.TitanAmalgam;
import com.SkyIsland.AgeOfTitans.items.TitanHeart;
import com.SkyIsland.AgeOfTitans.items.Titarillium;
import com.SkyIsland.AgeOfTitans.items.Vectorium;
import com.SkyIsland.AgeOfTitans.items.VinteumBlend;
import com.SkyIsland.AgeOfTitans.mobs.FleshTitan;
import com.SkyIsland.AgeOfTitans.mobs.FriendlyTitan;
import com.SkyIsland.AgeOfTitans.mobs.NormalTitan;
import com.SkyIsland.AgeOfTitans.mobs.Titan;
import com.SkyIsland.AgeOfTitans.thaumcraft.TitanWandCore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;
import tconstruct.armor.TinkerArmor;
import tconstruct.tools.TinkerTools;

@Mod(modid=AgeOfTitans.MODID, name=AgeOfTitans.MODNAME, version=AgeOfTitans.MODVER)
public class AgeOfTitans //Start the class Declaration
{
    //Set the ID of the mod (Should be lower case).
    public static final String MODID = "ageoftitans";
    //Set the "Name" of the mod.
    public static final String MODNAME = "Age Of Titans";
    //Set the version of the mod.
    public static final String MODVER = "0.0.1";
    
    private int modMobID = 0;

    @Instance(value = AgeOfTitans.MODID) //Tell Forge what instance to use.
    public static AgeOfTitans instance;
    
    @SidedProxy(clientSide="com.SkyIsland.AgeOfTitans.ClientProxy", serverSide="com.SkyIsland.AgeOfTitans.CommonProxy")
    public static CommonProxy proxy;
    
    public static Logger logger = LogManager.getLogger(MODID);
    
    public static Item vectorSword;
    
    public static Item titanHeart;
    
    public static Item cr2;
    
    public static CreativeTabs creativeTab;
    
    public static CreativeTabs magicTab;
    
    public static Random random = new Random();
       
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	registerModEntity(NormalTitan.class, "Titan");
    	registerModEntity(FleshTitan.class, "FleshTitan");
    	registerModEntity(FriendlyTitan.class, "FriendlyTitan");
    	registerModEntity(Titan.TitanPart.class, "TitanPart");
    	
    	AgeOfTitans.creativeTab = new CreativeTabs("ageoftitans"){
	    	@Override
	        @SideOnly(Side.CLIENT)
	        public Item getTabIconItem(){
	            return vectorSword;
	        }

	        @Override
	        @SideOnly(Side.CLIENT)
	        public int func_151243_f()
	        {
	            return 0;
	        }
	    };
	    
	    AgeOfTitans.magicTab = new CreativeTabs("ageoftitans.magic"){
	    	@Override
	        @SideOnly(Side.CLIENT)
	        public Item getTabIconItem(){
	            return TitanWandCore.wand;
	        }

	        @Override
	        @SideOnly(Side.CLIENT)
	        public int func_151243_f()
	        {
	            return 1;
	        }
	    };
    	
    	vectorSword = new ItemVectorSword("vector_sword", Vectorium.Material);
    	GameRegistry.registerItem(vectorSword, "vector_sword");
    	cr2 = new CR2();
    	GameRegistry.registerItem(cr2, CR2.unlocalizedName);
    	titanHeart = new TitanHeart();
    	GameRegistry.registerItem(titanHeart, TitanHeart.unlocalizedName);
    	
    	MinecraftForge.EVENT_BUS.register(new FleshTitan.DropHandler());   	    

    	Vectorium.preInit();
    	SaturatedLiquidEtherium.preinit();
    	TitanAmalgam.preInit();
    	Titarillium.preInit();
    	HeartBlock.preInit();
    	new VinteumBlend();
    	VinteumBlend.preInit();
    	new CrystalVinteum();
    	CrystalVinteum.preInit();

    	proxy.preInit();
    	
	    
    	
    }
       
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	//LanguageRegistry.instance().addStringLocalization("entity.Titan.name", "en_US","Titan");
    	proxy.load();
		Vectorium.registerAlloys();
		TitanAmalgam.registerAlloys();
		Titarillium.registerAlloys();
    	initRecipes();
		
    }
       
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit();
    }
    
    public void registerModEntity(Class<? extends Entity> parEntityClass, String parEntityName)
    	{
    	    EntityRegistry.registerModEntity(parEntityClass, parEntityName, modMobID++, 
    	          AgeOfTitans.instance, 80, 3, false);
    	    
    	    BiomeGenBase[] biomes = new BiomeGenBase[0];
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.CONIFEROUS));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.DEAD));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.DENSE));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.DRY));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.FOREST));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.HILLS));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.HOT));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.JUNGLE));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.LUSH));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.MESA));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.MAGICAL));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.MOUNTAIN));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.PLAINS));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.WASTELAND));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SWAMP));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SNOWY));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SAVANNA));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.RIVER));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SANDY));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SPOOKY));
    		biomes = ArrayUtils.addAll(biomes, BiomeDictionary.getBiomesForType(Type.SPARSE));
    		EntityRegistry.addSpawn(NormalTitan.class, 7, 1, 3, EnumCreatureType.monster, biomes);
    		EntityRegistry.addSpawn(FleshTitan.class, 1, 1, 3, EnumCreatureType.monster, biomes);
    	    //EntityRegistry.addSpawn(parEntityClass, 1, 1, 3, EnumCreatureType.monster, BiomeGenBase.plains);
    	}
    
    private void initRecipes() {
    	logger.info("Starting recipe registration");
    	
    	GameRegistry.addShapelessRecipe(new ItemStack(cr2, 1), new Object[]{Blocks.redstone_block,
    			Blocks.coal_block, Items.quartz, Items.flint});
    	
    	GameRegistry.addShapelessRecipe(new ItemStack(TinkerArmor.heartCanister, 1, 1), 
    			new Object[]{titanHeart});
    	//4149:5002
    	GameRegistry.addShapedRecipe(new ItemStack(vectorSword), " a ", "bcb", "def", 
    			'a', new ItemStack(TinkerTools.largeSwordBlade, 1, Vectorium.id),
    			'b', Items.fishing_rod, 'c', titanHeart, 'd', Blocks.lever, 
    			'e', new ItemStack(TinkerTools.toolRod, 1, 6), 'f', Blocks.piston);
    	
    	GameRegistry.addShapedRecipe(new ItemStack(HeartBlock.block, 8), "aaa", "aba", "aaa",
    			'a', Blocks.stone, 'b', titanHeart);
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /////// TEST CODE
//    public void onItemDrop(ItemTossEvent event) {
//    	System.out.println(event.entityItem.getEntityItem().getItemDamage()
//    			+ " [" + event.entityItem.getEntityItem().getItemDamage() + "]");
//    }
}
