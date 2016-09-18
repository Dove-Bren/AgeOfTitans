package com.SkyIsland.AgeOfTitans.mobs.tasks;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.mobs.Titan;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class DestroyWallTask extends EntityAIBase {

	private Titan titan;
	
	private static final int blockHRadius = 5;
	
	private static final int blockHeight = 15;
	
	private static final double stopThreshold = 0.0001;
	
	private static final float antiGravity = 0.0784000015258789f;
	
	public static final int cooldownMax = 20;
	
	/**
	 * Sets how many hardness points are needed to delay the breaking by a tick.<br />
	 * Usual hardness is around 5 for vanilla.
	 */
	public static final int hardnessPerTick = 20;
	
	private int step;
	
	private int cooldown;
	
	public DestroyWallTask(Titan titan) {
		this.titan = titan;
		cooldown = cooldownMax;
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		//check cooldown isn't still expiring
		if (cooldown > 0) {
			cooldown--;
			return false;
		}
		
		//Check to see if moving towards a target, but stuck on a wall (not moving?)
		boolean ret = isStuck(); 
		return ret;
	}
	
	@Override
	public void startExecuting() {
		
		int x,y,z;
		int maxy = (int) (titan.posY) + blockHeight,
			maxx = ((int) titan.posX) + blockHRadius,
			maxz = ((int) titan.posZ) + blockHRadius;
			
		//World world = titan.worldObj;

		x = ((int) titan.posX) - blockHRadius;
		y = (int) (titan.posY);
		z = ((int) titan.posZ) - blockHRadius;
			
		
		this.step = -(getSumHardness(titan.worldObj, x, y, z, maxx, maxy, maxz) / hardnessPerTick);
		cooldown = cooldownMax;
		titan.swingProgress = 0f;
		titan.isSwingInProgress = true;
		
		titan.playSound(AgeOfTitans.MODID + ":mob.titan.angry", 1.0f, 1.0f);
	}
	
	@Override
	public boolean continueExecuting() {
		if (!isStuck()) {
			step = 0;
			System.out.println("Cancelling cooldown");
			return false;
		}
		
		if (step % 30 == 0) {
			titan.playSound(AgeOfTitans.MODID + ":misc.crumble", 1f, 1f);
		} else if (step % 30 == 15) {
			titan.worldObj.playAuxSFX(1010, (int) titan.posX, (int) titan.posY, (int) titan.posZ, 0);
		}
		
		
		if (step < 5) {
			step++;
			return true;
		}
		
		clearBlocks();
		return false;
		
	}
	
	protected void updateBlockBreak(World world, int x, int y, int z, int w, int h, int l, int progress) {
		int i, j, k;
		
		for (i = x; i < w + 1; i++)
		for (j = y; j < h + 1; j++)
		for (k = z; k < l; k++) {
			if(world.getBlock(i, j, k) != Blocks.bedrock){
				world.destroyBlockInWorldPartially(titan.getEntityId(), i, j, k, progress);
			}
		}
	}
			
	
	protected void clearBlocks() {
		titan.playLivingSound();
		titan.playSound(AgeOfTitans.MODID + ":mob.titan.angry", 1.0f, 1.0f);
		int x, y, z;
		y = (int) (titan.posY);
		x = ((int) titan.posX) - blockHRadius;
		z = ((int) titan.posZ) - blockHRadius;
		int maxy = (int) (titan.posY) + blockHeight,
			maxx = ((int) titan.posX) + blockHRadius,
			maxz = ((int) titan.posZ) + blockHRadius;
		
		World world = titan.worldObj;

		for (x = ((int) titan.posX) - blockHRadius; x < maxx + 1; x++)
		for (y = (int) (titan.posY); y < maxy + 1; y++)
		for (z = ((int) titan.posZ) - blockHRadius; z < maxz; z++) {
			if(world.getBlock(x, y, z) != Blocks.bedrock){
				world.getBlock(x, y, z).dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			}
			
		}
	}
	
	protected boolean isStuck() {
		if (titan.getAttackTarget() != null) {
			if (Math.abs(titan.motionX) < stopThreshold && Math.abs(titan.motionY + antiGravity) < stopThreshold && Math.abs(titan.motionZ) < stopThreshold) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Searches through all blocks at the given position and sums their hardness level.<br />
	 * This is intended to be used to get the time required to do a {@link #clearBlocks()} call
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @param l
	 * @param h
	 * @return
	 */
	protected int getSumHardness(World world, int x, int y, int z, int w, int h, int l) {
		int sum = 0;
		
		int i, j, k;
		
		for (i = x; i < w + 1; i++)
		for (j = y; j < h + 1; j++)
		for (k = z; k < l; k++) {
			if(world.getBlock(i, j, k) != Blocks.bedrock){
				sum += world.getBlock(i,j,k).getBlockHardness(world, i, j, k);
			}
			
		}
		
		return sum;
	}
	
	

}
