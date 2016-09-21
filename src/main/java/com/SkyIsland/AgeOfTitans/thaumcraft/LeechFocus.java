package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;

/**
 * Majority of the code adapted from Forbidden Magic's ItemFocusBlink
 * https://github.com/SpitefulFox/ForbiddenMagic/blob/master/src/main/java/fox/spiteful/forbidden/items/wands/ItemFocusBlink.java
 * @author Skyler
 *
 */
public class LeechFocus extends ItemFocusBasic {

	public static LeechFocus focus;
	
	private IIcon icon;
    private IIcon ornament;
    AspectList visCost = (new AspectList()).add(Aspect.ENTROPY, 400).add(Aspect.AIR, 200).add(Aspect.FIRE, 200);
    AspectList vampireCost = (new AspectList()).add(Aspect.ENTROPY, 500).add(Aspect.AIR, 100).add(Aspect.FIRE, 300);
    FocusUpgradeType vampire = getUpgrade(37, new ResourceLocation(AgeOfTitans.MODID + ":textures/misc/vampire.png"), AgeOfTitans.MODID + ".upgrade.vampire.name", AgeOfTitans.MODID + ".upgrade.vampire.text", (new AspectList()).add(Aspect.ENTROPY, 1).add(Aspect.FIRE, 1));

    private static final float foodPerHealth = 0.25f;
    private static final float healthPerHealth = 0.25f;
    private static final int baseDamage = 4;
    private static final int damagePerPotency = 2;
    private static final float range = 25.0f;
    
    public LeechFocus(String unlocalizedName){
        super();
        setCreativeTab(AgeOfTitans.magicTab);
        this.setUnlocalizedName(unlocalizedName);
        GameRegistry.registerItem(this, unlocalizedName);
        focus = this;
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition wut) {
//    	if (!world.isRemote) {
//    		return itemstack;
//    	}
    	
    	
    	ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
    	if(wand.consumeAllVis(itemstack, player, getVisCost(itemstack), false, false)) {
    		//have enough vis
    		
//    		MovingObjectPosition mop = BlockUtils.getTargetBlock(world, (player.prevPosX + (player.posX - player.prevPosX)),
//                    (player.prevPosY + (player.posY - player.prevPosY) + 1.62 - player.yOffset),
//                    (player.prevPosZ + (player.posZ - player.prevPosZ)),
//                    (player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw)),
//                    (player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch)), false, 128.0);
//    		Vec3 ppos = Vec3.createVectorHelper(player.posX, player.posY + 1.62f, player.posZ);
//    		Vec3 look = player.getLook(1.0F);
//    		look = Vec3.createVectorHelper(look.xCoord * range, look.yCoord * range, look.zCoord * range);
//    		
//            MovingObjectPosition mop = world.rayTraceBlocks(ppos, look);
    		//MovingObjectPosition mop = raytraceFromEyes(player, range);
    		//MovingObjectPosition mop = player.rayTrace(range, 1.0f);
    		Entity hit = EntityUtils.getPointedEntity(world, player, 0.1, range, 0.1f);
            //if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
    		if (hit != null) {
            	if (!(hit instanceof EntityLiving)) {
            		return itemstack;
            	}
            	
            	wand.consumeAllVis(itemstack, player, getVisCost(itemstack), true, false);
            	EntityLiving entity = (EntityLiving) hit;
            	
            	if (!world.isRemote) {
            		spawnEffect("instantSpell", 4, world, entity);
            		spawnEffect("heart", 4, world, player);
            	}
            	
            	player.playSound(AgeOfTitans.MODID + ":wand.leech.hit", 1.0f, 0.8f + AgeOfTitans.random.nextFloat() * .4f);
            	
            	float damage = baseDamage + (wand.getFocusPotency(itemstack) * damagePerPotency);
            	boolean result = entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
            	
            	if (result) {
            		if (this.isUpgradedWith(wand.getFocusItem(itemstack), vampire)) {
            			int foodlevel = (int) (healthPerHealth * damage);
            			player.setHealth(Math.max(Math.min(20.0f, player.getHealth() + (float) foodlevel), 0.0f));
            		} else {
            			int foodlevel = (int) (foodPerHealth * damage);
            			player.getFoodStats().setFoodLevel((int)
            				Math.max(Math.min(20.0, foodlevel + player.getFoodStats().getFoodLevel()), 0.0));
            		}
            	}
            }
    	}
        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon(AgeOfTitans.MODID + ":focus_leech");
        this.ornament = ir.registerIcon(AgeOfTitans.MODID + ":focus_leech_orn");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
        return renderPass == 1 ? this.icon : this.ornament;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getFocusColor(ItemStack item){
        return 0x650000;
    }

    @Override
    public IIcon getOrnament(ItemStack focusstack) {
        return ornament;
    }

    @Override
    public AspectList getVisCost(ItemStack item){
        return this.isUpgradedWith(item, vampire) ? vampireCost : visCost;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack item, int rank){
        switch(rank){
            case 1:
            case 2:
            	return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 3:
                return new FocusUpgradeType[] {vampire, FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 4:
            case 5:
            	return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            default:
                return null;
        }
    }

	public static FocusUpgradeType getUpgrade(int id, ResourceLocation icon, String name, String text, AspectList aspects){
        if (id >= FocusUpgradeType.types.length) {
            FocusUpgradeType[] temp = new FocusUpgradeType[id+1];
            System.arraycopy(FocusUpgradeType.types, 0, temp, 0, FocusUpgradeType.types.length);
            FocusUpgradeType.types = temp;
        }
        return new FocusUpgradeType(id, icon, name, text, aspects);
    }
	
	private static void spawnEffect(String effect, int number, World world, Entity entity) {
		//code adapted from http://jabelarminecraft.blogspot.com/p/minecraft-forge-1721710-modding-tips.html
		
		double motionX, motionY, motionZ;
		
		for (int i = 0; i < number; i++) {
			motionX = AgeOfTitans.random.nextGaussian() * 0.02D;
		    motionY = AgeOfTitans.random.nextGaussian() * 0.02D;
		    motionZ = AgeOfTitans.random.nextGaussian() * 0.02D;
		    world.spawnParticle(
		          effect, 
		          entity.posX + AgeOfTitans.random.nextFloat() * entity.width * 2.0F - entity.width, 
		          entity.posY + 0.5D + AgeOfTitans.random.nextFloat() * entity.height, 
		          entity.posZ + AgeOfTitans.random.nextFloat() * entity.width * 2.0F - entity.width, 
		          motionX, 
		          motionY, 
		          motionZ);
		}
	}
	
	public static MovingObjectPosition raytraceFromEyes(Entity entity, float length) {
		Vec3 startPos = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3 endPos = startPos.addVector(entity.getLookVec().xCoord * length, entity.getLookVec().yCoord * length, entity.getLookVec().zCoord * length);
        return entity.worldObj.rayTraceBlocks(startPos, endPos);
	}
	
}
