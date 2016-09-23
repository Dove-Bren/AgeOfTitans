package com.SkyIsland.AgeOfTitans.items;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.blocks.SaturatedLiquidEtherium;
import com.google.common.collect.Lists;

import am2.blocks.BlocksCommonProxy;
import am2.items.ItemOre;
import am2.items.ItemsCommonProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

/**
 * Fuel item used for the VectorSword
 * @author Skyler
 *
 */
public class VinteumBlend extends Item {
	
	public static final String unlocalizedName = "vinteum_blend";

	public static VinteumBlend item;
	
	public static IRecipe recipe;
	
	public VinteumBlend() {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":vinteum_blend");
        this.maxStackSize = 64;
        this.setCreativeTab(AgeOfTitans.magicTab);
        GameRegistry.registerItem(this, unlocalizedName);
        
        item = this;
    }
	
	public static final void preInit() {
		recipe = new ShapelessRecipes(new ItemStack(item), 
				Lists.newArrayList(new ItemStack(ItemsCommonProxy.itemOre, 1, ItemOre.META_VINTEUMDUST),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemOre.META_ARCANEASH)));
	}
	
	public static final void postInit() {
		GameRegistry.addShapelessRecipe(new ItemStack(item), 
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemOre.META_VINTEUMDUST),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemOre.META_ARCANEASH));
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		System.out.println("line1");
			if (entityItem.worldObj.getBlock((int) entityItem.posX/* - 1*/, (int) entityItem.posY, (int) entityItem.posZ/* - 1*/)
					== BlocksCommonProxy.liquidEssence) {
				System.out.println("ess");
					//SaturatedLiquidEtherium.block) {
				int amount = entityItem.getEntityItem().stackSize;
				World world = entityItem.worldObj;
				
				if (amount > 1) {
					entityItem.getEntityItem().stackSize--;
				} else {
					entityItem.worldObj.removeEntity(entityItem);
				}
				
				world.setBlock((int) entityItem.posX/* - 1*/, (int) entityItem.posY, (int) entityItem.posZ/* - 1*/,
						SaturatedLiquidEtherium.block);
			}
//		}
		return false;
	}

}
