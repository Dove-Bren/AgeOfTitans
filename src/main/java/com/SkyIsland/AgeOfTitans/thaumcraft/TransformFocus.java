package com.SkyIsland.AgeOfTitans.thaumcraft;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.mobs.FleshTitan;
import com.SkyIsland.AgeOfTitans.mobs.FriendlyTitan;
import com.SkyIsland.AgeOfTitans.mobs.Titan;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
public class TransformFocus extends ItemFocusBasic {

	public static TransformFocus focus;
	
	private IIcon icon;
    private IIcon ornament;
    private AspectList visCost = (new AspectList()).add(Aspect.ORDER, 1000).add(Aspect.EARTH, 600).add(Aspect.WATER, 600);
    private AspectList extendedCost = (new AspectList()).add(Aspect.ORDER, 1400).add(Aspect.EARTH, 800).add(Aspect.WATER, 900);
    private AspectList splitCost = (new AspectList()).add(Aspect.ENTROPY, 600).add(Aspect.ORDER, 900).add(Aspect.EARTH, 600).add(Aspect.WATER, 600);
    private AspectList fleshCost = (new AspectList()).add(Aspect.ORDER, 1000).add(Aspect.EARTH, 700).add(Aspect.WATER, 700);
    private AspectList voidCost = (new AspectList()).add(Aspect.ENTROPY, 500).add(Aspect.ORDER, 500).add(Aspect.EARTH, 500).add(Aspect.WATER, 500).add(Aspect.AIR, 500).add(Aspect.FIRE, 500);
    
    private FocusUpgradeType extended = getUpgrade(38, new ResourceLocation(AgeOfTitans.MODID + ":textures/misc/titan_extended.png"), AgeOfTitans.MODID + ".upgrade.titan_extended.name", AgeOfTitans.MODID + ".upgrade.titan_extended.text", (new AspectList()).add(Aspect.ORDER, 1).add(Aspect.WATER, 1));
    private FocusUpgradeType split = getUpgrade(39, new ResourceLocation(AgeOfTitans.MODID + ":textures/misc/titan_split.png"), AgeOfTitans.MODID + ".upgrade.titan_split.name", AgeOfTitans.MODID + ".upgrade.titan_split.text", (new AspectList()).add(Aspect.ENTROPY, 2).add(Aspect.ORDER, 1));
    private FocusUpgradeType flesh = getUpgrade(40, new ResourceLocation(AgeOfTitans.MODID + ":textures/misc/titan_flesh.png"), AgeOfTitans.MODID + ".upgrade.titan_flesh.name", AgeOfTitans.MODID + ".upgrade.titan_flesh.text", (new AspectList()).add(Aspect.ORDER, 3));
    private FocusUpgradeType voidType = getUpgrade(41, new ResourceLocation(AgeOfTitans.MODID + ":textures/misc/titan_void.png"), AgeOfTitans.MODID + ".upgrade.titan_void.name", AgeOfTitans.MODID + ".upgrade.titan_void.text", (new AspectList()).add(Aspect.ENTROPY, 3).add(Aspect.FIRE, 2).add(Aspect.AIR, 2));
    
    private static final int baseTime = 20 * 30;
    private static final int timePerPotency = 20 * 5;
    private static final int timeExtended = 20 * 60;
    private static final float splitEfficiency = 0.75f;
    
    private static final float range = 25.0f;
    
