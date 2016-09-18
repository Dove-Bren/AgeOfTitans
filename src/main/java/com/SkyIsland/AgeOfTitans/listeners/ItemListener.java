package com.SkyIsland.AgeOfTitans.listeners;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;

public class ItemListener {

	public ItemListener() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onItemThrown(ItemTossEvent e) {
		;
	}
	
}
