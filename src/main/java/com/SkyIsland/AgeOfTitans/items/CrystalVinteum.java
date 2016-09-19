package com.SkyIsland.AgeOfTitans.items;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * Crystal vinteum item base
 * @author Skyler
 *
 */
public class CrystalVinteum extends Item {
	
	public static final String unlocalizedName = "crystal_vinteum";

	public static CrystalVinteum item;
	
	public CrystalVinteum() {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":crystal_vinteum");
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
