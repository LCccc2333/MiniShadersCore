package mod.lcwalker.minishaderscore.shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Matrix4f;

import org.lwjgl.BufferUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;

@SideOnly(Side.CLIENT)
public class ReShaderUniform {
	
    private int location;
    private final int count;
    private final int type;
    private final IntBuffer intBuffer;
    private final FloatBuffer floatBuffer;
    private final String name;
    private boolean dir;
    private final ReShaderManager manager;

    public ReShaderUniform(String name, int type, int count, ReShaderManager manager2) {
    	
        this.name = name;
        this.count = count;
        this.type = type;
        this.manager = manager2;

        if (type <= 3) {
        	
            this.intBuffer = BufferUtils.createIntBuffer(count);
            this.floatBuffer = null;
        }
        else {
        	
            this.intBuffer = null;
            this.floatBuffer = BufferUtils.createFloatBuffer(count);
        }
        
        this.location = -1;
        
        this.markDirty();
    }

    private void markDirty() {
    	
        this.dir = true;

        if (this.manager != null) {
            this.manager.fuckoff();
        }
    }

    public static int parseType(String s) {
    	
        byte by = -1;

        if (s.equals("int")) {
            by = 0;
        }
        else if (s.equals("float")) {
            by = 4;
        }
        else if (s.startsWith("matrix")) {
            if (s.endsWith("2x2")) {
                by = 8;
            }
            else if (s.endsWith("3x3")) {
                by = 9;
            }
            else if (s.endsWith("4x4")) {
                by = 10;
            }
        }

        return by;
    }

    public void setLocation(int l) {
        this.location = l;
    }

    public String getName() {
        return this.name;
    }

    public void floatBufferPut(float f) {
    	
        this.floatBuffer.position(0);
        this.floatBuffer.put(0, f);
        
        this.markDirty();
    }

    public void floatBufferPut(float f1, float f2) {
    	
        this.floatBuffer.position(0);
        this.floatBuffer.put(0, f1);
        this.floatBuffer.put(1, f2);
        
        this.markDirty();
    }

    public void floatBufferPut(float f1, float f2, float f3) {
    	
        this.floatBuffer.position(0);
        this.floatBuffer.put(0, f1);
        this.floatBuffer.put(1, f2);
        this.floatBuffer.put(2, f3);
        this.markDirty();
    }

    public void floatBufferPut(float f1, float f2, float f3, float f4) {
    	
        this.floatBuffer.position(0);
        this.floatBuffer.put(f1);
        this.floatBuffer.put(f2);
        this.floatBuffer.put(f3);
        this.floatBuffer.put(f4);
        this.floatBuffer.flip();
        this.markDirty();
    }

    public void floatBufferPut_(float f1, float f2, float f3, float f4) {
    	
        this.floatBuffer.position(0);

        if (this.type >= 4) {
            this.floatBuffer.put(0, f1);
        }

        if (this.type >= 5) {
            this.floatBuffer.put(1, f2);
        }

        if (this.type >= 6) {
            this.floatBuffer.put(2, f3);
        }

        if (this.type >= 7) {
            this.floatBuffer.put(3, f4);
        }

        this.markDirty();
    }

    public void intBufferPut(int i1, int i2, int i3, int i4) {
    	
        this.intBuffer.position(0);

        if (this.type >= 0) {
            this.intBuffer.put(0, i1);
        }

        if (this.type >= 1) {
            this.intBuffer.put(1, i2);
        }

        if (this.type >= 2) {
            this.intBuffer.put(2, i3);
        }

        if (this.type >= 3) {
            this.intBuffer.put(3, i4);
        }

        this.markDirty();
    }

    public void putfs(float[] fs) {
    	
        if (fs.length < this.count) {}
        else {
        	
            this.floatBuffer.position(0);
            this.floatBuffer.put(fs);
            this.floatBuffer.position(0);
            
            this.markDirty();
        }
    }

    public void putAll(float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        
    	this.floatBuffer.position(0);
        this.floatBuffer.put(0, f1);
        this.floatBuffer.put(1, f2);
        this.floatBuffer.put(2, f3);
        this.floatBuffer.put(3, f4);
        this.floatBuffer.put(4, f5);
        this.floatBuffer.put(5, f6);
        this.floatBuffer.put(6, f7);
        this.floatBuffer.put(7, f8);
        this.floatBuffer.put(8, f9);
        this.floatBuffer.put(9, f10);
        this.floatBuffer.put(10, f11);
        this.floatBuffer.put(11, f12);
        this.floatBuffer.put(12, f13);
        this.floatBuffer.put(13, f14);
        this.floatBuffer.put(14, f15);
        this.floatBuffer.put(15, f16);
        
        this.markDirty();
    }

    public void matrix(Matrix4f matrix4f) {
        this.putAll(matrix4f.m00, matrix4f.m01, matrix4f.m02, matrix4f.m03, matrix4f.m10, matrix4f.m11, matrix4f.m12, matrix4f.m13, matrix4f.m20, matrix4f.m21, matrix4f.m22, matrix4f.m23, matrix4f.m30, matrix4f.m31, matrix4f.m32, matrix4f.m33);
    }

    public void upload() {
    	
        if (!this.dir) {;}
        this.dir = false;

        if (this.type <= 3) {
            this.uploadInt();
        }
        else if (this.type <= 7) {
            this.uploadFloat();
        }
        else {
            if (this.type > 10) {
                return;
            }

            this.uploadFloatMatrix();
        }
    }

    private void uploadInt() {
    	
        switch (this.type) {
            case 0:
                OpenGlHelper.func_153181_a(this.location, this.intBuffer);
                break;
            case 1:
                OpenGlHelper.func_153182_b(this.location, this.intBuffer);
                break;
            case 2:
                OpenGlHelper.func_153192_c(this.location, this.intBuffer);
                break;
            case 3:
                OpenGlHelper.func_153162_d(this.location, this.intBuffer);
                break;
            default:  
        }
    }

    private void uploadFloat() {
    	
        switch (this.type) {
            case 4:
                OpenGlHelper.func_153168_a(this.location, this.floatBuffer);
                break;
            case 5:
                OpenGlHelper.func_153177_b(this.location, this.floatBuffer);
                break;
            case 6:
                OpenGlHelper.func_153191_c(this.location, this.floatBuffer);
                break;
            case 7:
                OpenGlHelper.func_153159_d(this.location, this.floatBuffer);
                break;
            default:
                
        }
    }

    private void uploadFloatMatrix() {
    	
        switch (this.type) {
            case 8:
                OpenGlHelper.func_153173_a(this.location, true, this.floatBuffer);
                break;
            case 9:
                OpenGlHelper.func_153189_b(this.location, true, this.floatBuffer);
                break;
            case 10:
                OpenGlHelper.func_153160_c(this.location, true, this.floatBuffer);
        }
    }
}