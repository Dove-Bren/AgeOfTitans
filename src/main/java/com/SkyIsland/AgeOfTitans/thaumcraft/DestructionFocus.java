package com.SkyIsland.AgeOfTitans.thaumcraft;

import java.util.List;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;

/**
 * Majority of the code adapted from Forbidden Magic's ItemFocusBlink
 * https://github.com/SpitefulFox/ForbiddenMagic/blob/master/src/main/java/fox/spiteful/forbidden/items/wands/ItemFocusBlink.java
 * @author Skyler
 *
 */
public class DestructionFocus extends ItemFocusBasic {

	public static DestructionFocus focus;
	
	private IIcon icon;
    private IIcon ornament;
    AspectList visCost = (new AspectList()).add(Aspect.ENTROPY, 1000).add(Aspect.EARTH, 1000);
    AspectList stoneCost = (new AspectList()).add(Aspect.ENTROPY, 1000).add(Aspect.EARTH, 1000).add(Aspect.ORDER, 500);
    AspectList vacuumCost = (new AspectList()).add(Aspect.ENTROPY, 1000).add(Aspect.EARTH, 1000).add(Aspect.AIR, 500);
    FocusUpgradeType stoneseeker = getUpgrade(35, new ResourceLocation(AgeOfTitans.MODID + ":textures/misc/stoneseeker.png"), AgeOfTitans.MODID + ".upgrade.stoneseeker.name", AgeOfTitans.MODID + ".upgrade.stoneseeker.text", (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.ORDER, 5));
    FocusUpgradeType vacuum = getUpgrade(36, new ResourceLocation(AgeOfTitans.MODID + ":textures/misc/vacuum.png"),  AgeOfTitans.MODID + ".upgrade.vacuum.name", AgeOfTitans.MODID + ".upgrade.vacuum.text", (new AspectList()).add(Aspect.DARKNESS, 1));

    private static final int baseRadius = 4;
    
