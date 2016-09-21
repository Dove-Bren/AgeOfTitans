package com.SkyIsland.AgeOfTitans;

import com.SkyIsland.AgeOfTitans.mobs.FleshTitan;
import com.SkyIsland.AgeOfTitans.mobs.FriendlyTitan;
import com.SkyIsland.AgeOfTitans.mobs.NormalTitan;
import com.SkyIsland.AgeOfTitans.mobs.Titan;
import com.SkyIsland.AgeOfTitans.thaumcraft.EssentiaCoalescer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.model.ModelIronGolem;

public class ClientProxy extends CommonProxy {

	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	@Override
	public void preInit() {
		super.preInit();
	    // register my Items, Blocks, Entities, etc
		RenderingRegistry.registerEntityRenderingHandler(NormalTitan.class, new Titan.TitanRenderer(new ModelIronGolem(), 1.5f));
		RenderingRegistry.registerEntityRenderingHandler(FleshTitan.class, new Titan.TitanRenderer(new ModelIronGolem(), 1.5f));
		RenderingRegistry.registerEntityRenderingHandler(FriendlyTitan.class, new Titan.TitanRenderer(new ModelIronGolem(), 1.5f));
		EssentiaCoalescer.clientInit();
	}

	/**
	 * Do your mod setup. Build whatever data structures you care about. Register recipes,
	 * send FMLInterModComms messages to other mods.
	 */
	public void load() {
	  // register my Recipies
		super.load();
	}

	/**
	 * Handle interaction with other mods, complete your setup based on this.
	 */
	public void postInit() {
		super.postInit();
	}
}
