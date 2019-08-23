package mod.lcwalker.minishaderscore.shader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.util.JsonBlendingMode;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ReShaderManager {
	
    private static final ReShaderDefault defaultShaderUniform = new ReShaderDefault();
    private static ReShaderManager staticShaderManager = null;
    
    private static int currentProgram = -1;
    private final int program;
    private static boolean lastCull = true;
    private final boolean useFaceCulling;
    private boolean isDir;
    
    private final Map shaderSamplers = Maps.newHashMap();
    private final Map mappedShaderUniforms = Maps.newHashMap();
    private final List samplerNames = Lists.newArrayList();
    private final List shaderSamplerLocations = Lists.newArrayList();
    private final List shaderUniforms = Lists.newArrayList();
    private final List shaderUniformLocations = Lists.newArrayList();
    private final List attribLocations;
    private final List attributes;
    
    private final JsonBlendingMode jsonBlendingMode;
    
    private final ReShaderLoader vshLoader;
    private final ReShaderLoader fshLoader;
    
    private final String jsonName;
    public static File jsonf = null;

    public ReShaderManager(IResourceManager resManager, String jsonName) throws JsonException {
    	
    	this.jsonf = new File(ReShaderGroup.shadersprogramFile, jsonName + ".json");
		
		this.jsonName = jsonName;
		
		JsonParser jsonparser = new JsonParser();
        InputStream is = null;

        try {
        	
    		is = new FileInputStream(this.jsonf);
            JsonObject jsonobject = jsonparser.parse(IOUtils.toString(is, Charsets.UTF_8)).getAsJsonObject();
            String s1 = JsonUtils.getString(jsonobject, "vertex");
            String s2 = JsonUtils.getString(jsonobject, "fragment");
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "samplers", (JsonArray) null);

            if (jsonarray != null) {
            	
                int i = 0;
                for (Iterator iterator = jsonarray.iterator(); iterator.hasNext(); ++i) {
                	
                    JsonElement jsonelement = (JsonElement) iterator.next();
                    try {
                        this.parseSampler(jsonelement);
                    }
                    catch (Exception e) {
                    	e.printStackTrace();
                    }
                }
            }

            JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "attributes", (JsonArray) null);
            Iterator iterator1;
            
            if (jsonarray1 != null) {
            	
                int j = 0;
                
                this.attribLocations = Lists.newArrayListWithCapacity(jsonarray1.size());
                this.attributes = Lists.newArrayListWithCapacity(jsonarray1.size());

                for (iterator1 = jsonarray1.iterator(); iterator1.hasNext(); ++j) {
                	
                    JsonElement jsonelement1 = (JsonElement) iterator1.next();
                    try {
                        this.attributes.add(JsonUtils.getString(jsonelement1, "attribute"));
                    }
                    catch (Exception e) {
                    	e.printStackTrace();
                    }
                }
            }
            else {
            	
                this.attribLocations = null;
                this.attributes = null;
            }

            JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonobject, "uniforms", (JsonArray) null);
            if (jsonarray2 != null) {
            	
                int k = 0;
                for (Iterator iterator2 = jsonarray2.iterator(); iterator2.hasNext(); ++k) {
                	
                    JsonElement jsonelement2 = (JsonElement) iterator2.next();
                    try {
                        this.parseUniform(jsonelement2);
                    }
                    catch (Exception e) {
                    	e.printStackTrace();
                    }
                }
            }

            this.jsonBlendingMode = JsonBlendingMode.parseBlendNode(JsonUtils.getJsonObject(jsonobject, "blend", (JsonObject) null));
            this.useFaceCulling = JsonUtils.getBoolean(jsonobject, "cull", true);
            this.vshLoader = ReShaderLoader.readFile(resManager, ReShaderLoader.ShaderType.VERTEX, s1);
            this.fshLoader = ReShaderLoader.readFile(resManager, ReShaderLoader.ShaderType.FRAGMENT, s2);
            this.program = ReShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
            
            ReShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
            
            this.setupUniforms();

            if (this.attributes != null) {
            	
                iterator1 = this.attributes.iterator();
                while (iterator1.hasNext()) {
                	
                    String s3 = (String) iterator1.next();
                    
                    int l = OpenGlHelper.glGetAttribLocation(this.program, s3);
                    this.attribLocations.add(Integer.valueOf(l));
                }
            }
        }
        catch (Exception e) {
        	throw new JsonException("");
        }
        finally {
            IOUtils.closeQuietly(is);
        }
        
        this.fuckoff();
    }

    public void deleteShader() {
        ReShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
    }

    public void endShader() {
    	
        OpenGlHelper.glUseProgram(0);
        
        currentProgram = -1;
        staticShaderManager = null;
        lastCull = true;

        for (int i = 0; i < this.shaderSamplerLocations.size(); ++i) {
            if (this.shaderSamplers.get(this.samplerNames.get(i)) != null) {
            	
                GL13.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + i);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
        }
    }

    public void useShader() {
    	
        this.isDir = false;
        
        staticShaderManager = this;
        
        this.jsonBlendingMode.apply();

        if (this.program != currentProgram) {
        	
            OpenGlHelper.glUseProgram(this.program);
            
            currentProgram = this.program;
        }

        if (lastCull != this.useFaceCulling) {
        	
            lastCull = this.useFaceCulling;
            if (this.useFaceCulling) {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            else {
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
        }

        for (int i = 0; i < this.shaderSamplerLocations.size(); ++i) {
        	
            if (this.shaderSamplers.get(this.samplerNames.get(i)) != null) {
            	
                GL13.glActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + i);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                
                Object object = this.shaderSamplers.get(this.samplerNames.get(i));
                
                int j = -1;
                if (object instanceof Framebuffer) {
                    j = ((Framebuffer) object).framebufferTexture;
                }
                else if (object instanceof ITextureObject) {
                    j = ((ITextureObject) object).getGlTextureId();
                }
                else if (object instanceof Integer) {
                    j = ((Integer) object).intValue();
                }

                if (j != -1) {
                	
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, j);
                    
                    OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, (CharSequence) this.samplerNames.get(i)), i);
                }
            }
        }

        Iterator iterator = this.shaderUniforms.iterator();
        while (iterator.hasNext()) {
        	
            ReShaderUniform shaderuniform = (ReShaderUniform) iterator.next();
            shaderuniform.upload();
        }
    }

    public void fuckoff() {
        this.isDir = true;
    }

    public ReShaderUniform getShaderUniform(String key) {
        return this.mappedShaderUniforms.containsKey(key) ? (ReShaderUniform) this.mappedShaderUniforms.get(key) : null;
    }

    public ReShaderUniform getShaderUniformOrDefault(String key) {
        return (ReShaderUniform) (this.mappedShaderUniforms.containsKey(key) ? (ReShaderUniform) this.mappedShaderUniforms.get(key) : defaultShaderUniform);
    }

    private void setupUniforms() {
    	
        int i = 0;
        String s;
        int k;

        for (int j = 0; i < this.samplerNames.size(); ++j) {
        	
            s = (String) this.samplerNames.get(i);
            k = OpenGlHelper.glGetUniformLocation(this.program, s);
            if (k == -1) {
            	
                this.shaderSamplers.remove(s);
                this.samplerNames.remove(j);
                
                --j;
            }
            else {
                this.shaderSamplerLocations.add(Integer.valueOf(k));
            }

            ++i;
        }

        Iterator iterator = this.shaderUniforms.iterator();
        while (iterator.hasNext()) {
        	
            ReShaderUniform shaderuniform = (ReShaderUniform) iterator.next();
            s = shaderuniform.getName();
            
            k = OpenGlHelper.glGetUniformLocation(this.program, s);
            if (k == -1) {}
            else {
            	
                this.shaderUniformLocations.add(Integer.valueOf(k));
                
                shaderuniform.setLocation(k);
                this.mappedShaderUniforms.put(s, shaderuniform);
            }
        }
    }

    private void parseSampler(JsonElement jsonElement) {
    	
        JsonObject jsonobject = JsonUtils.getJsonObject(jsonElement, "sampler");
        String s = JsonUtils.getString(jsonobject, "name");

        if (!JsonUtils.isString(jsonobject, "file")) {
        	
            this.shaderSamplers.put(s, (Object) null);
            this.samplerNames.add(s);
        }
        else {
            this.samplerNames.add(s);
        }
    }

    public void addSamplerTexture(String s, Object o) {
    	
        if (this.shaderSamplers.containsKey(s)) {
            this.shaderSamplers.remove(s);
        }

        this.shaderSamplers.put(s, o);
        
        this.fuckoff();
    }

    private void parseUniform(JsonElement jsonElement) throws JsonException {
    	
        JsonObject jsonobject = JsonUtils.getJsonObject(jsonElement, "uniform");
        String s = JsonUtils.getString(jsonobject, "name");
        
        int i = ReShaderUniform.parseType(JsonUtils.getString(jsonobject, "type"));
        int j = JsonUtils.getInt(jsonobject, "count");
        float[] afloat = new float[Math.max(j, 16)];
        
        JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "values");
        if (jsonarray.size() != j && jsonarray.size() > 1) {
            throw new JsonException("");
        }
        else {
        	
            int k = 0;
            for (Iterator iterator = jsonarray.iterator(); iterator.hasNext(); ++k) {
            	
                JsonElement jsonelement1 = (JsonElement) iterator.next();
                try {
                    afloat[k] = JsonUtils.getFloat(jsonelement1, "value");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (j > 1 && jsonarray.size() == 1) {
                while (k < j) {
                	
                    afloat[k] = afloat[0];
                    ++k;
                }
            }

            int l = j > 1 && j <= 4 && i < 8 ? j - 1 : 0;
            
            ReShaderUniform shaderuniform = new ReShaderUniform(s, i + l, j, this);
            if (i <= 3) {
                shaderuniform.intBufferPut((int) afloat[0], (int) afloat[1], (int) afloat[2], (int) afloat[3]);
            }
            else if (i <= 7) {
                shaderuniform.floatBufferPut_(afloat[0], afloat[1], afloat[2], afloat[3]);
            }
            else {
                shaderuniform.putfs(afloat);
            }

            this.shaderUniforms.add(shaderuniform);
        }
    }

    public ReShaderLoader getVshShaderLoader() {
        return this.vshLoader;
    }

    public ReShaderLoader getFshShaderLoader() {
        return this.fshLoader;
    }

    public int getProgramObj() {
        return this.program;
    }
}