    public TransformFocus(String unlocalizedName){
        super();
        setCreativeTab(AgeOfTitans.magicTab);
        this.setUnlocalizedName(unlocalizedName);
        GameRegistry.registerItem(this, unlocalizedName);
        focus = this;
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition wut) {
    	
    	ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
    	if(wand.consumeAllVis(itemstack, player, getVisCost(itemstack), false, false)) {
    		Entity hit = EntityUtils.getPointedEntity(world, player, 0.1, range, 0.1f);
    		if (hit != null) {
            	if (!(hit instanceof Titan)) {
            		return itemstack;
            	}
            	if (hit instanceof FriendlyTitan)
            		return itemstack;
            	
            	//not a friendly titan
            	if (!this.isUpgradedWith(wand.getFocusItem(itemstack), flesh)
            			&& hit instanceof FleshTitan)
            		return itemstack; //flesh, but no flesh upgrade
            	
            	wand.consumeAllVis(itemstack, player, getVisCost(itemstack), true, false);
            	Titan entity = (Titan) hit;
            	
            	if (!world.isRemote) {
            		spawnEffect("hugeexplosion", 10, world, entity);
            	}
            	
            	player.playSound(AgeOfTitans.MODID + ":wand.titan.hit", 1.0f, 0.8f + AgeOfTitans.random.nextFloat() * .4f);
            	
            	int duration = baseTime;
            	duration += timePerPotency * wand.getFocusPotency(itemstack);
            	if (this.isUpgradedWith(wand.getFocusItem(itemstack), extended))
            		duration += timeExtended;
            	
            	boolean isVoid = this.isUpgradedWith(wand.getFocusItem(itemstack), voidType);
            	
            	boolean isSplit = isUpgradedWith(wand.getFocusItem(itemstack), split);
            	
            	transform(entity, duration, isVoid, isSplit);
            	
            }
    	}
        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon(AgeOfTitans.MODID + ":focus_titan");
        this.ornament = ir.registerIcon(AgeOfTitans.MODID + ":focus_titan_orn");
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
        return 0x52875F;
    }

    @Override
    public IIcon getOrnament(ItemStack focusstack) {
        return ornament;
    }

    @Override
    public AspectList getVisCost(ItemStack item){
    	if (this.isUpgradedWith(item, extended))
    		return extendedCost;
    	if (this.isUpgradedWith(item, split))
    		return splitCost;
    	if (this.isUpgradedWith(item, flesh))
    		return fleshCost;
    	if (this.isUpgradedWith(item, voidType))
    		return voidCost;
    	
    	return visCost;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack item, int rank){
        switch(rank){
            case 1:
            case 2:
            	return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 3:
                return new FocusUpgradeType[] {extended, flesh, voidType, split, FocusUpgradeType.frugal};
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
	
	private static void transform(Titan inputTitan, int duration, boolean isVoided, boolean isSplit) {
		if (inputTitan.worldObj.isRemote)
			return;
		
		FriendlyTitan titan = new FriendlyTitan(inputTitan.worldObj);
		float health;
		titan.setPosition(inputTitan.posX, inputTitan.posY, inputTitan.posZ);
		titan.rotationYaw = inputTitan.rotationYaw;
		titan.rotationYawHead = inputTitan.rotationYawHead;
		titan.rotationPitch = inputTitan.rotationPitch;
		health = inputTitan.getHealth();
		if (isSplit)
			health *= splitEfficiency;
		titan.setHealth(Math.min(titan.getMaxHealth(),
				health));
		
		titan.setTimer(duration);
		if (isVoided)
			titan.setDieOnTimer();

		inputTitan.worldObj.spawnEntityInWorld(titan);
		
		if (isSplit) {
			//do it again, pretty much
			titan = new FriendlyTitan(inputTitan.worldObj);
			titan.setPosition(inputTitan.posX, inputTitan.posY, inputTitan.posZ);
			titan.rotationYaw = inputTitan.rotationYaw;
			titan.rotationYawHead = inputTitan.rotationYawHead;
			titan.rotationPitch = inputTitan.rotationPitch;
//			health = inputTitan.getHealth();
//				health *= splitEfficiency; //same as before
			titan.setHealth(Math.min(titan.getMaxHealth(),
					health));
			
			titan.setTimer(duration);
//			if (isVoided)
//				titan.setDieOnTimer();

			inputTitan.worldObj.spawnEntityInWorld(titan);
		}
		
		inputTitan.worldObj.removeEntity(inputTitan);
	}
	
}
