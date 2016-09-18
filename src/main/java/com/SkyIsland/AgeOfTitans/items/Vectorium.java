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

public class Vectorium {
	
	public static final int id = 5002;

	public static final Item.ToolMaterial Material = EnumHelper.addToolMaterial("vectorium", 10, ItemVectorSword.durability, 30.0F, 10.0F, 30);
	
	public static final VectoriumIngot ingot = new VectoriumIngot("vectorium_ingot");
	
	public static void preInit() {
	
		
		GameRegistry.registerItem(ingot, "vectorium_ingot");
		VectoriumFluid.preinit();
		
		
		TinkersConstructUtils.addMaterial(id, "vectorium", "Vectorium", 1400, 11, 6, 8, 1.75f, 4.0f, 20, 2.0f, 0.9f, EnumChatFormatting.AQUA.toString(), 
				//255 << 24 | 206 << 16 | 34 << 8 | 40);
				0x84CEFF);
		
		TinkersConstructUtils.addMaterialItem(id, new ItemStack(ingot), 1);
		TinkersConstructUtils.addPartBuilderMaterial(id, new ItemStack(ingot), 1);
		TinkersConstructUtils.addPartCastinMaterial(VectoriumFluid.fluid.getName(), id);
		TinkersConstructUtils.addMeltingItem(VectoriumFluid.fluid.getName(), new ItemStack(ingot), new ItemStack(Blocks.iron_block), 144, 500);
		
		
		//registerAlloys();
		
	}
	
	public static void registerAlloys() {
		System.out.println("Beginning Alloy Plug");
		
		//TinkerSmeltery.registerFluid(fluid.getName());
		Smeltery.addAlloyMixing((new FluidStack(VectoriumFluid.fluid, 144)), 
				new FluidStack(TinkerSmeltery.moltenManyullynFluid, 144),
				new FluidStack(FluidRegistry.getFluid("black.steel.molten"), 288),
				new FluidStack(FluidRegistry.getFluid("damascus.steel.molten"), 576));
		
		
		
		ItemStack cast = new ItemStack(TinkerSmeltery.metalPattern);
		TConstructRegistry.getTableCasting().addCastingRecipe(new ItemStack(Vectorium.ingot),
				new FluidStack(VectoriumFluid.fluid, 144), cast, false, 30);


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
	
	public static class VectoriumFluid extends BlockFluidClassic {

		public static final String still 
			= AgeOfTitans.MODID + ":vectorium_still";
		
		public static final String moving 
		= AgeOfTitans.MODID + ":vectorium_flow";
		
		public static IIcon stillIcon, flowingIcon;
		
		public static Fluid fluid;
		
		public static VectoriumBucket bucket;
		
		public static VectoriumFluid block;
		
		public static String blockName = "vectoriumFluidBlock";
		
		public static void preinit() {
			fluid = new Fluid("VectoriumFluid");
			fluid.setGaseous(false);
			fluid.setLuminosity(4);
			
			FluidRegistry.registerFluid(fluid);
			block = new VectoriumFluid();
			block.setBlockName(blockName);
			GameRegistry.registerBlock(block, blockName);
			
			fluid.setBlock(block);
			
			bucket = new VectoriumBucket(block);
			GameRegistry.registerItem(bucket, VectoriumBucket.unlocalizedName);
			FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(bucket), new ItemStack(Items.bucket));
		}
		
		public VectoriumFluid() {
			super(fluid, net.minecraft.block.material.Material.lava);
			this.setCreativeTab(AgeOfTitans.creativeTab);
		}
		
		@Override
		public void onEntityCollidedWithBlock( World world, int x, int y, int z, Entity entity ) {
			if (entity instanceof EntityLivingBase) {
				((EntityLivingBase)entity).setFire(30);
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
	
	public static class VectoriumIngot extends Item {
		
		public VectoriumIngot(String unlocalizedName) {
			super();
	        this.setUnlocalizedName(unlocalizedName);
	        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
	        this.maxStackSize = 64;
	        this.setMaxDamage(0);
	        this.setCreativeTab(AgeOfTitans.creativeTab);
		}
		
	}
	
	public static class VectoriumBucket extends ItemBucket {
		
		public static final String unlocalizedName = "vectorium_bucket";

		public VectoriumBucket(Block p_i45331_1_) {
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
