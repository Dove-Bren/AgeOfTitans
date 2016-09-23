package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TitanWandCore extends Item {

	private static final String NBT_RECHARGE = "wand_eat_delay";
	
	private static final String NBT_FULL = "wand_full";
	
	private static final int FOOD_RATE = 5 * 20;
	
	public static TitanWandCore wand;
	
	public static final int rechargeMax = 50;
	
	public static final int capacity = 200;
	
	public TitanWandCore(String unlocalizedName) {
		super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
        this.maxStackSize = 1;
        this.setMaxDamage(0);
        this.setCreativeTab(AgeOfTitans.magicTab);
        
        wand = this;
        GameRegistry.registerItem(this, unlocalizedName);
	}

	public static class RodUpdated implements IWandRodOnUpdate {

		@Override
		public void onUpdate(ItemStack itemstack, EntityPlayer player) {
			if (!itemstack.hasTagCompound())
				itemstack.setTagCompound(new NBTTagCompound());
			
			AspectList list = new AspectList().add(Aspect.AIR, rechargeMax * 100)
					.add(Aspect.WATER, rechargeMax * 100)
					.add(Aspect.EARTH, rechargeMax * 100)
					.add(Aspect.FIRE, rechargeMax * 100)
					.add(Aspect.ORDER, rechargeMax * 100)
					.add(Aspect.ENTROPY, rechargeMax * 100);
			
			NBTTagCompound nbt = itemstack.getTagCompound();
			int delay = nbt.getInteger(NBT_RECHARGE); //rets 0 if not, which works fine
				
			delay -= 1;
			
			if (ThaumcraftApiHelper.consumeVisFromWand(itemstack, player, list, false, true)) {
				//no need to recharge. write back delay, stop
				nbt.setInteger(NBT_RECHARGE, delay);
				return;
			}
			
			if (delay <= 0) {
				if (isFull(itemstack))
					setFull(itemstack, false);
			
				//returns new delay or 0, if couldn't eat. Also
				//updates fullness on success
				
				//make sure we need to recharge even
				delay = tryConsume(itemstack, player);
			}
			
			if (player.ticksExisted % 200 == 0)
			if (isFull(itemstack)) {
				ItemWandCasting wand = (ItemWandCasting) itemstack.getItem();
				for (Aspect aspect : list.getAspects()) {
					if (wand.getVis(itemstack, aspect) > rechargeMax * 100)
						continue;
					
					wand.addVis(itemstack, aspect, 1, true);
					
					//don't know how else ot ask how much vis a wand has!
					
//					if (ThaumcraftApiHelper.consumeVisFromWand(itemstack, player, new AspectList().add(aspect, rechargeMax * 100), false, true)) {
//							System.out.print("/");
//							continue; //can take out, so don't recharge
//					}
//					System.out.print(".");
//					ThaumcraftApiHelper.consumeVisFromWand(itemstack, player, new AspectList().add(aspect, -1), true, true);
				}
			}
			
			nbt.setInteger(NBT_RECHARGE, delay);
		}
		
	}
	
	protected static int tryConsume(ItemStack wand, EntityPlayer player) {
		//try to eat food from the player's inventory and
		//return new food time
		ItemStack item = null;
		int pos;
		for (pos = 0; pos < player.inventory.mainInventory.length; pos++) {
			item = player.inventory.mainInventory[pos];
			if (item != null && item.getItem() instanceof ItemFood) {
				//firstFood = item;
				break;
			}
		}
		
		if (pos >= player.inventory.mainInventory.length)
			return 0; //looked too far, didn't find anything
		
		if (item == null) {
			//something went wrong
			return 0;
		}
		
		item.stackSize--;
		if (item.stackSize <= 0) {
			player.inventory.mainInventory[pos] = null;
		}
			
			
	
		player.playSound(AgeOfTitans.MODID + ":item.titan_wand.eat", 1.0f, .8f + (float) (AgeOfTitans.random.nextDouble() * .2));
		setFull(wand, true);
		int foodLevel = ((ItemFood) item.getItem()).func_150905_g(item); //could be null even :(
		return FOOD_RATE * foodLevel;
	}

	protected static void setFull(ItemStack wand, boolean full) {
		if (!wand.hasTagCompound())
			wand.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = wand.getTagCompound();
		nbt.setBoolean(NBT_FULL, full);
	}
	
	protected static boolean isFull(ItemStack wand) {
		if (!wand.hasTagCompound())
			wand.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound nbt = wand.getTagCompound();
		return nbt.getBoolean(NBT_FULL);
	}
}
