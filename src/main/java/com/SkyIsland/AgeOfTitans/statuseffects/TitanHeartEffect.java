package com.SkyIsland.AgeOfTitans.statuseffects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * This effect is <em>like</em> a potion effect, but tracking of what has what
 * effects and that junk is done here. Since we only wanna do one thing, it
 * isn't worth it to inject our potion effect via reflection and almost
 * never use it. Simple event handling/fakeout should suffice
 * @author Skyler
 *
 */
public class TitanHeartEffect {

	public static TitanHeartEffect instance;
	
	private static class Record {
		
		private int initialDuration;
		private int durationLeft;
		private int amplitude;
		
		public Record(int duration, int amplitude) {
			this.durationLeft = duration;
			this.initialDuration = duration;
			this.amplitude = amplitude;
		}
		
		public boolean tick() {
			return (--durationLeft <= 0);
		}
		
		public int getAmplitude() {
			return amplitude;
		}
		
		public int getInitialDuration() {
			return initialDuration;
		}
	}
	
	private Map<EntityLivingBase, Record> riseMap;
	private Map<EntityLivingBase, Record> fallMap;
	
	private final PotionEffect[] strength;
	private final PotionEffect[] vision;
	private final PotionEffect[] slowness;
	private final PotionEffect[] weakness;
	private final PotionEffect[] vuln;
	
	public TitanHeartEffect() {
		instance = this;
		riseMap = new HashMap<EntityLivingBase, Record>();
		fallMap = new HashMap<EntityLivingBase, Record>();
		
		strength = new PotionEffect[]{
			new PotionEffect(Potion.damageBoost.id, 120, 2),
			new PotionEffect(Potion.damageBoost.id, 120, 3),
			new PotionEffect(Potion.damageBoost.id, 120, 4)
		};
		vision = new PotionEffect[]{
				new PotionEffect(Potion.nightVision.id, 120, 1),
				new PotionEffect(Potion.nightVision.id, 120, 1),
				new PotionEffect(Potion.nightVision.id, 120, 1)
			};
		weakness = new PotionEffect[]{
				new PotionEffect(Potion.weakness.id, 120, 3),
				new PotionEffect(Potion.weakness.id, 120, 4),
				new PotionEffect(Potion.weakness.id, 120, 5)
			};
		slowness = new PotionEffect[]{
				new PotionEffect(Potion.moveSlowdown.id, 120, 1),
				new PotionEffect(Potion.moveSlowdown.id, 120, 2),
				new PotionEffect(Potion.moveSlowdown.id, 120, 3)
			};
		vuln = new PotionEffect[]{
				new PotionEffect(Potion.resistance.id, 120, -1),
				new PotionEffect(Potion.resistance.id, 120, -2),
				new PotionEffect(Potion.resistance.id, 120, -3)
			};
		
		//MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	public void register(EntityLivingBase entity, int duration, int amplitude) {
		if (amplitude > 2)
			amplitude = 2;
		riseMap.put(entity, new Record(duration, amplitude));
	}
	
	@SubscribeEvent
	public void update(ServerTickEvent event) {
		doRise();
		doFall();
	}
	
	private void doRise() {
		if (riseMap.isEmpty())
			return;

		Entry<EntityLivingBase, Record> entry;
		Iterator<Entry<EntityLivingBase, Record>> it = riseMap.entrySet().iterator();
		while (it.hasNext()){
			entry = it.next();
			if (entry.getKey().isDead) {
				it.remove();
				continue;
			}
			
			if (entry.getValue().tick()) {
//				it.remove();
//				continue;
				//move to fall 
				fallMap.put(entry.getKey(), 
						new Record(entry.getValue().getInitialDuration(), entry.getValue().getAmplitude()));
				it.remove();
				continue;
			}
			
			//effect continues
			if (entry.getKey().ticksExisted % 20 != 0) {
				//not a second interval
				continue;
			}
			
			performRise(entry.getKey(), entry.getValue().getAmplitude());
		}
	}
	
	private void doFall() {
		if (fallMap.isEmpty())
			return;
		
		Entry<EntityLivingBase, Record> entry;
		Iterator<Entry<EntityLivingBase, Record>> it = fallMap.entrySet().iterator();
		while (it.hasNext()){
			entry = it.next();
			if (entry.getKey().isDead) {
				it.remove();
				continue;
			}
			
			if (entry.getValue().tick()) {
				it.remove();
				continue;
			}
			
			//effect continues
			if (entry.getKey().ticksExisted % 20 != 0) {
				//not a second interval
				continue;
			}
			
			performFall(entry.getKey(), entry.getValue().getAmplitude());
		}
	}
	
	private void performRise(EntityLivingBase ent, int amp) {
		//good effects: night vision, strength
		ent.addPotionEffect(strength[amp]);
		ent.addPotionEffect(vision[amp]);
	}
	
	private void performFall(EntityLivingBase ent, int amp) {
		//bad effects: slowness, weakness, -resistance
		ent.addPotionEffect(slowness[amp]);
		ent.addPotionEffect(weakness[amp]);
		ent.addPotionEffect(vuln[amp]);
	}
	
}
