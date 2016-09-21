package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectSourceHelper;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileEntityEssentiaCoalescer extends TileThaumcraft implements IEssentiaTransport {

//	private static final String NBT_DIR = "direction";

	private static ForgeDirection outputDirection = ForgeDirection.DOWN;
	
	private static int COOLDOWN_MAX = 200;
	
	private Aspect suctionType;
	
	private int suction;
	
	private boolean setRight;
	
	private int partCooldown;
	
	public TileEntityEssentiaCoalescer() {
//		this(ForgeDirection.UP);
		suctionType = null;
		suction = 0;
		setRight = false;
		partCooldown = -200;

	}
	
//	/**
//	 * Updates the tile entity based on the blocks around it. Typically
//	 * done on block change nearby, when the structure needs to make sure
//	 * it's still good (or is now good);
//	 */
//	public void updateOnNearby() {
//		this.setRight = getSetup();
//	}
	
	/**
	 * Looks at setup and determines whether or not the construct is correct.
	 * This is the method that determines what the proper
	 * structure should be!
	 * @return
	 */
	private boolean getSetup() {
		if (setRight) {
			MinecraftForge.EVENT_BUS.unregister(this);
			setRight = false;
		}
		
		//simple block check. Stop rampant tile entity errors
		if (!(worldObj.getBlock(xCoord, yCoord, zCoord) instanceof EssentiaCoalescer)) {
			return false;
		}
		
		//require 4 redstone blocks at pos (+-2, +-2) (4 positions, diagonals)
		//also require glass on top of that
		for (int i = -2; i <= 2; i += 4)
		for (int j = -2; j <= 2; j += 4) {
			//do at each position
			Block b = worldObj.getBlock(xCoord + i, yCoord, zCoord + j);
			if (b == null || !OreDictionary.itemMatches(new ItemStack(Blocks.redstone_block, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(b), false)) {
				return false;
			}
			
			//also check for glass
			b = worldObj.getBlock(xCoord + i, yCoord + 1, zCoord + j);
			if (b == null || !OreDictionary.itemMatches(new ItemStack(Blocks.glass, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(b), false)) {
				return false;
			}
		}
		
        MinecraftForge.EVENT_BUS.register(this);
		return true;
	}
	
	@Override
	public void updateEntity() {
		
		//pretty jank, but..
		if (partCooldown == -200) {
			setRight = getSetup();
			partCooldown = COOLDOWN_MAX;
			return;
		}
		
		if (!worldObj.isRemote)
			return;
		
		if (!setRight)
			return;
		
		if (partCooldown <= 0) {
			for (int i = 0; i < 10; i++)
			worldObj.spawnParticle("witchMagic",
					xCoord, yCoord + 1.2, zCoord,
					AgeOfTitans.random.nextGaussian() - 0.5f, 0.5f, AgeOfTitans.random.nextGaussian() - 0.5f);
			
			partCooldown = COOLDOWN_MAX;
		} else
			partCooldown--;
		
		
	}
	
//	public TileEntityEssentiaCoalescer(ForgeDirection outputDirection) {
//		outputDirection = outputDirection;
//		suctionType = null;
//		suction = 0;
//	}
	
	@Override
	public boolean isConnectable(ForgeDirection face) {
		if (!setRight)
			return false;
		return face == outputDirection;
	}

	@Override
	public boolean canInputFrom(ForgeDirection face) {
		return false;
	}

	@Override
	public boolean canOutputTo(ForgeDirection face) {
		if (!setRight)
			return false;
		return face == outputDirection;
	}

	@Override
	public void setSuction(Aspect aspect, int amount) {
		this.suction = amount;
		this.suctionType = aspect;
	}

	@Override
	public Aspect getSuctionType(ForgeDirection face) {
		return suctionType;
	}

	@Override
	public int getSuctionAmount(ForgeDirection face) {
		return suction;
	}

	@Override
	public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
		if (!setRight)
			return 0;
		if (face != outputDirection)
			return 0;
		
		return (AspectSourceHelper.drainEssentia(this, aspect, ForgeDirection.UNKNOWN, 12)
				? 1 : 0);
	}

	@Override
	public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
		return 0;
	}

	@Override
	public Aspect getEssentiaType(ForgeDirection face) {
		if (!setRight)
			return null;
		
		if (face != outputDirection)
			return null;
		
		return suctionType;
	}

	@Override
	public int getEssentiaAmount(ForgeDirection face) {
		if (!setRight)
			return 0;
		
		if (face != outputDirection)
			return 0;
		
		if (suctionType == null)
			return 0;
		
		return (AspectSourceHelper.findEssentia(this, suctionType, ForgeDirection.UNKNOWN, 12)
				? 1 : 0);
	}

	@Override
	public int getMinimumSuction() {
		return 0;
	}

	@Override
	public boolean renderExtendedTube() {
		return false;
	}
	
//	public ForgeDirection getOutputDirection() {
//		return outputDirection;
//	}
//
//	public void setDirection(ForgeDirection orientation) {
//		outputDirection = orientation;
//	}
//
//	@Override
//	public void readCustomNBT(NBTTagCompound nbttagcompound)
//    {
//        nbttagcompound.setInteger(NBT_DIR, (byte) outputDirection.ordinal());
//    }
//	
//	@Override
//	public void writeCustomNBT(NBTTagCompound nbttagcompound)
//    {
//		outputDirection = ForgeDirection.getOrientation(
//				nbttagcompound.getInteger(NBT_DIR)
//				);
//    }
	

    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldBlockBreak(BlockEvent.BreakEvent event) {
    	
    	//monitor and make sure it was changed
    	if (event.isCanceled())
    		return;
    	
		//see if the broken block is part of what we care about!
		if (event.x == xCoord - 2 || event.x == xCoord + 2) {
			if (event.z == zCoord - 2 || event.z == zCoord + 2) {
				if (event.y == yCoord || event.y == yCoord + 1) {
					//they broke part of us!
					EntityLightningBolt lightning = new EntityLightningBolt(worldObj, xCoord, yCoord, zCoord);
					worldObj.addWeatherEffect(lightning);
					this.setRight = false;
					MinecraftForge.EVENT_BUS.unregister(this); //broken, can't be fixd. stop listening
				}
			}
		}
	}
    
    public void onBreak() {
    	MinecraftForge.EVENT_BUS.unregister(this);
    }
}
