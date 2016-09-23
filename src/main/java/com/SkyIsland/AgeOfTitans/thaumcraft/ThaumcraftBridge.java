package com.SkyIsland.AgeOfTitans.thaumcraft;

import java.util.LinkedList;
import java.util.List;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;
import com.SkyIsland.AgeOfTitans.blocks.HeartBlock;
import com.SkyIsland.AgeOfTitans.blocks.SaturatedLiquidEtherium;
import com.SkyIsland.AgeOfTitans.items.CrystalVinteum;
import com.SkyIsland.AgeOfTitans.items.TitanAmalgam;
import com.SkyIsland.AgeOfTitans.items.Titarillium;
import com.SkyIsland.AgeOfTitans.items.Vectorium;
import com.SkyIsland.AgeOfTitans.items.VinteumBlend;

import am2.blocks.BlocksCommonProxy;
import am2.items.ItemOre;
import am2.items.ItemsCommonProxy;
import fox.spiteful.forbidden.DarkAspects;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumic.tinkerer.common.lib.LibResearch;
import vazkii.botania.common.item.ModItems;

public class ThaumcraftBridge {
	
	private static final String RESEARCH_CATEGORY = "Titanic";
	private static final ResourceLocation RESEARCH_BACK_TEXT = new ResourceLocation(AgeOfTitans.MODID + ":textures/gui/research_background.png");
	private static final ResourceLocation RESEARCH_ICON_TEXT = new ResourceLocation(AgeOfTitans.MODID + ":textures/gui/research_icon.png");
	
	public static WandRod titanrod;
	public static WandCap vinteumcap;
	public static WandCap titarilliumcap;
	
	public ThaumcraftBridge() {
		//set up subitems
		new TitanWandCore("titan_wand_core");
		new CrystalVinteumCap("crystal_vinteum_cap");
		new InertTitarilliumCap("inert_titarillium_cap");
		new TitarilliumCap("titarillium_cap");
		new DestructionFocus("destruction_focus");
		new LeechFocus("leech_focus");
		new TransformFocus("titan_focus");
		
		titanrod = new WandRod("titan_wand", TitanWandCore.capacity, new ItemStack(TitanWandCore.wand),
				  10, new TitanWandCore.RodUpdated(), new ResourceLocation(AgeOfTitans.MODID + ":textures/models/titan_wand_core.png"));
		  vinteumcap = new WandCap("crystal_vinteum", CrystalVinteumCap.visRate, new ItemStack(CrystalVinteumCap.cap), 1);
	      vinteumcap.setTexture(new ResourceLocation(AgeOfTitans.MODID + ":textures/models/cap_crystal_vinteum.png"));
	      titarilliumcap = new WandCap("titarillium", TitarilliumCap.visRate, new ItemStack(TitarilliumCap.cap), 1);
	      titarilliumcap.setTexture(new ResourceLocation(AgeOfTitans.MODID + ":textures/models/cap_titarillium.png"));
		
		EssentiaCoalescer.preInit();
	}

	public static void postInit() {
		System.out.println("Requesting Thaumcraft Tie-In");
		  
		  registerAspects();
		  registerResearch();
		  
//		  titanrod = new WandRod("titan_wand", TitanWandCore.capacity, new ItemStack(TitanWandCore.wand),
//				  10, new TitanWandCore.RodUpdated(), new ResourceLocation(AgeOfTitans.MODID + ":textures/models/titan_wand_core.png"));
//		  vinteumcap = new WandCap("CAP_crystal_vinteum", CrystalVinteumCap.visRate, new ItemStack(CrystalVinteumCap.cap), 1);
//	      vinteumcap.setTexture(new ResourceLocation(AgeOfTitans.MODID + ":textures/models/cap_crystal_vinteum.png"));
		  
		  //WandCap WAND_CAP_GOLD = new WandCap("gold", 1f, new ItemStack(ConfigItems.itemWandCap,1,1),3);
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
		  ThaumcraftApi.registerObjectTag(new ItemStack(VinteumBlend.item), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.AURA, 1));
		  ThaumcraftApi.registerObjectTag(new ItemStack(CrystalVinteum.item), 
				  (new AspectList()).add(Aspect.MAGIC, 3).add(Aspect.AURA, 1).add(Aspect.CRYSTAL, 3));

