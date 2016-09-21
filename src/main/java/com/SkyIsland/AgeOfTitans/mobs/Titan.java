package com.SkyIsland.AgeOfTitans.mobs;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class Titan extends EntityIronGolem implements IEntityMultiPart {

	public static final Random random = new Random();
	
	public static final double destroyerChance = 0.20;
	
	protected TitanPart head;
	
	protected TitanPart neck;
	
	protected TitanPart torso;
	
	protected TitanPart legs;
	
	protected float headOffset, neckOffset, torsoOffset, legsOffset;
	
	/**
	 * Whether this titan is one that will destroy buildings and blocks while chasing a player
	 */
	protected boolean isDestroyer;
	
	protected ResourceLocation texture;
	
	protected float width;
	
	protected float height;

	protected int attackTimer;
	
	protected int attackCooldown = 40;
	
	public Titan(World world) {
		super(world);
		isDestroyer = false;
		width = 2.0f;
		height = 12.0f;
		this.jumpMovementFactor = 0.06f;
		this.getNavigator().setAvoidsWater(false);
		this.getNavigator().setCanSwim(true);
		
		this.setSize(width/2, height);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(60.0);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.00);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(130);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.275D + (rand.nextDouble() * .02));
		this.setHealth(this.getMaxHealth());
		setupAI();
    	
		switch (Titan.random.nextInt(3)) {
		case 0:
			texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan2.png");
			break;
		case 1:
			texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan1.png");
			break;
		case 2:
			texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/entity/titan/titan3.png");
			break;
		}
		
		if (this.isDead) {
			AgeOfTitans.logger.info("Cleaning up dead titan");
			this.setDead();
			return;
		}
		
		this.legs = new TitanPart(this, "legs", 0.4f, width, 4f); //4
		this.torso = new TitanPart(this, "torso", 0.6f, width, 4f); //4
		this.neck = new TitanPart(this, "neck", 1.6f, width, 1f); //1
		this.head = new TitanPart(this, "head", .8f, width, 3f); //3
		
		legsOffset = 0;
		torsoOffset = 4f;
		neckOffset=8f;
		headOffset=9f;
	}
	
	@Override
	public void onLivingUpdate() {
		if (attackTimer > 0)
			attackTimer--;
		
		//keep body parts updated
		super.onLivingUpdate();
		double x = this.posX,
				y = this.posY,
				z = this.posZ;
		float pitch = 0.0F,
				yaw = 0.0F; //convenience
		
		legs.onUpdate();
		legs.setLocationAndAngles(x,y + legsOffset,z,yaw,pitch);
		torso.onUpdate();
		torso.setLocationAndAngles(x,y + torsoOffset,z,yaw,pitch);
		neck.onUpdate();
		neck.setLocationAndAngles(x, y + neckOffset, z, yaw, pitch);
		head.onUpdate();
		head.setLocationAndAngles(x, y + headOffset, z, yaw, pitch);
	}
	
	@Override
    public boolean attackEntityAsMob(Entity entityTarget)
    {
		if (attackTimer > 0) {
			return false;
		}
		
        this.attackTimer = attackCooldown;
        this.worldObj.setEntityState(this, (byte)4);
        boolean flag = entityTarget.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(15));
        
        entityTarget.addVelocity((double)(-MathHelper.sin(rotationYaw * 
                (float)Math.PI / 180.0F) * 0.5F), 
                 0.1D, (double)(MathHelper.cos(rotationYaw * 
                (float)Math.PI / 180.0F) * 0.5F));
          motionX *= 0.9D;
          motionZ *= 0.9D;
        
        this.playSound(AgeOfTitans.MODID + ":mob.titan.attack", 1.0F, 1.0F);
        return flag;
    }
	
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		legs.setDead();
		torso.setDead();
		neck.setDead();
		head.setDead();
