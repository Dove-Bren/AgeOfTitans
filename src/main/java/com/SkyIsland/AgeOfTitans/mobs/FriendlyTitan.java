package com.SkyIsland.AgeOfTitans.mobs;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FriendlyTitan extends Titan implements IBossDisplayData {
	
	protected int timer;
	
	protected boolean dieOnTimer;
	
	public FriendlyTitan(World world) {
		super(world);
		this.isDestroyer = false;
		this.timer = -1;
		dieOnTimer = false;
		
		setupAI();
		
    	//texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan_friendly.png");
		switch (Titan.random.nextInt(3)) {
		case 0:
			texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan_friendly2.png");
			break;
		case 1:
			texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan_friendly1.png");
			break;
		case 2:
			texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan_friendly3.png");
			break;
		}
	}
	
	/**
	 * Sets how long until this friendly titan turns back into a regular titan
	 * @param time
	 */
	public void setTimer(int time) {
		this.timer = time;
	}
	
	public void setDieOnTimer() {
		dieOnTimer = true;
	}
	
	@Override
	public void onLivingUpdate() {
		if (timer > -1) {
			//gonna turn back after some time
			timer--;
			if (timer == 0) {
				if (dieOnTimer) {
					if (!worldObj.isRemote)
					worldObj.removeEntity(this);
				} else
					transform();
			}
		}
		
		super.onLivingUpdate();
	}
	
	/**
	 * Takes this entity, kills it, and adds back an aggressive one
	 */
	private void transform() {
		if (worldObj.isRemote)
			return;
		
		Titan titan = new NormalTitan(worldObj);
		titan.setPosition(posX, posY, posZ);
		titan.rotationYaw = this.rotationYaw;
		titan.rotationYawHead = this.rotationYawHead;
		titan.rotationPitch = this.rotationPitch;
		titan.setHealth(this.getHealth());
		
		worldObj.removeEntity(this);
		worldObj.spawnEntityInWorld(titan);
	}
	
	@Override
	protected void setupAI()
	{
		clearAITasks();
		getNavigator().setAvoidsWater(true);
		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, NormalTitan.class, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, NormalTitan.class, 20.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, NormalTitan.class, 0, true));
	}
	
	@Override
	public float getSoundVolume() {
		return 6f;
	}
	
//	public boolean attackEntityAsMob(Entity entityTarget) {
//		if (entityTarget == null) {
//			System.out.println("null!");
//			return false;
//		}
//		
//		boolean hit = super.attackEntityAsMob(entityTarget);
//		
//		if (entityTarget instanceof Titan) {
//			//code taken and adapted from
//			//http://jabelarminecraft.blogspot.com/p/minecraft-forge-1721710-custom-entity.html
//			
//			
//			float attackDamage = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
//			    
//		    boolean isTargetHurt = entityTarget.attackEntityFrom(DamageSource
//		    		.causeMobDamage(this), attackDamage);
//
//		    if (isTargetHurt) {
//				entityTarget.addVelocity((double)(-MathHelper.sin(rotationYaw * 
//				(float)Math.PI / 180.0F) * (float) 2  * 0.5F), 
//				       0.1D, (double)(MathHelper.cos(rotationYaw * 
//				(float)Math.PI / 180.0F) * (float) 2  * 0.5F));
//				motionX *= 0.6D;
//				motionZ *= 0.6D;
//		    }
//
//		    return isTargetHurt ;
//		} else {
//			return super.attackEntityAsMob(entityTarget);
//		}
		
		
//		if (entityTarget instanceof Titan) {
//			//code taken and adapted from
//			//http://jabelarminecraft.blogspot.com/p/minecraft-forge-1721710-custom-entity.html
//			
//			
//			float attackDamage = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
//			    
//		    boolean isTargetHurt = entityTarget.attackEntityFrom(DamageSource
//		    		.causeMobDamage(this), attackDamage);
//
//		    if (isTargetHurt) {
//				entityTarget.addVelocity((double)(-MathHelper.sin(rotationYaw * 
//				(float)Math.PI / 180.0F) * (float) 2  * 0.5F), 
//				       0.1D, (double)(MathHelper.cos(rotationYaw * 
//				(float)Math.PI / 180.0F) * (float) 2  * 0.5F));
//				motionX *= 0.6D;
//				motionZ *= 0.6D;
//		    }
//
//		    return isTargetHurt ;
//		} else {
//			return super.attackEntityAsMob(entityTarget);
//		}
//	}

}
