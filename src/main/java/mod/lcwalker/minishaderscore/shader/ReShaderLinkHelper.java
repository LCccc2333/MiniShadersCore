package mod.lcwalker.minishaderscore.shader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.util.JsonException;

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
        OpenGlHelper.func_153187_e(manager.getProgramObj());
    }

    public int createProgram() throws JsonException {
    	
    	int i = OpenGlHelper.func_153183_d();
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
        OpenGlHelper.func_153179_f(manager.getProgramObj());
    }
}