package com.SkyIsland.AgeOfTitans.items;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * Fuel item used for the VectorSword
 * @author Skyler
 *
 */
public class CrystalVinteum extends Item {
	
	public static final String unlocalizedName = "crystal_vitreum";

	public static CrystalVinteum item;
	
	public CrystalVinteum() {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":crystal_vitreum");
        this.maxStackSize = 64;
        this.setCreativeTab(AgeOfTitans.magicTab);
        GameRegistry.registerItem(this, unlocalizedName);
        
        item = this;
    }
	
	public static final void preInit() {
		;
	}
	
	public static final void postInit() {
		;
	}

}
