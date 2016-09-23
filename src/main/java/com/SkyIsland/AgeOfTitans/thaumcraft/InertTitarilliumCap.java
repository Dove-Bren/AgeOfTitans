package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.items.Titarillium;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;

public class InertTitarilliumCap extends Item {

	public static InertTitarilliumCap cap;
	public static IArcaneRecipe recipe;
	
	public InertTitarilliumCap(String unlocalizedName) {
		super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
        this.maxStackSize = 64;
        this.setMaxDamage(0);
        this.setCreativeTab(AgeOfTitans.magicTab);
        
        cap = this;
        GameRegistry.registerItem(this, unlocalizedName);
        
        recipe = ThaumcraftApi.addArcaneCraftingRecipe("CAP_titarillium", new ItemStack(this),
        		new AspectList().add(Aspect.ORDER, 5).add(Aspect.EARTH, 5),
        		"xxx", "x x", " x ", 'x', Titarillium.nugget);
        		
//        		GameRegistry.addShapedRecipe(new ItemStack(this), "xxx", "x x", "   ",
//        		'x', new ItemStack(Titarillium.nugget));
	}
}
