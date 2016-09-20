package com.SkyIsland.AgeOfTitans.thaumcraft;

import org.lwjgl.opengl.GL11;

import com.SkyIsland.AgeOfTitans.AgeOfTitans;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;

public class RendererEssentiaCoalescer extends TileEntitySpecialRenderer {

	private static IModelCustom model;
	
	private static final ResourceLocation modelLoc = new ResourceLocation(AgeOfTitans.MODID + ":models/essentia_coalescer.obj");
	
	private static final ResourceLocation texture = new ResourceLocation(AgeOfTitans.MODID + ":textures/models/essentia_coalescer.png");
	
	public RendererEssentiaCoalescer() {
        model = AdvancedModelLoader.loadModel(modelLoc);
	}
	
	
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
	
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		this.bindTexture(texture);
		
		//GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		
		GL11.glPushMatrix();
		TileEntityEssentiaCoalescer tile = (TileEntityEssentiaCoalescer) tileentity;
		ForgeDirection dir = tile.getOutputDirection();
		System.out.println(dir.name());
		
		if (dir != ForgeDirection.EAST) {
			float direction, xrot, yrot, zrot;
			direction = xrot = yrot = zrot = 0f;
			
			switch(dir) {
			case NORTH:
				direction = 90f;
				yrot = 1.0f;
				break;
			case SOUTH:
				direction = -90f;
				yrot = 1.0f;
				break;
			case WEST:
				direction = 180f;
				yrot = 180.0f;
				break;
			case DOWN:
				direction = -90f;
				zrot = 1.0f;
				break;
			case UP:
			default:
				direction = 90f;
				zrot = 1.0f;
				break;
			}
			
			GL11.glRotatef(direction, xrot, yrot, zrot);
		}
		
		
		
		model.renderAll();
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
