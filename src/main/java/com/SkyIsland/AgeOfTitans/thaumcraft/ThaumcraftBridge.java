package com.SkyIsland.AgeOfTitans.thaumcraft;

import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.blocks.HeartBlock;
import com.SkyIsland.AgeOfTitans.blocks.SaturatedLiquidEtherium;
import com.SkyIsland.AgeOfTitans.items.CrystalVitreum;
import com.SkyIsland.AgeOfTitans.items.TitanAmalgam;
import com.SkyIsland.AgeOfTitans.items.Titarillium;
import com.SkyIsland.AgeOfTitans.items.Vectorium;
import com.SkyIsland.AgeOfTitans.items.VitreumBlend;

import am2.blocks.BlocksCommonProxy;
import am2.items.ItemOre;
import am2.items.ItemsCommonProxy;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;

public class ThaumcraftBridge {
	
	private static final String RESEARCH_CATEGORY = "Titanic";
	private static final ResourceLocation RESEARCH_BACK_TEXT = new ResourceLocation(AgeOfTitans.MODID + ":textures/gui/research_background.png");
	private static final ResourceLocation RESEARCH_ICON_TEXT = new ResourceLocation(AgeOfTitans.MODID + ":textures/gui/research_icon.png");
	
	public static WandRod titanrod;
	
	public ThaumcraftBridge() {
		//set up subitems
		new TitanWandCore("titan_wand_core");
	}

	public static void postInit() {
		System.out.println("Requesting Thaumcraft Tie-In");
		  
		  registerAspects();
		  registerResearch();
		  
		  titanrod = new WandRod("titan_wand", TitanWandCore.capacity, new ItemStack(TitanWandCore.wand),
				  10, new TitanWandCore.RodUpdated(), new ResourceLocation(AgeOfTitans.MODID + ":textures/items/titan_wand_core.png"));
		  
		  registerRecipes();
	}
	
