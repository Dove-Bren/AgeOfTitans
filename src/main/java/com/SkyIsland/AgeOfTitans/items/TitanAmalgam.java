package com.SkyIsland.AgeOfTitans.items;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.utils.TinkersConstructUtils;

import am2.items.ItemOre;
import am2.items.ItemsCommonProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import tconstruct.library.TConstructRegistry;
import tconstruct.smeltery.TinkerSmeltery;

public class TitanAmalgam {
	
	public static final int id = 5003;

	public static final Item.ToolMaterial Material = EnumHelper.addToolMaterial("titanAmalgam", 11, 300, 20.0F, 4.0F, 10);
	
	public static final AmalgamIngot ingot = new AmalgamIngot("amalgam_ingot");
	
	public static void preInit() {
	
		
		GameRegistry.registerItem(ingot, "amalgam_ingot");
		AmalgamFluid.preinit();
		
		TinkersConstructUtils.addMaterial(id, "titan_amalgum", "Titan Amalgam", 300, 6, 2, 4, 1.05f, 3.0f, 12, 1.4f, 0.7f, EnumChatFormatting.AQUA.toString(), 
				//255 << 24 | 206 << 16 | 34 << 8 | 40);
				0x74DEA0);
		
		TinkersConstructUtils.addMaterialItem(id, new ItemStack(ingot), 1);
		TinkersConstructUtils.addPartBuilderMaterial(id, new ItemStack(ingot), 1);
		TinkersConstructUtils.addPartCastinMaterial(AmalgamFluid.fluid.getName(), id);
		TinkersConstructUtils.addMeltingItem(AmalgamFluid.fluid.getName(), new ItemStack(ingot), new ItemStack(Blocks.iron_block), 144, 800);
		
	}
	
	public static void registerAlloys() {
//		Smeltery.addAlloyMixing((new FluidStack(VectoriumFluid.fluid, 144)), 
//				new FluidStack(TinkerSmeltery.moltenManyullynFluid, 144),
//				new FluidStack(FluidRegistry.getFluid("black.steel.molten"), 288),
//				new FluidStack(FluidRegistry.getFluid("damascus.steel.molten"), 576));
		//   No alloy for Amalgam
		
		
		ItemStack cast = new ItemStack(TinkerSmeltery.metalPattern);
		TConstructRegistry.getTableCasting().addCastingRecipe(new ItemStack(TitanAmalgam.ingot),
				new FluidStack(AmalgamFluid.fluid, 144), cast, false, 25);

		GameRegistry.addShapelessRecipe(new ItemStack(ingot), 
				AgeOfTitans.titanHeart, new ItemStack(ItemsCommonProxy.itemOre, 1, ItemOre.META_MOONSTONE),
				new ItemStack(OreDictionary.getOres("ingotAstralSilver").get(0).getItem(), 0, OreDictionary.WILDCARD_VALUE)
				);
		
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
	
	public static class AmalgamFluid extends BlockFluidClassic {

		public static final String still 
			= AgeOfTitans.MODID + ":amalgam_still";
		
		public static final String moving 
		= AgeOfTitans.MODID + ":amalgam_flow";
		
		public static IIcon stillIcon, flowingIcon;
		
		public static Fluid fluid;
		
		public static AmalgamFluid block;
		
		public static AmalgamBucket bucket;
		
		public static String blockName = "amalgamFluidBlock";
		
		public static void preinit() {
			fluid = new Fluid("AmalgamFluid");
			fluid.setGaseous(false);
			fluid.setLuminosity(4);
			
			FluidRegistry.registerFluid(fluid);
			block = new AmalgamFluid();
			block.setBlockName(blockName);
			GameRegistry.registerBlock(block, blockName);
			
			fluid.setBlock(block);
			
			bucket = new AmalgamBucket(block);
			GameRegistry.registerItem(bucket, AmalgamBucket.unlocalizedName);
			FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(bucket), new ItemStack(Items.bucket));
		}
		
		public AmalgamFluid() {
			super(fluid, net.minecraft.block.material.Material.lava);
			this.setCreativeTab(AgeOfTitans.creativeTab);
		}
		
//		@Override
//		public void onEntityCollidedWithBlock( World world, int x, int y, int z, Entity entity ) {
////			if (entity instanceof EntityLivingBase) {
////				//((EntityLivingBase)entity).setFire(30);
////			}
//			
//		}
		
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
	
	public static class AmalgamIngot extends Item {
		
		public AmalgamIngot(String unlocalizedName) {
			super();
	        this.setUnlocalizedName(unlocalizedName);
	        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
	        this.maxStackSize = 64;
	        this.setMaxDamage(0);
	        this.setCreativeTab(AgeOfTitans.creativeTab);
		}
		
		@Override
		public void onUpdate(ItemStack stack, World world, Entity entity, int partial_ticks, boolean p_77663_5_) {
			if (entity instanceof EntityLiving) {
				((EntityLiving) entity).addPotionEffect(new PotionEffect(Potion.poison.id, 100, 0, false));
			} else if (entity instanceof EntityLivingBase) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, 100, 0, false));
			}
		}
		
	}
	
	public static class AmalgamBucket extends ItemBucket {
		
		public static final String unlocalizedName = "amalgam_bucket";

		public AmalgamBucket(Block p_i45331_1_) {
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
