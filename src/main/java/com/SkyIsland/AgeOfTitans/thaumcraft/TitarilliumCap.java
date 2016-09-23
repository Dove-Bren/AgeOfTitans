package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import thaumcraft.api.crafting.InfusionRecipe;

public class TitarilliumCap extends Item {

	public static TitarilliumCap cap;
	public static InfusionRecipe recipe;
	
	public static final float visRate = 0.75f;
	
	public TitarilliumCap(String unlocalizedName) {
		super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
        this.maxStackSize = 64;
        this.setMaxDamage(0);
        this.setCreativeTab(AgeOfTitans.magicTab);
        
        cap = this;
        GameRegistry.registerItem(this, unlocalizedName);

        //recipe shifted to ThaumcraftBridge
        
//        		ThaumcraftApi.addArcaneCraftingRecipe("CAP_titarillium", new ItemStack(this),
//        		new AspectList().add(Aspect.TOOL, 1).add(DarkAspects.WRATH, 1),
//        		"xxx", "x x", " x ", 'x', Titarillium.nugget);
//        		
//        		GameRegistry.addShapedRecipe(new ItemStack(this), "xxx", "x x", "   ",
//        		'x', new ItemStack(Titarillium.nugget));
	}
}
