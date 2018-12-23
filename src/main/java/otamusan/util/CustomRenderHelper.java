package otamusan.util;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomRenderHelper {
	/** Float buffer used to set OpenGL material colors */
	private static final FloatBuffer COLOR_BUFFER = GLAllocation.createDirectFloatBuffer(16);

	public static void percentLight(int light, int pname, float e) {
		COLOR_BUFFER.clear();
		GL11.glGetLight(light, pname, COLOR_BUFFER);
		COLOR_BUFFER.put(0, COLOR_BUFFER.get(0)*e);
		COLOR_BUFFER.put(1, COLOR_BUFFER.get(1)*e);
		COLOR_BUFFER.put(2, COLOR_BUFFER.get(2)*e);
		//COLOR_BUFFER.put(3, COLOR_BUFFER.get(3)*e);
		GlStateManager.glLight(light, pname, COLOR_BUFFER);
	}

	public static void percentLights(int light, float e) {
		percentLight(light, GL11.GL_DIFFUSE, e);
		percentLight(light, GL11.GL_AMBIENT, e);
		percentLight(light, GL11.GL_SPECULAR, e);
	}

	public static void percentLightModel(int pname, float e) {
		COLOR_BUFFER.clear();
		GL11.glGetFloat(pname, COLOR_BUFFER);
		COLOR_BUFFER.put(0, COLOR_BUFFER.get(0)*e);
		COLOR_BUFFER.put(1, COLOR_BUFFER.get(1)*e);
		COLOR_BUFFER.put(2, COLOR_BUFFER.get(2)*e);
		//COLOR_BUFFER.put(3, COLOR_BUFFER.get(3)*e);
		GlStateManager.glLightModel(pname, COLOR_BUFFER);
	}

	public static void percentAllLights(float e) {
		percentLights(GL11.GL_LIGHT0, e);
		percentLights(GL11.GL_LIGHT1, e);
		/*
		percentLights(GL11.GL_LIGHT2, e);
		percentLights(GL11.GL_LIGHT3, e);
		percentLights(GL11.GL_LIGHT4, e);
		percentLights(GL11.GL_LIGHT5, e);
		percentLights(GL11.GL_LIGHT6, e);
		percentLights(GL11.GL_LIGHT7, e);
		*/
		percentLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, e);
	}
}