//		legs.attackEntityFrom(DamageSource.outOfWorld, 9999f);
//		torso.attackEntityFrom(DamageSource.outOfWorld, 9999f);
//		neck.attackEntityFrom(DamageSource.outOfWorld, 9999f);
//		head.attackEntityFrom(DamageSource.outOfWorld, 9999f);
	}
	
	@Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
		//allow hole for friendly titans
		if (source.damageType.equals("mob") &&
				source.getEntity() != null && source.getEntity() instanceof Titan) {
			return takeDamage(source, damage);
		}
		
        return false;
    }
	
	/**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }
	
	@Override
    protected String getLivingSound()
    {
        return AgeOfTitans.MODID + ":mob.titan.idle";
    }

    @Override
    protected String getHurtSound()
    {
        return AgeOfTitans.MODID + ":mob.titan.hurt";
    }

    @Override
    protected String getDeathSound()
    {
        return AgeOfTitans.MODID + ":mob.titan.death";
    }
	
	@Override
	public boolean canDespawn() {
		return true;
	}
	
	@Override
	public boolean isAIEnabled() {
		return true;
	}
	
	public boolean isDestroyer() {
		return this.isDestroyer;
	}
	
//	@Override
//	public void onUpdate() {
//		//if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
//        if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
//		{
//			System.out.println("Killin non-peaceful mob");
//            this.setDead();
//        }
//	}
	
	protected boolean isValidLightLevel()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int l = this.worldObj.getBlockLightValue(i, j, k);

            if (this.worldObj.isThundering())
            {
                int i1 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                l = this.worldObj.getBlockLightValue(i, j, k);
                this.worldObj.skylightSubtracted = i1;
            }

            return l <= this.rand.nextInt(12);
        }
    }
	
	/**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }
	
	@Override
	public Entity[] getParts() {
		return new Entity[]{
		legs,
		torso,
		neck,
		head
		};
	}
	
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
	}

	protected void clearAITasks()
	{
	   tasks.taskEntries.clear();
	   targetTasks.taskEntries.clear();
	}
	
	public static class TitanRenderer extends RenderLiving {
		 private float secretParameter;

		    public TitanRenderer(ModelBase par1ModelBase, float par2)
		    {
		        super(par1ModelBase, par2);
		        this.secretParameter = par2;
		        
		        ModelIronGolem m = (ModelIronGolem) this.mainModel;
		    	
		    	
		    	m.ironGolemRightArm = (new ModelRenderer(m)).setTextureSize(128, 128);
		        m.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		        m.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -1.0F, -3.0F, 4, 15, 6, secretParameter);
		        
		        m.ironGolemLeftArm = (new ModelRenderer(m)).setTextureSize(128, 128);
		        m.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		        m.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -1.0F, -3.0F, 4, 15, 6, secretParameter);
		        
		    }
		    
		    @Override
		    protected void preRenderCallback(EntityLivingBase entity, float f)
		    {
		    			    	
		        preRenderCallbackTitan((Titan) entity, f);

		    }
		  
		    protected void preRenderCallbackTitan(Titan entity, float f)

		    {
		        // some people do some G11 transformations or blends here, like you can do

		        // GL11.glScalef(2F, 2F, 2F); to scale up the entity

		        // which is used for Slime entities.  I suggest having the entity cast to

		        // your custom type to make it easier to access fields from your 

		        // custom entity, eg. GL11.glScalef(entity.scaleFactor, entity.scaleFactor, 

		        // entity.scaleFactor); 
		    	
		    	
		        
		    	//GL11.glTranslatef(0, 3.0f, 0f);
		    	final float basey = 2.9f;
		    	final float basex = 1.4f;
		    	GL11.glScalef(entity.width / basex, entity.height / basey, entity.width / basex);

		    }
		    
		    @Override
		    public void doRender(Entity entity, double x, double y, double z, float pitch, float yaw) {
		    	super.doRender(entity, x, y, z, pitch, yaw);
		    	if (!(entity instanceof Titan)) {
		    		return; //how did they get here?
		    	}
		    	Titan titan = (Titan) entity;
		    	if (RenderManager.debugBoundingBox && !entity.isInvisible()) {
		    		for (TitanPart part : new TitanPart[]{titan.legs, titan.torso, titan.neck, titan.head}) {
		    			renderDebugBoundingBox(part, part.posX, part.posY, part.posZ, yaw, 0f);
		    		}
		    	}
		    }
		    
//		    private void renderDebugBoundingBox(Entity entityIn, double p_85094_2_, double p_85094_4_, double p_85094_6_, float p_85094_8_, float p_85094_9_)
//		    {
//		    	GL11.glDepthMask(false);
//		        GL11.glDisable(GL11.GL_TEXTURE_2D);
//		        GL11.glDisable(GL11.GL_LIGHTING);
//		        GL11.glDisable(GL11.GL_CULL_FACE);
//		        GL11.glDisable(GL11.GL_BLEND);
//		        float f2 = entityIn.width / 2.0F;
//		        //AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(entityIn.posX - f2, entityIn.posY, entityIn.posZ - f2, entityIn.posX + f2, entityIn.posY + entityIn.height, entityIn.posZ + f2);
//		        //AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(axisalignedbb.minX + p_85094_2_, axisalignedbb.minY + p_85094_4_, axisalignedbb.minZ + p_85094_6_, axisalignedbb.maxX + p_85094_2_, axisalignedbb.maxY + p_85094_4_, axisalignedbb.maxZ + p_85094_6_);
//		        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(p_85094_2_ - (double)f2, p_85094_4_, p_85094_6_ - (double)f2, p_85094_2_ + (double)f2, p_85094_4_ + (double)entityIn.height, p_85094_6_ + (double)f2);
//
//		        RenderGlobal.drawOutlinedBoundingBox(axisalignedbb, 16777215);
//		        GL11.glEnable(GL11.GL_TEXTURE_2D);
//		        GL11.glEnable(GL11.GL_LIGHTING);
//		        GL11.glEnable(GL11.GL_CULL_FACE);
//		        GL11.glDisable(GL11.GL_BLEND);
//		        GL11.glDepthMask(true);
//		    }
		    
		    private void renderDebugBoundingBox(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
		    {
		    	
		    	
		        //GL11.glDepthMask(false);
		        GL11.glDisable(GL11.GL_TEXTURE_2D);
		        GL11.glDisable(GL11.GL_LIGHTING);
		        GL11.glDisable(GL11.GL_CULL_FACE);
		        GL11.glDisable(GL11.GL_BLEND);
		        GL11.glPushMatrix();
		        Tessellator var10 = Tessellator.instance;
		        var10.startDrawingQuads();
		        var10.setColorRGBA(255, 255, 255, 32);
		       
		       
		        Entity[] parts = par1Entity.getParts();
		        if(parts != null)
		        {
			        for(Entity part : parts)
			        {
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.minY, part.boundingBox.minZ);
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.maxY, part.boundingBox.minZ);
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.maxY, part.boundingBox.maxZ);
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.minY, part.boundingBox.maxZ);
			                 
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.minY, part.boundingBox.minZ);
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.minY, part.boundingBox.minZ);
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.maxY, part.boundingBox.minZ);
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.maxY, part.boundingBox.minZ);
			               
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.minY, part.boundingBox.minZ);
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.maxY, part.boundingBox.minZ);
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.maxY, part.boundingBox.maxZ);
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.minY, part.boundingBox.maxZ);
			                   
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.minY, part.boundingBox.maxZ);
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.minY, part.boundingBox.maxZ);
			            var10.addVertex(part.boundingBox.maxX, part.boundingBox.maxY, part.boundingBox.maxZ);
			            var10.addVertex(part.boundingBox.minX, part.boundingBox.maxY, part.boundingBox.maxZ);
			        }
		        } else {
		       
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.minY, par1Entity.boundingBox.minZ);
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.minZ);
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.maxZ);
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.minY, par1Entity.boundingBox.maxZ);
		                   
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.minY, par1Entity.boundingBox.minZ);
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.minY, par1Entity.boundingBox.minZ);
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.minZ);
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.minZ);
		                       
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.minY, par1Entity.boundingBox.minZ);
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.minZ);
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.maxZ);
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.minY, par1Entity.boundingBox.maxZ);
		                     
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.minY, par1Entity.boundingBox.maxZ);
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.minY, par1Entity.boundingBox.maxZ);
		        var10.addVertex(par1Entity.boundingBox.maxX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.maxZ);
		        var10.addVertex(par1Entity.boundingBox.minX, par1Entity.boundingBox.maxY, par1Entity.boundingBox.maxZ);
		       
		        }

		       
		        GL11.glTranslatef((float)(par2-par1Entity.posX), (float)(par4-par1Entity.posY), (float)(par6-par1Entity.posZ));
		        var10.draw();
		        GL11.glPopMatrix();
		        GL11.glEnable(GL11.GL_TEXTURE_2D);
		        GL11.glEnable(GL11.GL_LIGHTING);
		        GL11.glEnable(GL11.GL_CULL_FACE);
		        GL11.glDisable(GL11.GL_BLEND);
		        //GL11.glDepthMask(true);
		    }



		    @Override
		    protected ResourceLocation getEntityTexture(Entity par1Entity)
		    {
		        return ((Titan) par1Entity).texture;
		    }
	}

	@Override
	public World func_82194_d() {
		return this.worldObj;
	}

	@Override
	public boolean attackEntityFromPart(EntityDragonPart part, DamageSource source, float damage) {
		if (this.isEntityInvulnerable()) {
			return false;
		}
		
		if (!(part instanceof TitanPart)) {
			return false;
		}
		TitanPart tPart = (TitanPart) part;
				
		return takeDamage(source, damage * tPart.modifier);
		
	}
	
	protected boolean takeDamage(DamageSource source, float damage) {
		return super.attackEntityFrom(source, damage);
	}
	

	
	public static class TitanPart extends EntityDragonPart {

		public float modifier;
		
		/**
		 * 
		 * @param p_i1697_1_ The core Multipart entity this belongs to
		 * @param p_i1697_2_ Some lowercase identifying string
		 * @param p_i1697_3_ The width of the part
		 * @param p_i1697_4_ the height
		 */
		public TitanPart(IEntityMultiPart parent, String name, float modifier, float width, float height) {
			super(parent, name, width, height);
			
			if (parent == null) {
				this.setDead();
				return;
			}
			
			if ((parent instanceof Entity) && ((Entity) parent).isDead) {
				this.setDead();
				return;
			}
			
			this.modifier = modifier;
		}
		
		@Override
	    public boolean attackEntityFrom(DamageSource source, float damage)
	    {
	        return this.entityDragonObj.attackEntityFromPart(this, source, damage);
	    }

		public void setSize(float width, float height) {
			super.setSize(width, height);
		}
		
	}
	

}
