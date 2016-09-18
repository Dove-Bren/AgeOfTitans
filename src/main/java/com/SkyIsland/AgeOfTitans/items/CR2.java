package com.SkyIsland.AgeOfTitans.items;

import java.util.List;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Fuel item used for the VectorSword
 * @author Skyler
 *
 */
public class CR2 extends Item {
	
	public static final String unlocalizedName = "crr";

	public CR2() {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":box_cr2_pellets");
        this.maxStackSize = 64;
        this.setCreativeTab(AgeOfTitans.creativeTab);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, @SuppressWarnings("rawtypes") List list, boolean bool) {
        list.add("Advanced combustion fuel used for the Vector Sword.");
        list.add("Each pack of pellets supplies enough energy for " + ItemVectorSword.durability);
        list.add("uses of the Vector Sword.");
    }
}
