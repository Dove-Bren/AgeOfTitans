package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class EssentiaCoalescer extends BlockContainer {
	
	public static Block block;
	
	public static Material material;
	
	public static final String unlocalizedName = "essentia_coalescer";
	
	public static void preInit() {
	
		block = new EssentiaCoalescer();
        
        GameRegistry.registerBlock(block, unlocalizedName);		
		GameRegistry.registerTileEntity(TileEntityEssentiaCoalescer.class, "tileEssentiaCoalscer");
	}
	
	public EssentiaCoalescer() {
		super(Material.ground);
		this.blockHardness = 0.75f;
		this.blockResistance = 10;
		this.setStepSound(Block.soundTypeStone);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(AgeOfTitans.magicTab);
        this.setBlockTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
        this.minY = 0f;
        this.maxY = 0.425f;
	}
	
	public static void clientInit() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEssentiaCoalescer.class, new RendererEssentiaCoalescer());
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int meta) {
//		return new TileEntityEssentiaCoalescer(ForgeDirection.getOrientation(meta));
		return new TileEntityEssentiaCoalescer();
	}
	
//	@Override
//	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack itemstack) {
//		//point towards the player
//		int dir = BlockPistonBase.determineOrientation(world, x, y, z, placer);
//		world.setBlockMetadataWithNotify(x, y, z, dir, 3);
////		((TileEntityEssentiaCoalescer) world.getTileEntity(x, y, z)).setDirection(ForgeDirection.getOrientation(dir));
//	}
	
	@Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public int getRenderType(){
        return -1;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    	TileEntity te = world.getTileEntity(x, y, z);
    	if (te == null || !(te instanceof TileEntityEssentiaCoalescer))
    		return;
    	
    	((TileEntityEssentiaCoalescer) te).onBreak();
    }
	
}
