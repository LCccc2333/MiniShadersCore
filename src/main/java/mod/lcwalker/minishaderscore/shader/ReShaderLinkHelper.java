package mod.lcwalker.minishaderscore.shader;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.util.JsonException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ReShaderLinkHelper {
	
    private static ReShaderLinkHelper staticShaderLinkHelper;

    public static void setNewStaticShaderLinkHelper() {
    	staticShaderLinkHelper = new ReShaderLinkHelper();
    }

    public static ReShaderLinkHelper getStaticShaderLinkHelper() {
        return staticShaderLinkHelper;
    }

    public void deleteShader(ReShaderManager manager) {
    	
    	manager.getFshShaderLoader().delete(manager);
    	manager.getVshShaderLoader().delete(manager);
        OpenGlHelper.glDeleteProgram(manager.getProgramObj());
    }

    public int createProgram() throws JsonException {
    	
    	int i = OpenGlHelper.glCreateProgram();
        if (i <= 0) {
            throw new JsonException("");
        }
        else {
            return i;
        }
    }

    public void linkProgram(ReShaderManager manager) {
    	
    	manager.getFshShaderLoader().attach(manager);
        manager.getVshShaderLoader().attach(manager);
        OpenGlHelper.glLinkProgram(manager.getProgramObj());
    }
}