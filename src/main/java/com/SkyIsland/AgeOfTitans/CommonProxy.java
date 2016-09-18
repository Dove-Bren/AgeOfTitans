package com.SkyIsland.AgeOfTitans;

import com.SkyIsland.AgeOfTitans.items.CrystalVitreum;
import com.SkyIsland.AgeOfTitans.items.VitreumBlend;
import com.SkyIsland.AgeOfTitans.listeners.ItemListener;
import com.SkyIsland.AgeOfTitans.thaumcraft.ThaumcraftBridge;

public class CommonProxy  {
	
	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	  public void preInit()
	  {
		  new ThaumcraftBridge();
	  }

	  /**
	   * Do your mod setup. Build whatever data structures you care about. Register recipes,
	   * send FMLInterModComms messages to other mods.
	   */
	  public void load()
	  {

	  }

	  /**
	   * Handle interaction with other mods, complete your setup based on this.
	   */
	  public void postInit() {
		  ThaumcraftBridge.postInit();
		  new ItemListener();
		  VitreumBlend.postInit();
		  CrystalVitreum.postInit();
	  }
}
