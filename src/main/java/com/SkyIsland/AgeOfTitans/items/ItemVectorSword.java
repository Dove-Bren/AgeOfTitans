package com.SkyIsland.AgeOfTitans.items;

import java.util.List;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.blocks.HeartBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 *
 * @author Jacob Norman
 * @author Skyler Manzanares
 */
public class ItemVectorSword extends ItemSword{
	
	private static final String NBT_USES = "sword_uses";
	
	public static final int durability = 400;
	
	public static final int maxUses = 200;

    public ItemVectorSword(String unlocalizedName, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":" + unlocalizedName);
        this.setCreativeTab(AgeOfTitans.creativeTab);
        this.maxStackSize = 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean bool) {
        list.add("A technologically advanced sword that allows");
        list.add("its user to zip around the world as if");
        list.add("they were flying. Requires CR2 as fuel.");
    }

    @Override
    public boolean onItemUse(ItemStack tool,
			EntityPlayer player, World world, int x, int y,
			int z, int par7, float xFloat, float yFloat, float zFloat) {
    	if (!world.isRemote)
    	if (world.getBlock(x, y, z) == HeartBlock.block)
    	if (player.isSneaking()) {
    		world.setBlockToAir(x, y, z);
    		HeartBlock.block.dropBlockAsItem(world, x, y, z, 0, 0);
        	return true;
    	}
    	
    	return false;
    	
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    	if (player.isSneaking()) {
    		return stack;
    	}
    	
    	int uses = getRemainingUses(stack);
    	
    	if (uses <= 0) {
    		if (player.inventory.consumeInventoryItem(AgeOfTitans.cr2)) {
    			//setRemainingUses(stack, maxUses);
    			uses = maxUses;
    		} else {
    			//couldn't refil, don't fling
    			player.playSound(AgeOfTitans.MODID + ":item.vector_sword.fail", 1.0f, .7f + (float) (AgeOfTitans.random.nextDouble() * .3));
    			return stack;
    		}
    	}
    	
    	//stack.setItemDamage(stack.getItemDamage() + 1);
    	uses--;
    	setRemainingUses(stack, uses);
    	
        if (world.isRemote) {
            Vec3 vec3 = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
            Vec3 vec3a = player.getLook(1.0F);
	        Vec3 vec3b = vec3.addVector(vec3a.xCoord * 32, vec3a.yCoord * 32, vec3a.zCoord * 32);
	
	        MovingObjectPosition mop = world.rayTraceBlocks(vec3, vec3b);
	        
	        if (mop != null && (mop.typeOfHit == MovingObjectType.BLOCK
	        		|| mop.typeOfHit == MovingObjectType.ENTITY)) {
		        Vec3 vec = player.getLookVec();
		        double wantedH = 0.5D;
		        double wantedY = 0.7D;
		        
		        player.addVelocity(vec.xCoord * wantedH, vec.yCoord * wantedY, vec.zCoord * wantedH);
		        player.velocityChanged = true;
		        
	        }
	        return stack;
        } else {
        
	        //Vec3 vec3 = player.getPosition(0F);
	        
	        
	        return stack;
        }
    }
    
    public static int getRemainingUses(ItemStack sword) {
    	if (!sword.hasTagCompound())
    		sword.setTagCompound(new NBTTagCompound());
    	
    	NBTTagCompound nbt = sword.getTagCompound();
    	return nbt.getInteger(NBT_USES);
    }
    
    public static void setRemainingUses(ItemStack sword, int uses) {
    	if (!sword.hasTagCompound())
    		sword.setTagCompound(new NBTTagCompound());
    	
    	NBTTagCompound nbt = sword.getTagCompound();
    	nbt.setInteger(NBT_USES, uses);
    }
}
