package mod.lcwalker.minishaderscore.shader;

import javax.vecmath.Matrix4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ReShaderDefault extends ReShaderUniform {

    public ReShaderDefault() {
        super("dummy", 4, 1, (ReShaderManager) null);
    }

    public void floatBufferPut(float f1) {}

    public void floatBufferPut(float f1, float f2) {}

    public void floatBufferPut(float f1, float f2, float f3) {}

    public void floatBufferPut(float f1, float f2, float f3, float f4) {}

    public void floatBufferPut_(float f1, float f2, float f3, float f4) {}

    public void intBufferPut(int i1, int i2, int i3, int i4) {}

    public void putfs(float[] fs) {}

    public void putAll(float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {}

    public void matrix(Matrix4f matrix4f) {}
}