package com.SkyIsland.AgeOfTitans.items;

import java.util.List;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.mobs.FriendlyTitan;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Rare drop from titans
 * @author Skyler
 *
 */
public class TitanHeart extends ItemFood {
	
	public static final String unlocalizedName = "titan_heart";
	
//	public static ToolMod modifier;

	public TitanHeart() {
        super(1, 1f, false);
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":titan_heart");
        this.maxStackSize = 64;
        this.setCreativeTab(AgeOfTitans.creativeTab);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean bool) {
        list.add("The still heart of a titan. As massive as it is");
        list.add("disgusting. The stench is monsterous, but appealing...");
    }
    
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) {
    	ItemStack ret = super.onEaten(itemstack, world, player);
    	
    	if (world.isRemote)
    		return ret;
    	
    	//call down lightning, and summon a friendly!
    	double xoffset, zoffset;
    	double yaw  = ((player.rotationYaw)  * Math.PI) / 180f;
    	xoffset = Math.cos(yaw) * 5.0;
    	zoffset = Math.sin(yaw) * 5.0;
    	
    	double x = player.posX + xoffset,
    		   y = (int) player.posY,
    		   z = player.posZ + zoffset;
    	
    	//summon lightning
    	EntityLightningBolt lightning = new EntityLightningBolt(world, x, y, z);
		world.addWeatherEffect(lightning);
		
		FriendlyTitan titan = new FriendlyTitan(world);
		titan.setPosition(x, y, z);
		titan.setTimer(20 * 300); //300 seconds, of 5 minutes :D
		titan.setDieOnTimer();
		world.spawnEntityInWorld(titan);
    	
    	return ret;
    }
    
    public void postInit() {
//    	modifier = new ToolMod("Hearts", 50, new ItemStack[] {new ItemStack(AgeOfTitans.titanHeart)},
//    			new int[]{1});
//
//        ModifyBuilder.registerModifier(modifier);
        
        /*
    	 * TinkerTools.modLapis = new ModLapis(10, new ItemStack[] { lapisItem, lapisBlock }, new int[] { 1, 9 });
        ModifyBuilder.registerModifier(TinkerTools.modLapis);

        TinkerTools.modAttack = new ModAttack("Quartz", 11, new ItemStack[] { new ItemStack(Items.quartz), new ItemStack(Blocks.quartz_block, 1, Short.MAX_VALUE) }, new int[] { 1, 4 });
ModifyBuilder.registerModifier(TinkerTools.modAttack);
    	 */
    }
    
//    public static class ToolMod extends ItemModTypeFilter {
//    	
//    	String tooltipName;
//        int max = 2;
//        String tagName;
//
//        public ToolMod(String type, int effect, ItemStack[] items, int[] values)
//        {
//            super(effect, "ModTitan", items, values);
//            tooltipName = "\u00a7eNape Seeker";
//            tagName = type;
//        }
//
//        @Override
//        protected boolean canModify (ItemStack tool, ItemStack[] input)
//        {
//            if (matchingAmount(input) > max)
//                return false;
//
//            NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
//            if (!tags.hasKey(key))
//                return tags.getInteger("Modifiers") > 0 && matchingAmount(input) <= max;
//
//            int keyPair[] = tags.getIntArray(key);
//            if (keyPair[0] + matchingAmount(input) <= keyPair[1])
//                return true;
//
//            else if (keyPair[0] == keyPair[1])
//                return tags.getInteger("Modifiers") > 0;
//
//            else
//                return false;
//        }
//
//        @Override
//        public void modify (ItemStack[] input, ItemStack tool)
//        {
//            NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
//            int increase = matchingAmount(input);
//            if (tags.hasKey(key))
//            {
//                int[] keyPair = tags.getIntArray(key);
//
//                if (keyPair[0] % max == 0)
//                {
//                    keyPair[0] += increase;
//                    keyPair[1] += max;
//                    tags.setIntArray(key, keyPair);
//
//                    int modifiers = tags.getInteger("Modifiers");
//                    modifiers -= 1;
//                    tags.setInteger("Modifiers", modifiers);
//                }
//                else
//                {
//                    keyPair[0] += increase;
//                    tags.setIntArray(key, keyPair);
//                }
//                updateModTag(tool, keyPair);
//
//            }
//            else
//            {
//                int modifiers = tags.getInteger("Modifiers");
//                modifiers -= 1;
//                tags.setInteger("Modifiers", modifiers);
//                String modName = "\u00a7e" + tagName + " (" + increase + "/" + max + ")";
//                int tooltipIndex = addToolTip(tool, tooltipName, modName);
//                int[] keyPair = new int[] { increase, max, tooltipIndex };
//                tags.setIntArray(key, keyPair);
//            }
//        }
//
//        void updateModTag (ItemStack tool, int[] keys)
//        {
//            NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
//            String tip = "ModifierTip" + keys[2];
//            String modName = "\u00a7e" + tagName + " (" + keys[0] + "/" + keys[1] + ")";
//            tags.setString(tip, modName);
//        }
//        
//    }
    
    
}
