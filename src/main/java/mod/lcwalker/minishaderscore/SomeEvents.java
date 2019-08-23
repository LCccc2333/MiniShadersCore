package mod.lcwalker.minishaderscore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mod.lcwalker.minishaderscore.shader.ReShaderGroup;

public class SomeEvents {

	private static boolean b1 = true;

	public static void preInit(FMLPreInitializationEvent event) {
		
		if (b1) {
			
	    	ReShaderGroup.addList();
	    	
	    	b1 = !b1;
		}
	}
	
	public static void postInit(FMLPostInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new SomeEvents());
	}
}
