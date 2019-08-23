package mod.lcwalker.minishaderscore.util;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.lcwalker.minishaderscore.shader.ReShaderGroup;
import mod.lcwalker.minishaderscore.shader.ReShaderLinkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class UseShader {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	private static boolean canUse = false;
	private static boolean canUpdate = true;
	public static ReShaderGroup theShaderGroup;
	
	@SubscribeEvent
	public void renderTick(TickEvent.RenderTickEvent event) {
		
		if (event.phase == TickEvent.Phase.START) {
			if (OpenGlHelper.shadersSupported && ReShaderLinkHelper.getStaticShaderLinkHelper() == null) {
                ReShaderLinkHelper.setNewStaticShaderLinkHelper();
            }
	    }
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void loadShaderGroup(RenderGameOverlayEvent.Pre event) {
		
		if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
			
			if (OpenGlHelper.shadersSupported && this.theShaderGroup != null) {
				
				GL11.glMatrixMode(5890);
				GL11.glLoadIdentity();
				
				GL11.glPushMatrix();
				
				this.theShaderGroup.loadShaderGroup(event.partialTicks);
				
				GL11.glPopMatrix();
			}
			
			mc.getFramebuffer().bindFramebuffer(true);
		} 
	}
	
	public static void setShaders(String jsonName) {
		
		canUse = true;
		
		try {
			if (OpenGlHelper.shadersSupported) {
					
				if (canUpdate) {
					
					theShaderGroup = new ReShaderGroup(mc.getTextureManager(), new ResourceManager(mc.getResourceManager()), mc.getFramebuffer(), jsonName);
					theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
					
					canUpdate = false;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeShaders() {
		
		if (OpenGlHelper.shadersSupported) {
			if (canUse) {
				
				theShaderGroup = null;
				ReShaderGroup.jsonf = null;
				
				resetShaderIndex();
				
				canUse = false;
				canUpdate = true;
			}
		}
	}
	
	public static void resetShaderIndex() {
		
		int i = ReflectionHelper.getPrivateValue(EntityRenderer.class, null, "shaderCount", "field_147708_e");
		ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, mc.entityRenderer, i, "shaderIndex", "field_147713_ae");
	}
}