		  ThaumcraftApi.registerObjectTag(new ItemStack(TitanWandCore.wand), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.LIFE, 2).add(Aspect.HEAL, 2).add(Aspect.DARKNESS, 2));
		  ThaumcraftApi.registerObjectTag(new ItemStack(CrystalVinteumCap.cap), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.CRYSTAL, 2));
		  ThaumcraftApi.registerObjectTag(new ItemStack(InertTitarilliumCap.cap), 
				  (new AspectList()).add(Aspect.MAGIC, 2).add(Aspect.METAL, 2));
		  ThaumcraftApi.registerObjectTag(new ItemStack(TitarilliumCap.cap), 
				  (new AspectList()).add(Aspect.MAGIC, 2).add(Aspect.METAL, 2).add(Aspect.TOOL, 4));
		  ThaumcraftApi.registerObjectTag(new ItemStack(DestructionFocus.focus), 
				  (new AspectList()).add(DarkAspects.WRATH, 4).add(Aspect.MINE, 2));
		  ThaumcraftApi.registerObjectTag(new ItemStack(LeechFocus.focus), 
				  (new AspectList()).add(DarkAspects.GLUTTONY, 4).add(Aspect.HUNGER, 8));
		  ThaumcraftApi.registerObjectTag(new ItemStack(EssentiaCoalescer.block), 
				  (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.TRAVEL, 5).add(Aspect.ELDRITCH, 8));
		  
	}
	
	private static final void registerRecipes() {
		
		TitarilliumCap.recipe = ThaumcraftApi.addInfusionCraftingRecipe("CAP_titarillium", new ItemStack(TitarilliumCap.cap), 20,
				new AspectList().add(Aspect.MAGIC, 16).add(DarkAspects.WRATH, 12),
				new ItemStack(InertTitarilliumCap.cap),
				new ItemStack[]{new ItemStack(ModItems.manaResource, 1, 1), new ItemStack(ModItems.manaResource, 1, 1)});
		
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
		pages.add(new ResearchPage("You've seen vinteum do wonderful things when used with your spell crafting altar, but failed to find a practical bridge between the powerful work of crafted spells and the refined world of Thaumcraft. Through a surprisingly-small number of experiments, you have finally been able to create a material made of vinteum that seems quite receptive to vis! Crystal Vinteum, as you've called it, is a crystal material made of vinteum and soaked in Liquid Essentia."));
		pages.add(new ResearchPage("The process is actually quite mundane; first, you must assemble a compound of vinteum and arcane ash. Simply mixing the two by hand in equal parts works. Then, find a pool of Liquid Essence and throw the compound in. The Liquid Essence quickly transforms into a strange solution which you have termed 'Saturated Etherium'. Once this is done, the final step is to reduce the liquid, leaving behind only the crystal vinteum."));
		pages.add(new ResearchPage("To do this, you must light a fire directly above the saturated etherium. Once the fumes catch, the process is exceptionally quick. The crystal vinteum is produced, leaving behind most of the liquid essence."));
		pages.add(new ResearchPage(VinteumBlend.recipe));
		
		Research.CRYSTAL_VINTEUM.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
		pages = new LinkedList<ResearchPage>();
		pages.add(new ResearchPage("The idea to use the newly discovered Crystal Vinteum as materials for wand caps came almost immediately. While the material isn't quite as potent as Thaumium, the crystals form a cap still capable of reducing vis cost when used on a wand."));
		pages.add(new ResearchPage(CrystalVinteumCap.recipe));
				
		Research.VINTEUM_CAP.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
		IArcaneRecipe arcaneRecipe;
		
		arcaneRecipe = ThaumcraftApi.addArcaneCraftingRecipe(
				"FOCUS_destruction", new ItemStack(DestructionFocus.focus), new AspectList().add(Aspect.ENTROPY, 15).add(Aspect.EARTH, 10).add(Aspect.FIRE, 10),
				"vwv", "lhl", "vwv", 'v', CrystalVinteum.item, 'w', new ItemStack(ForbiddenItems.deadlyShards, 1, 0),
				'l', new ItemStack(ForbiddenItems.deadlyShards, 1, 4), 'h', AgeOfTitans.titanHeart);
		
		pages = new LinkedList<ResearchPage>();
		pages.add(new ResearchPage("The invention of crystal vinteum has led to many other discoveries. One example is the use of the crystal as a base for wand foci. The crystal seems to work best when large amounts of energy are dumped into it and released quickly, when compared to quartz. The Destruction Focus is the perfect example. The focus takes a large amount of vis and channels it into an emotional, anger-filled destructive blast. Like the Flesh Titan who's heart the focus is created from, the focus then destroys all breakable blocks around it!"));
		pages.add(new ResearchPage(arcaneRecipe));
		
		Research.DESTRUCTION_FOCUS.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
		arcaneRecipe = ThaumcraftApi.addArcaneCraftingRecipe(
				"FOCUS_leech", new ItemStack(LeechFocus.focus), new AspectList().add(Aspect.ENTROPY, 15).add(Aspect.FIRE, 10).add(Aspect.WATER, 10),
				"vgv", "sps", "vgv", 'v', CrystalVinteum.item, 'g', new ItemStack(ForbiddenItems.deadlyShards, 1, 6),
				's', new ItemStack(ForbiddenItems.deadlyShards, 1, 5), 'h', new ItemStack(ConfigItems.itemFocusPech));
		
		pages = new LinkedList<ResearchPage>();
		pages.add(new ResearchPage("After transforming the Pech's Curse focus into one of healing, you wondered what else you might be able to do. Combined with the power of the crystal vinteum you've made, you have found a new use for the focus. Instead of completely discarding the focus' inate harming abilities, you alter them such that the target is still damaged, but their energy is instead transferred to you! With the leech focus, damage dealt to enemies restores your own hunger!"));
		pages.add(new ResearchPage(arcaneRecipe));
		
		Research.LEECH_FOCUS.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
		pages = new LinkedList<ResearchPage>();
		pages.add(new ResearchPage("Magic exists all around you. Just look at those huge Titans. Certainly, that much rage and wrath couldn't exist if there wasn't another plane of energy to feed it. They don't even seem to eat cows and stuff. You're not sure what, but you know there must be magic involved. The only question is... where does it come from?"));
		
		Research.TITANS.getItem().setPages(pages.toArray(new ResearchPage[0]));
		ItemStack voidMetal = new ItemStack(ConfigItems.itemResource, 1, 16);
		ItemStack diamond = new ItemStack(Items.diamond);
		ItemStack crucible = new ItemStack(ConfigBlocks.blockMetalDevice, 1, 0);
		
		recipe = ThaumcraftApi.addInfusionCraftingRecipe("essentia_coalescer", new ItemStack(EssentiaCoalescer.block), 100,
				new AspectList().add(Aspect.ELDRITCH, 64).add(Aspect.TRAVEL, 32).add(Aspect.MAGIC, 32).add(Aspect.WATER, 32),
				new ItemStack(ConfigBlocks.blockMirror, 1, 6),
				new ItemStack[]{diamond, voidMetal, voidMetal, crucible, voidMetal, voidMetal});
		
		pages = new LinkedList<ResearchPage>();
		pages.add(new ResearchPage("You've seen the magic of essentia mirrors, and no longer need to move jars back and forth for simple infusions. The mirrors work exceptionally for things that draw essentia, like infusions, but your alchemy setup is still somewhat primative. Your recent discovery of void metal, however, might have changed this for good. You've already noted the exceptional capabilities of the metal -- ever expansive, ever repairing. The metal is also somehow tied to a place that is not the same you live on. While the idea was a stretch, you began"));
		pages.add(new ResearchPage("to wonder if this plane and the place over which essentia is trasferred via mirrors was not one and the same. This idea proved to be quite correct! Following the discovery, you searched for some way to make use of the connection... and after much tinkering, you've created what you've called the Essentia Coalescer. While not exactly what you were after, the Essentia Coalescer allows you to draw essentia through the plane towards itself. What makes the invention worthwhile, however, is the addition of a condensing agent that allows"));
		pages.add(new ResearchPage("the drawn essentia to again settle into the slurry form you know as Essentia. You've even hooked up a pipe apparatus to connect to all your gadgets and gizmos!"));
		pages.add(new ResearchPage(recipe));
		pages.add(new ResearchPage("The Essentia Coalescer must be placed on top of a pipe in order for the coalescing to work. In addition, the block must be surrounded by some redstone and glass in a specific pattern to help encourage the coalescing of the essentia. Four pillars should surround the Coalescer "));
		pages.add(new ResearchPage(new ResourceLocation(AgeOfTitans.MODID + ":textures/gui/coalescer_book.png"), "Coalescer Construct"));
		
		Research.ESSENTIA_COALESCER.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
		ItemStack crystal = new ItemStack(CrystalVinteum.item);
		recipe = ThaumcraftApi.addInfusionCraftingRecipe("FOCUS_titan", new ItemStack(TransformFocus.focus), 100,
				new AspectList().add(Aspect.ORDER, 32).add(Aspect.EXCHANGE, 24).add(Aspect.WATER, 32),
				new ItemStack(AgeOfTitans.titanHeart),
				new ItemStack[]{diamond, crystal, crystal, new ItemStack(Items.ender_pearl), crystal, crystal});
		
		pages = new LinkedList<ResearchPage>();
		
		pages.add(new ResearchPage("You've done it! Through careful, intensive study, you've finally had a breakthrough! The Titans' wrath certainly has magical origins, and almost trivially so. Your studies have revealed a strange and constant stream of magical energy affecting the Titans, causing their incredible rage. Your discovery doesn't stop there, however. You've found that simply staunching the stream of negative energies not only stops the Titans from attacking players... it makes them attack other Titans!"));
		pages.add(new ResearchPage(recipe));
		
		Research.TITAN_FOCUS.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
		pages = new LinkedList<ResearchPage>();
		
		pages.add(new ResearchPage("Finally! A cap worth while! You've found that the elementium used in crafting titarillium is augmented by the hard metal exterior rather than deterred. As such, caps made of titarillium are incredibly adept at channeling magic."));
		pages.add(new ResearchPage(InertTitarilliumCap.recipe));
		pages.add(new ResearchPage("Unfortunately, however, the metal very still resistent to shaping. As such, the wand caps you make by simply shaping the metal aren't as effective. The effect is all but nullified, however, through a simple infusion."));
		pages.add(new ResearchPage(TitarilliumCap.recipe));
		
		Research.TITARILLIUM_CAP.getItem().setPages(pages.toArray(new ResearchPage[0]));
		
	}
	
	private static final void registerResearch() {
		ResearchCategories.registerCategory(RESEARCH_CATEGORY, RESEARCH_ICON_TEXT, RESEARCH_BACK_TEXT);
		
		for (Research r : Research.values())
			ResearchCategories.addResearch(r.getItem());
		
		Research.TITANS.getItem().setAutoUnlock();
	}
	
	private static enum Research {
		TITANS("basic_titan", null, 4, 4, 1, new ItemStack(AgeOfTitans.titanHeart),
				null, null, true, false),
		TITAN_ROD("ROD_titan_wand", new AspectList().add(DarkAspects.WRATH, 1)
										.add(Aspect.FLESH, 1)
										.add(Aspect.MAN, 1),
			1, 1, 1, new ItemStack(TitanWandCore.wand), new String[]{"basic_titan", "ROD_primal_staff"}, new ItemStack[]{new ItemStack(AgeOfTitans.titanHeart)},
			true, false, 3),
		CRYSTAL_VINTEUM("crystal_vinteum", new AspectList().add(Aspect.AURA, 4).add(Aspect.MAGIC, 2).add(Aspect.CRYSTAL, 5),
				2, 5, 1, new ItemStack(CrystalVinteum.item),
				new String[]{"basic_titan"}, new ItemStack[]{new ItemStack(ItemsCommonProxy.itemOre, 1, ItemOre.META_VINTEUMDUST), new ItemStack(BlocksCommonProxy.liquidEssence)}, true, true),
		VINTEUM_CAP("CAP_crystal_vinteum", new AspectList().add(Aspect.METAL, 4).add(Aspect.CRYSTAL, 3),
				1, 3, 2, new ItemStack(CrystalVinteumCap.cap), new String[]{"CAP_gold", "crystal_vinteum"}, new ItemStack[]{new ItemStack(CrystalVinteum.item)},
				false, true),
		TITARILLIUM_CAP("CAP_titarillium", new AspectList().add(Aspect.METAL, 3).add(Aspect.TOOL, 2).add(Aspect.BEAST, 2).add(Aspect.MAN, 2),
				0, 3, 1, new ItemStack(TitarilliumCap.cap), new String[]{"CAP_thaumium", "crystal_vinteum"}, null,
				false, true),
		DESTRUCTION_FOCUS("FOCUS_destruction", new AspectList().add(Aspect.MINE, 1).add(DarkAspects.WRATH, 1).add(Aspect.GREED, 1),
				1, 7, 2, new ItemStack(DestructionFocus.focus), new String[]{"FOCUSFIRE", "crystal_vinteum"}, null,
				false, false, 1),
		LEECH_FOCUS("FOCUS_leech", new AspectList().add(Aspect.HUNGER, 1).add(DarkAspects.WRATH, 1).add(DarkAspects.GLUTTONY, 1),
				3, 7, 2, new ItemStack(LeechFocus.focus), new String[]{"FOCUSFIRE", "crystal_vinteum", LibResearch.KEY_FOCUS_HEAL}, null,
				false, false, 1),
		TITAN_FOCUS("FOCUS_titan", new AspectList().add(Aspect.MAN, 1).add(DarkAspects.WRATH, 1).add(Aspect.ENTROPY, 1).add(Aspect.ORDER, 1),
				2, 8, 3, new ItemStack(TransformFocus.focus), new String[]{"FOCUSFIRE", "crystal_vinteum", "FOCUS_leech", "FOCUS_destruction"}, new ItemStack[]{new ItemStack(AgeOfTitans.titanHeart)},
				false, false, 1),
		ESSENTIA_COALESCER("essentia_coalescer", new AspectList().add(Aspect.ELDRITCH, 1).add(Aspect.DARKNESS, 1).add(Aspect.TRAVEL, 1).add(Aspect.MAGIC, 1).add(Aspect.WATER, 1),
				6, 5, 3, new ItemStack(EssentiaCoalescer.block), new String[]{"THAUMATORIUM", "MIRRORESSENTIA", "crystal_vinteum", "VOIDMETAL"}, null,
				true, false);
		
		/**
		 * THAUMATORIUM
		 * MIRRORESSENTIA
		 * crystal_vinteum
		 */
		
		private ResearchItem item;
		
		private Research(String key, AspectList aspects, int x, int y, int complexity,
				ItemStack icon, String[] parents, ItemStack[] itemScans, boolean isSpecial, boolean isSecondary) {
			this(key, aspects, x, y, complexity, icon, parents, itemScans, isSpecial,
					isSecondary, 0);
		}
		
		private Research(String key, AspectList aspects, int x, int y, int complexity,
				ItemStack icon, String[] parents, ItemStack[] itemScans,
				boolean isSpecial, boolean isSecondary, int warp) {
			item = new ResearchItem(key, RESEARCH_CATEGORY, aspects, x, y, complexity, icon);
			if (aspects == null)
				item.isAutoUnlock();
				//item.isSecondary();
			if (itemScans != null) {
				item.setItemTriggers(itemScans);
			}
			if (isSpecial)
				item.isSpecial();
			//item.parentsHidden = parents;
			if (parents != null)
				item.setParents(parents);
			
			if (itemScans == null && parents == null)
				;
			else if (itemScans == null) {
				item.setConcealed();
				System.out.println(key + " research is 'concealed'");
			} else {
				item.setHidden();
				System.out.println(key + " research is 'hidden'");
			}
			if (isSecondary)
				item.setSecondary();
			
			if (warp > 0) {
				ThaumcraftApi.addWarpToResearch(key, warp);
			}
					
			//String key, String category, AspectList tags, int col, int row, int complex, ResourceLocation icon
		}
		
		public ResearchItem getItem() {
			return item;
		}
		
	}
	
	
	//public ResearchItem(String key, String category, AspectList tags, int col, int row, int complex, ItemStack icon)
	
}
