package com.SkyIsland.AgeOfTitans.items;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.utils.TinkersConstructUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.Smeltery;
import tconstruct.smeltery.TinkerSmeltery;

public class Titarillium {
	
	public static final int id = 5004;

	public static final Item.ToolMaterial Material = EnumHelper.addToolMaterial("titarillium", 12, 600, 350.0F, 13.0F, 35);
	
	public static final TitarilliumIngot ingot = new TitarilliumIngot("titarillium_ingot");
	
	public static final TitarilliumNugget nugget = new TitarilliumNugget("titarillium_nugget");
	
	public static void preInit() {
	
		
		GameRegistry.registerItem(ingot, "titarillium_ingot");
		GameRegistry.registerItem(nugget, "titarillium_nugget");
		TitarilliumFluid.preinit();
		
		
		TinkersConstructUtils.addMaterial(id, "titarillium", "Titarillium", 1200, 1000, 6, 11, 1.90f, 5.0f, 18, 2.6f, 0.4f, EnumChatFormatting.DARK_RED.toString(), 
				//255 << 24 | 206 << 16 | 34 << 8 | 40);
				0xC03040);
		TinkersConstructUtils.addMaterialItem(id, new ItemStack(ingot), 1);
		TinkersConstructUtils.addPartBuilderMaterial(id, new ItemStack(ingot), 1);
		TinkersConstructUtils.addPartCastinMaterial(TitarilliumFluid.fluid.getName(), id);
		TinkersConstructUtils.addMeltingItem(TitarilliumFluid.fluid.getName(), new ItemStack(ingot), new ItemStack(Blocks.iron_block), 144, 500);
		
		GameRegistry.addShapelessRecipe(new ItemStack(ingot), nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget);
		GameRegistry.addShapelessRecipe(new ItemStack(nugget, 9), ingot);
		
		//registerAlloys();
		
	}
	
	public static void registerAlloys() {
		Smeltery.addAlloyMixing((new FluidStack(TitarilliumFluid.fluid, 144)), 
				new FluidStack(TinkerSmeltery.moltenEmeraldFluid, 288),
				new FluidStack(FluidRegistry.getFluid("elementium.molten"), 144),
				new FluidStack(TitanAmalgam.AmalgamFluid.fluid, 288));
		
		
		
		ItemStack cast = new ItemStack(TinkerSmeltery.metalPattern);
		TConstructRegistry.getTableCasting().addCastingRecipe(new ItemStack(Titarillium.ingot),
				new FluidStack(TitarilliumFluid.fluid, 144), cast, false, 30);


//		NBTTagList tagList = new NBTTagList();
//		// if you have access to the fluid-instance you can also do FluidStack.writeToNBT
//		NBTTagCompound fluid = ((new FluidStack(Vectorium.fluid, 288)).writeToNBT(new NBTTagCompound()));
//		tagList.appendTag(fluid);
//		//1 ingot many
//		fluid = new NBTTagCompound();
//		
//		//String name, String fluidName, String blockName, String texture, int density, int viscosity, int temperature, Material material)
//		fluid.setString("FluidName", "manyullyn");
//		fluid.setInteger("Amount", 144); // 3/4 ingot
//		tagList.appendTag(fluid);
//		//2 ingots black steel
//		
//		fluid = new NBTTagCompound();
//		fluid.setString("FluidName", "black_steel");
//		fluid.setInteger("Amount", 288); // 1/4 ingot
//		tagList.appendTag(fluid);
//		//
//		fluid = new NBTTagCompound();
//		fluid.setString("FluidName", "damascus_steel");
//		fluid.setInteger("Amount", 576); // 1/4 ingot
//		tagList.appendTag(fluid);
//		//
//		
//		NBTTagCompound message = new NBTTagCompound();
//		message.setTag("alloy", tagList);
//		FMLInterModComms.sendMessage("tconstruct", "alloy", message);
	}
	
	public static class TitarilliumFluid extends BlockFluidClassic {

		public static final String still 
			= AgeOfTitans.MODID + ":titarillium_still";
		
		public static final String moving 
		= AgeOfTitans.MODID + ":titarillium_flow";
		
		public static IIcon stillIcon, flowingIcon;
		
		public static Fluid fluid;
		
		public static TitarilliumFluid block;
		
		public static TitarilliumBucket bucket;
		
		public static String blockName = "titarilliumFluidBlock";
		
		public static void preinit() {
			fluid = new Fluid("TitarilliumFluid");
			fluid.setGaseous(false);
			fluid.setLuminosity(4);
			
			FluidRegistry.registerFluid(fluid);
			block = new TitarilliumFluid();
			block.setBlockName(blockName);
			GameRegistry.registerBlock(block, blockName);
			
			fluid.setBlock(block);
			
			bucket = new TitarilliumBucket(block);
			GameRegistry.registerItem(bucket, TitarilliumBucket.unlocalizedName);
			FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(bucket), new ItemStack(Items.bucket));
		}
		
		public TitarilliumFluid() {
			super(fluid, net.minecraft.block.material.Material.lava);
			this.setCreativeTab(AgeOfTitans.creativeTab);
		}
		
		@Override
		public void onEntityCollidedWithBlock( World world, int x, int y, int z, Entity entity ) {
			if (entity instanceof EntityLivingBase) {
				((EntityLivingBase)entity).setFire(60);
			}
		}
		
		@Override
        public IIcon getIcon(int side, int meta) {
                return (side == 0 || side == 1)? stillIcon : flowingIcon;
        }
       
        @SideOnly(Side.CLIENT)
        @Override
        public void registerBlockIcons(IIconRegister register) {
                stillIcon = register.registerIcon(still);
                flowingIcon = register.registerIcon(moving);
                fluid.setStillIcon(stillIcon);
                fluid.setFlowingIcon(flowingIcon);
        }
		
		
		
	}
	
	public static class TitarilliumNugget extends Item {
		
		public TitarilliumNugget(String unlocalizedName) {
			super();
	        this.setUnlocalizedName(unlocalizedName);
	        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
	        this.maxStackSize = 64;
	        this.setMaxDamage(0);
	        this.setCreativeTab(AgeOfTitans.creativeTab);
		}
	}
	
	public static class TitarilliumIngot extends Item {
		
		public TitarilliumIngot(String unlocalizedName) {
			super();
	        this.setUnlocalizedName(unlocalizedName);
	        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
	        this.maxStackSize = 64;
	        this.setMaxDamage(0);
	        this.setCreativeTab(AgeOfTitans.creativeTab);
		}
		
	}
	
	public static class TitarilliumBucket extends ItemBucket {
		
		public static final String unlocalizedName = "titarillium_bucket";

		public TitarilliumBucket(Block p_i45331_1_) {
			super(p_i45331_1_);
	        this.setUnlocalizedName(unlocalizedName);
	        this.setContainerItem(Items.bucket);
	        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
	        this.maxStackSize = 1;
	        this.setMaxDamage(0);
	        this.setCreativeTab(AgeOfTitans.creativeTab);
		}
		
		
		
	}
	
}
