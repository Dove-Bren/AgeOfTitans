package com.SkyIsland.AgeOfTitans.mobs;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.mobs.tasks.DestroyWallTask;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class FleshTitan extends NormalTitan implements IBossDisplayData {
	
	public static class DropHandler {
		
		public static final double heartDropRate = 0.25;
		
		@SubscribeEvent
	    public void dropHandler(LivingDropsEvent event) {			
			if (event.entityLiving instanceof Titan) {
				Titan titan = (Titan) event.entityLiving;
				event.drops.clear();
				
				ItemStack itemStackToDrop = new ItemStack(Items.redstone, Titan.random.nextInt(2) + event.lootingLevel);
		        event.drops.add(new EntityItem(titan.worldObj, titan.posX, titan.posY, titan.posZ, itemStackToDrop));
		        
		        itemStackToDrop = new ItemStack(Items.flint, Titan.random.nextInt(2));
		        event.drops.add(new EntityItem(titan.worldObj, titan.posX, titan.posY, titan.posZ, itemStackToDrop));
		        
		        itemStackToDrop = new ItemStack(Items.flint, Titan.random.nextInt(2));
		        event.drops.add(new EntityItem(titan.worldObj, titan.posX, titan.posY, titan.posZ, itemStackToDrop));
				
				 if (event.entityLiving instanceof FleshTitan) {
					 if (Titan.random.nextDouble() < (heartDropRate * event.lootingLevel))
						 itemStackToDrop = new ItemStack(AgeOfTitans.titanHeart, 1);
				        event.drops.add(new EntityItem(titan.worldObj, titan.posX, titan.posY, titan.posZ, itemStackToDrop));
				 }
			}
	    }
	}
	
	
	
	public FleshTitan(World world) {
		super(world);
		this.isDestroyer = true;
		
		width = 2.5f;
		height = 15.0f;
		
		this.legs.setSize(width, 4.5f); //4
		this.torso.setSize(width, 4.5f); //4
		this.neck.setSize(width, 2f); //1
		this.head.setSize(width, 4f); //3
		
		legsOffset = 0;
		torsoOffset = 4.5f;
		neckOffset=9f;
		headOffset=11f;
		
		this.setSize(width/2, height);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(60.0);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.00);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200);
		this.setHealth(this.getMaxHealth());
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.32 + (rand.nextDouble() * .02));
		setupAI();
		
    	texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan_flesh.png");
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (this.worldObj.isRemote) {
			BossStatus.setBossStatus(this, true);
		}
	}
	
	@Override
	protected void setupAI()
	{
		clearAITasks();
	   getNavigator().setAvoidsWater(true);
       this.getNavigator().setBreakDoors(true);
       this.tasks.addTask(0, new EntityAISwimming(this));
       this.tasks.addTask(2, new EntityAIAttackOnCollide(this, FriendlyTitan.class, 1.0D, false));
       this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
       this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
       this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
       this.tasks.addTask(8, new EntityAIWatchClosest(this, FriendlyTitan.class, 20.0F));
       this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));
       this.tasks.addTask(8, new EntityAILookIdle(this));
       this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
       this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, FriendlyTitan.class, 0, true));
       this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    	   this.tasks.addTask(1, new DestroyWallTask(this));
	}
	
	@Override
	public float getSoundVolume() {
		return 6f;
	}

}