    public DestructionFocus(String unlocalizedName){
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
    		//have enough vis
    		int radius = baseRadius + (wand.getFocusEnlarge(itemstack));
    		boolean isStone = this.isUpgradedWith(wand.getFocusItem(itemstack), stoneseeker);
    		boolean isVacuum = this.isUpgradedWith(wand.getFocusItem(itemstack), vacuum);
    		ItemStack stoneArchtype = new ItemStack(Blocks.stone, 1, OreDictionary.WILDCARD_VALUE);
    		
    		wand.consumeAllVis(itemstack, player, getVisCost(itemstack), true, false);
    		
    		//from DestroyWallTask
    		player.playSound(AgeOfTitans.MODID + ":mob.titan.angry", 1.0f, 1.3f);
    		int x, y, z;
    		y = (int) (player.posY);
    		x = ((int) player.posX) - radius;
    		z = ((int) player.posZ) - radius;
    		int maxy = (int) (player.posY) + radius,
    			maxx = ((int) player.posX) + radius,
    			maxz = ((int) player.posZ) + radius;
    		
    		for (x = ((int) player.posX) - radius; x < maxx + 1; x++)
    		for (y = (int) (player.posY); y < maxy + 1; y++)
    		for (z = ((int) player.posZ) - radius; z < maxz; z++) {
    			Block block = world.getBlock(x, y, z);
    			if(block != Blocks.bedrock && block != Blocks.air)
    			if (player.canPlayerEdit(x, y, z, 0, new ItemStack(Items.diamond_pickaxe))) {
    				
    				//if isStone, just do stone and derivatives
    				if (isStone && !OreDictionary.itemMatches(stoneArchtype, new ItemStack(block), false))
    					continue;
    				
    				if (isVacuum) {
    					if (!player.inventory.addItemStackToInventory(new ItemStack(block))) {
    						List<ItemStack> drops = block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        					if (!drops.isEmpty())
        					for (ItemStack drop : drops) {
        						world.spawnEntityInWorld(new EntityItem(
        								world, player.posX, player.posY + 1.2, player.posZ,
        								drop
        								));
        					}
        					else {
        						ItemStack drop;
        						Item dropItem = block.getItemDropped(world.getBlockMetadata(x, y, z), AgeOfTitans.random, 0);
        						if (dropItem != null) {

        							drop = new ItemStack(dropItem);
            						world.spawnEntityInWorld(new EntityItem(
            								world, player.posX, player.posY + 1.2, player.posZ,
            								drop
            								));
        						}
        					}
    					}
    				} else {
    					block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
    				}
    				
    				world.setBlockToAir(x, y, z);
    			}
    			
    		}
    		
    	}
    	
    	
//    	MovingObjectPosition mop = BlockUtils.getTargetBlock(world, (player.prevPosX + (player.posX - player.prevPosX)),
//                (player.prevPosY + (player.posY - player.prevPosY) + 1.62 - player.yOffset),
//                (player.prevPosZ + (player.posZ - player.prevPosZ)),
//                (player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw)),
//                (player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch)), false, 128.0);
//        if(mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mop.sideHit != -1){
//            ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
//            if(wand.consumeAllVis(itemstack, player, getVisCost(itemstack), false, false)){
//                double ex = mop.hitVec.xCoord;
//                double wy = mop.hitVec.yCoord;
//                double zee = mop.hitVec.zCoord;
//                switch(mop.sideHit){
//                    case 0: wy -= 2.0;
//                        break;
//                    case 1: break;
//                    case 2: zee -= 0.5;
//                        break;
//                    case 3: zee += 0.5;
//                        break;
//                    case 4: ex -= 0.5;
//                        break;
//                    case 5: ex += 0.5;
//                        break;
//                }
//                world.playAuxSFXAtEntity((EntityPlayer) null, 1009, (int) player.posX, (int) player.posY, (int) player.posZ, 0);
//                for (int k = 0; k < 8; ++k)
//                {
//                    world.spawnParticle("smoke", player.posX + (world.rand.nextDouble() - 0.5D) * (double)player.width, player.posY + world.rand.nextDouble() * (double)player.height - 0.25D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double)player.width, 0, 0, 0);
//                }
//                double range = 3.0 + 1.5 * wand.getFocusEnlarge(itemstack);
//                if(this.isUpgradedWith(wand.getFocusItem(itemstack), hellfire)) {
//                    List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(ex - range, wy - range, zee - range, ex + range, wy + range, zee + range));
//                    for (EntityLivingBase entity : entities) {
//                        if(entity == player)
//                            continue;
//                        for (int k = 0; k < 8; ++k) {
//                            world.spawnParticle("flame", entity.posX + (world.rand.nextDouble() - 0.5D) * (double) entity.width, entity.posY + world.rand.nextDouble() * (double) entity.height - 0.25D, entity.posZ + (world.rand.nextDouble() - 0.5D) * (double) entity.width, 0, 0, 0);
//                        }
//                        int potency = wand.getFocusPotency(itemstack);
//                        entity.attackEntityFrom((new EntityDamageSource("fireball", player)).setFireDamage(), 3 + 3 * potency);
//                        entity.setFire(3 + 3 * potency);
//                    }
//                }
//                else if(this.isUpgradedWith(wand.getFocusItem(itemstack), pandemonium)){
//                    List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(ex - range, wy - range, zee - range, ex + range, wy + range, zee + range));
//                    for (EntityLivingBase entity : entities) {
//                        if(!(entity instanceof IMob))
//                            continue;
//                        for (int k = 0; k < 8; ++k) {
//                            world.spawnParticle("smoke", entity.posX + (world.rand.nextDouble() - 0.5D) * (double) entity.width, entity.posY + world.rand.nextDouble() * (double) entity.height - 0.25D, entity.posZ + (world.rand.nextDouble() - 0.5D) * (double) entity.width, 0, 0, 0);
//                        }
//                        entity.setPositionAndUpdate(player.posX, player.posY, player.posZ);
//                    }
//                }
//                player.setPositionAndUpdate(ex, wy, zee);
//                world.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)player.posX, (int)player.posY, (int)player.posZ, 0);
//                for (int k = 0; k < 8; ++k)
//                {
//                    world.spawnParticle("flame", player.posX + (world.rand.nextDouble() - 0.5D) * (double)player.width, player.posY + world.rand.nextDouble() * (double)player.height - 0.25D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double)player.width, 0, 0, 0);
//                }
//                wand.consumeAllVis(itemstack, player, getVisCost(itemstack), true, false);
//                player.swingItem();
//            }
//        }
        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon(AgeOfTitans.MODID + ":focus_destruction");
        this.ornament = ir.registerIcon(AgeOfTitans.MODID + ":focus_destruction_orn");
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
        return 0x5F2928;
    }

    @Override
    public IIcon getOrnament(ItemStack focusstack) {
        return ornament;
    }

    @Override
    public AspectList getVisCost(ItemStack item){
        return this.isUpgradedWith(item, stoneseeker) ? stoneCost : this.isUpgradedWith(item, vacuum) ? vacuumCost : visCost;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack item, int rank){
        switch(rank){
            case 1:
            case 2:
            	return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
            case 3:
                return new FocusUpgradeType[] {stoneseeker, vacuum, FocusUpgradeType.enlarge, FocusUpgradeType.frugal};
            case 4:
            case 5:
            	return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
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
	
}
