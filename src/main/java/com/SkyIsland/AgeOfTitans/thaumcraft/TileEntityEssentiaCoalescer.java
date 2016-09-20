package com.SkyIsland.AgeOfTitans.thaumcraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectSourceHelper;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileEntityEssentiaCoalescer extends TileThaumcraft implements IEssentiaTransport {

	private static final String NBT_DIR = "direction";

	private ForgeDirection outputDirection;
	
	private Aspect suctionType;
	
	private int suction;
	
	public TileEntityEssentiaCoalescer() {
		this(ForgeDirection.UP);
	}
	
	public TileEntityEssentiaCoalescer(ForgeDirection outputDirection) {
		this.outputDirection = outputDirection;
		suctionType = null;
		suction = 0;
	}
	
	@Override
	public boolean isConnectable(ForgeDirection face) {
		return face == outputDirection;
	}

	@Override
	public boolean canInputFrom(ForgeDirection face) {
		return false;
	}

	@Override
	public boolean canOutputTo(ForgeDirection face) {
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
		if (face != outputDirection)
			return null;
		
		return suctionType;
	}

	@Override
	public int getEssentiaAmount(ForgeDirection face) {
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
	
	public ForgeDirection getOutputDirection() {
		return outputDirection;
	}

	public void setDirection(ForgeDirection orientation) {
		this.outputDirection = orientation;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger(NBT_DIR, (byte) outputDirection.ordinal());
    }
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbttagcompound)
    {
		this.outputDirection = ForgeDirection.getOrientation(
				nbttagcompound.getInteger(NBT_DIR)
				);
    }
}
