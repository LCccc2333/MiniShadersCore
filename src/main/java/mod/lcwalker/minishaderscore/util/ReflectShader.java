package mod.lcwalker.minishaderscore.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cpw.mods.fml.relauncher.ReflectionHelper;
import mod.lcwalker.minishaderscore.ModMain;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ReflectShader {
	
	public static void re(String jsonName) {
		
		try {
			
			ReflectionHelper.getPrivateValue(EntityRenderer.class, null, "shaderResourceLocations", "field_147712_ad");
			
			Field f = ReflectionHelper.findField(EntityRenderer.class, "shaderResourceLocations", "field_147712_ad");
			Field f1 = ReflectionHelper.findField(EntityRenderer.class, "shaderCount", "field_147708_e");
		
			ResourceLocation[] res = getStaticFinalResourceLocation(f);
			ResourceLocation[] res1 = (ResourceLocation[]) Array.newInstance(ResourceLocation.class, 23);
			
			for (int i = 0; i < res.length; i++)
				Array.set(res1, i, res[i]);
			Array.set(res1, res1.length - 1, new ResourceLocation(String.format(ModMain.MODID + ":" + "shaders/post/%s.json", jsonName)));
			
			res = res1;
		
			majorization(f);
			setStaticFinal(f, res);
			EnumHelper.setFailsafeFieldValue(f1, null, res.length);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void majorization(Field field) throws Exception {
		
		Class<?> class1 = Class.forName("sun.reflect.UnsafeFieldAccessorImpl");
		Class<?> class2 = Class.forName("sun.reflect.UnsafeQualifiedStaticFieldAccessorImpl");
		
		Field f1 = Field.class.getDeclaredField("overrideFieldAccessor");
		f1.setAccessible(true);
		
		Object object = f1.get(field);

		Field f2 = class1.getDeclaredField("isFinal");
		f2.setAccessible(true);
		f2.set(object, false);

		Field f3 = class2.getDeclaredField("isReadOnly");
		f3.setAccessible(true);
		f3.set(object, false);
	}
	
	private static ResourceLocation[] getStaticFinalResourceLocation(Field field) throws Exception {
		
	    field.setAccessible(true);
	    
	    Field modifiers = Field.class.getDeclaredField("modifiers");
	    modifiers.setAccessible(true);
	    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	    
	    return (ResourceLocation[]) field.get(field);
	}
	  
	private static void setStaticFinal(Field field, Object object) throws Exception {
		
		field.setAccessible(true);
		
	    Field modifiers = Field.class.getDeclaredField("modifiers");
	    modifiers.setAccessible(true);
	    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	    
	    field.set(null, object);
	}
}
