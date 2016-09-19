package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.items.CrystalVinteum;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.wands.WandCap;

public class CrystalVinteumCap extends Item {

	public static CrystalVinteumCap cap;
	public static IRecipe recipe;
	
	public static final float visRate = 0.95f;
	
	public CrystalVinteumCap(String unlocalizedName) {
		super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
        this.maxStackSize = 1;
        this.setMaxDamage(0);
        this.setCreativeTab(AgeOfTitans.magicTab);
        
        cap = this;
        GameRegistry.registerItem(this, unlocalizedName);
        
        recipe = GameRegistry.addShapedRecipe(new ItemStack(this), "xxx", "x x", "   ",
        		'x', new ItemStack(CrystalVinteum.item));
        ThaumcraftBridge.vinteumcap = new WandCap("CAP_crystal_vinteum", CrystalVinteumCap.visRate, new ItemStack(CrystalVinteumCap.cap), 5);
        ThaumcraftBridge.vinteumcap.setTexture(new ResourceLocation(AgeOfTitans.MODID + ":textures/models/cap_crystal_vinteum.png"));
	}
}
