package mod.lcwalker.minishaderscore.shader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;

@SideOnly(Side.CLIENT)
public class ReShaderLoader {
	
    private final ReShaderLoader.ShaderType stype;
    private final String type;
    private int shader;
    private int i = 0;

    private ReShaderLoader(ReShaderLoader.ShaderType stype, int shader, String type) {
    	
        this.stype = stype;
        this.shader = shader;
        this.type = type;
    }

    public void attach(ReShaderManager manager) {
    	
        ++this.i;
        
        OpenGlHelper.func_153178_b(manager.getProgramObj(), this.shader);
    }

    public void delete(ReShaderManager manager) {
    	
        --this.i;
        if (this.i <= 0) {
        	
            OpenGlHelper.func_153180_a(this.shader);
            
            this.stype.getLoadedShaders().remove(this.type);
        }
    }

    public static ReShaderLoader readFile(IResourceManager iResourceManager, ReShaderLoader.ShaderType type, String name) throws Exception {
    	
        ReShaderLoader shaderloader = (ReShaderLoader) type.getLoadedShaders().get(name);

        if (shaderloader == null) {
        	
        	InputStream is = new FileInputStream(new File(ReShaderGroup.shadersprogramFile.getCanonicalPath(), name + type.getShaderExtension()));
            BufferedInputStream bis = new BufferedInputStream(is);
            
            byte[] bytes = IOUtils.toByteArray(bis);
            
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes);
            buffer.position(0);
            
            int id = OpenGlHelper.func_153195_b(type.getShaderMode());
            OpenGlHelper.func_153169_a(id, buffer);
            OpenGlHelper.func_153170_c(id);

            if (OpenGlHelper.func_153157_c(id, OpenGlHelper.field_153208_p) == 0) {}

            shaderloader = new ReShaderLoader(type, id, name);
            type.getLoadedShaders().put(name, shaderloader);
        }

        return shaderloader;
    }

    @SideOnly(Side.CLIENT)
    public static enum ShaderType {
    	
        VERTEX("vertex", ".vsh", OpenGlHelper.field_153209_q),
        FRAGMENT("fragment", ".fsh", OpenGlHelper.field_153210_r);
    	
        private final String name;
        private final String extension;
        private final int mode;
        private final Map map = Maps.newHashMap();

        private ShaderType(String name, String extension, int mode) {
        	
            this.name = name;
            this.extension = extension;
            this.mode = mode;
        }

        public String getShaderName() {
            return this.name;
        }

        protected String getShaderExtension() {
            return this.extension;
        }

        protected int getShaderMode() {
            return this.mode;
        }

        protected Map getLoadedShaders() {
            return this.map;
        }
    }
}