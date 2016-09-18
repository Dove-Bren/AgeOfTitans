package com.SkyIsland.AgeOfTitans.blocks;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class HeartBlock extends Block {
	
	public static Block block;
	
	public static Material material;
	
	public static final String unlocalizedName = "heartblock";
	
	public static void preInit() {
	
		block = new HeartBlock();
        
        GameRegistry.registerBlock(block, unlocalizedName);		
		
	}
	
	public HeartBlock() {
		super(Material.ground);
		this.blockHardness = 200;
		this.blockResistance = 45;
		this.setStepSound(Block.soundTypeStone);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(AgeOfTitans.creativeTab);
        this.setBlockTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
	}
	
	
	
}
