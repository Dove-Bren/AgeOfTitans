package com.SkyIsland.AgeOfTitans.blocks;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.items.CrystalVitreum;

import am2.blocks.liquid.BlockLiquidEssence;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class SaturatedLiquidEtherium extends BlockFluidClassic {

	public static final String still 
		= AgeOfTitans.MODID + ":saturated_etherium_still";
	
	public static final String moving 
	= AgeOfTitans.MODID + ":saturated_etherium_flow";
	
	public static IIcon stillIcon, flowingIcon;
	
	public static Fluid fluid;
	
	public static SaturatedLiquidEtherium block;
	
	public static String blockName = "saturatedEtheriumFluidBlock";
	
	public static void preinit() {
		fluid = new Fluid("SaturatedEtherium");
		fluid.setGaseous(false);
		fluid.setLuminosity(6);
		
		FluidRegistry.registerFluid(fluid);
		block = new SaturatedLiquidEtherium();
		block.setBlockName(blockName);
		GameRegistry.registerBlock(block, blockName);
		
		fluid.setBlock(block);
	}
	
	public SaturatedLiquidEtherium() {
		super(fluid, net.minecraft.block.material.Material.water);
		this.setCreativeTab(AgeOfTitans.magicTab);
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
    
    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
    	//do what we do
    	EntityItem ent = new EntityItem(world, x, y + 1.4, z, new ItemStack(CrystalVitreum.item, 4));
    	world.spawnEntityInWorld(ent);
    	
    	world.setBlock(x, y, z, BlockLiquidEssence.liquidEssenceFluid.getBlock());
    	return false;
    }
	
}