	private static final void registerAspects() {
		//register entities with Thaumcraft
//		  ThaumcraftApi.registerEntityTag(AgeOfTitans.MODID + ".Titan",
//				  (new AspectList()).add(Aspect.MAN, 8).add(DarkAspects.WRATH, 12));
//		  ThaumcraftApi.registerEntityTag(AgeOfTitans.MODID + ".FleshTitan",
//				  (new AspectList()).add(Aspect.MAN, 6).add(DarkAspects.WRATH, 15).add(Aspect.FLESH, 3));
		  
		  ThaumcraftApi.registerEntityTag(AgeOfTitans.MODID + ".TitanPart",
				  (new AspectList()).add(Aspect.MAN, 8).add(DarkAspects.WRATH, 12));
		  
		  //register items with Thaumcraft
		  ThaumcraftApi.registerObjectTag(new ItemStack(AgeOfTitans.cr2), 
				  (new AspectList()).add(Aspect.ENERGY, 4).add(Aspect.ORDER, 3).add(Aspect.MECHANISM, 3));
		  ThaumcraftApi.registerObjectTag(new ItemStack(AgeOfTitans.titanHeart), 
				  (new AspectList()).add(Aspect.FLESH, 6).add(DarkAspects.WRATH, 3));
		  ThaumcraftApi.registerObjectTag(new ItemStack(AgeOfTitans.vectorSword), 
				  (new AspectList()).add(Aspect.WEAPON, 8).add(Aspect.METAL, 4).add(Aspect.HEAL, 2));
		  ThaumcraftApi.registerObjectTag(new ItemStack(HeartBlock.block), 
				  (new AspectList()).add(Aspect.FLESH, 2).add(Aspect.ORDER, 8).add(Aspect.DARKNESS, 4));
		  ThaumcraftApi.registerObjectTag(new ItemStack(TitanAmalgam.ingot), 
				  (new AspectList()).add(Aspect.MAGIC, 6).add(Aspect.METAL, 2).add(Aspect.DARKNESS, 5));
		  ThaumcraftApi.registerObjectTag(new ItemStack(TitanAmalgam.AmalgamFluid.bucket), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.METAL, 6).add(Aspect.DARKNESS, 3));
		  ThaumcraftApi.registerObjectTag(new ItemStack(TitanAmalgam.AmalgamFluid.block), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.METAL, 2).add(Aspect.DARKNESS, 3));
		  
		  ThaumcraftApi.registerObjectTag(new ItemStack(Titarillium.ingot), 
				  (new AspectList()).add(Aspect.METAL, 10).add(Aspect.MAGIC, 8).add(Aspect.GREED, 5));
		  ThaumcraftApi.registerObjectTag(new ItemStack(Titarillium.TitarilliumFluid.bucket), 
				  (new AspectList()).add(Aspect.METAL, 12).add(Aspect.MAGIC, 6).add(Aspect.GREED, 3));
		  ThaumcraftApi.registerObjectTag(new ItemStack(Titarillium.TitarilliumFluid.block), 
				  (new AspectList()).add(Aspect.METAL, 8).add(Aspect.MAGIC, 6).add(Aspect.GREED, 5));
		  
		  ThaumcraftApi.registerObjectTag(new ItemStack(Vectorium.ingot), 
				  (new AspectList()).add(Aspect.METAL, 6).add(Aspect.MOTION, 4).add(Aspect.MECHANISM, 4));
		  ThaumcraftApi.registerObjectTag(new ItemStack(Vectorium.VectoriumFluid.bucket), 
				  (new AspectList()).add(Aspect.METAL, 8).add(Aspect.MOTION, 3).add(Aspect.MECHANISM, 2));
		  ThaumcraftApi.registerObjectTag(new ItemStack(Vectorium.VectoriumFluid.block), 
				  (new AspectList()).add(Aspect.METAL, 6).add(Aspect.MOTION, 3).add(Aspect.MECHANISM, 2));

		  ThaumcraftApi.registerObjectTag(new ItemStack(SaturatedLiquidEtherium.block), 
				  (new AspectList()).add(Aspect.MAGIC, 6).add(Aspect.WATER, 3).add(Aspect.AURA, 1));
		  ThaumcraftApi.registerObjectTag(new ItemStack(VitreumBlend.item), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.AURA, 1));
		  ThaumcraftApi.registerObjectTag(new ItemStack(CrystalVitreum.item), 
				  (new AspectList()).add(Aspect.MAGIC, 3).add(Aspect.AURA, 1).add(Aspect.CRYSTAL, 3));

		  ThaumcraftApi.registerObjectTag(new ItemStack(TitanWandCore.wand), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.LIFE, 2).add(Aspect.HEAL, 2).add(Aspect.DARKNESS, 2));
		  
	}
	
	private static final void registerRecipes() {
		InfusionRecipe recipe;
		List<ResearchPage> pages;
		ItemStack wardedGlass = ItemApi.getBlock("blockCosmeticOpaque", 2);
		ItemStack arcaneBellows = ItemApi.getBlock("blockWoodenDevice", 0);
		recipe = ThaumcraftApi.addInfusionCraftingRecipe("ROD_titan_wand", new ItemStack(TitanWandCore.wand), 150,
				new AspectList().add(Aspect.LIFE, 64).add(Aspect.HEAL, 32).add(Aspect.MAGIC, 32).add(Aspect.FLESH, 128),
				new ItemStack(AgeOfTitans.titanHeart),
				new ItemStack[]{ConfigItems.STAFF_ROD_PRIMAL.getItem(), wardedGlass, new ItemStack(Items.comparator),
						wardedGlass, arcaneBellows, wardedGlass, new ItemStack(Items.comparator), wardedGlass});
		
		pages = new LinkedList<ResearchPage>();
		pages.add(new ResearchPage("Your extensive knowledge of the titan hearts has lead to an extraordinary discovery! While the hearts themselves lie still, a little bit of magic and some machinery can make the heart beat again! Quite unexpectedly, however, the heart no longer behaves like a regular heart. Instead, the magic-imbued heart acts as an exceptional (and self-sustaining) conduit of magic. It didn't take long to"));
		pages.add(new ResearchPage("wonder what would happen when you put together the magic heart and a wand core. The results are far from disappointing. The Titan Heart Wand Core can store up to 200 vis and -- through the efforts of the again-beating titan heart -- slowly regenerates vis. This wand regenerates up to 50 vis of each type! This doesn't come for free, however, as the wand needs energy to function. As such, the"));
		pages.add(new ResearchPage("wand will find food in your bag and feed itself to regain it's vis!"));
		pages.add(new ResearchPage(recipe));
		
		Research.TITAN_ROD.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
		pages = new LinkedList<ResearchPage>();
		pages.add(new ResearchPage("You've seen vitreum do wonderful things when used with your spell crafting altar, but failed to find a practical bridge between the powerful work of crafted spells and the refined world of Thaumcraft. Through a surprisingly-small number of experiments, you have finally been able to create a material made of vitreum that seems quite receptive to vis! Crystal Vitreum, as you've called it, is a crystal material made of vitreum and soaked in Liquid Essentia."));
		pages.add(new ResearchPage("The process is actually quite mundane; first, you must assemble a compound of vitreum and arcane ash. Simply mixing the two by hand in equal parts works. Then, find a pool of Liquid Essence and throw the compound in. The Liquid Essence quickly transforms into a strange solution which you have termed 'Saturated Etherium'. Once this is done, the final step is to reduce the liquid, leaving behind only the crystal vitreum."));
		pages.add(new ResearchPage("To do this, you must light a fire directly above the saturated etherium. Once the fumes catch, the process is exceptionally quick. The crystal vitreum is produced, leaving behind most of the liquid essence."));
		pages.add(new ResearchPage(VitreumBlend.recipe));
		
		Research.CRYSTAL_VITREUM.getItem().setPages(pages.toArray(new ResearchPage[0]));
	}
	
	private static final void registerResearch() {
		ResearchCategories.registerCategory(RESEARCH_CATEGORY, RESEARCH_ICON_TEXT, RESEARCH_BACK_TEXT);
		
		for (Research r : Research.values())
			ResearchCategories.addResearch(r.getItem());
	}
	
	private static enum Research {
		TITAN_ROD("ROD_titan_wand", new AspectList().add(DarkAspects.WRATH, 1)
										.add(Aspect.FLESH, 1)
										.add(Aspect.MAN, 1),
			5, 5, 1, new ItemStack(TitanWandCore.wand), new String[]{"ROD_primal_staff"}, new ItemStack[]{new ItemStack(AgeOfTitans.titanHeart)},
			true, false),
		CRYSTAL_VITREUM("crystal_vitreum", new AspectList().add(Aspect.AURA, 4).add(Aspect.MAGIC, 2).add(Aspect.CRYSTAL, 5),
				2, 2, 1, new ItemStack(CrystalVitreum.item),
				null, new ItemStack[]{new ItemStack(ItemsCommonProxy.itemOre, 1, ItemOre.META_VINTEUMDUST), new ItemStack(BlocksCommonProxy.liquidEssence)}, true, true);
		
		private ResearchItem item;
		
		private Research(String key, AspectList aspects, int x, int y, int complexity,
				ItemStack icon, String[] parents, ItemStack[] itemScans, boolean isSpecial, boolean isSecondary) {
			item = new ResearchItem(key, RESEARCH_CATEGORY, aspects, x, y, complexity, icon);
			if (aspects == null)
				item.isAutoUnlock();
				//item.isSecondary();
			if (itemScans != null) {
				item.setItemTriggers(itemScans);
			}
			if (isSpecial)
				item.isSpecial();
			item.parentsHidden = parents;
			
			if (itemScans == null && parents == null)
				;
			else {
				item.setHidden();
			}
			if (isSecondary)
				item.setSecondary();
					
			//String key, String category, AspectList tags, int col, int row, int complex, ResourceLocation icon
		}
		
		public ResearchItem getItem() {
			return item;
		}
		
	}
	
	
	//public ResearchItem(String key, String category, AspectList tags, int col, int row, int complex, ItemStack icon)
	
}