package com.SkyIsland.AgeOfTitans.items;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.statuseffects.TitanHeartEffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Rare drop from titans
 * @author Skyler
 *
 */
public class TitanHeartNugget extends ItemFood {
	
	public static final String unlocalizedName = "titan_heart_nugget";
	
//	public static ToolMod modifier;

	public TitanHeartNugget() {
        super(1, 1f, false);
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(AgeOfTitans.MODID + ":titan_heart_nugget");
        this.maxStackSize = 64;
        this.setCreativeTab(AgeOfTitans.creativeTab);
    }
    
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) {
    	ItemStack ret = super.onEaten(itemstack, world, player);
    	
    	if (world.isRemote)
    		return ret;
    	
    	TitanHeartEffect.instance.register(player, 20 * 20, 0);
    	
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
