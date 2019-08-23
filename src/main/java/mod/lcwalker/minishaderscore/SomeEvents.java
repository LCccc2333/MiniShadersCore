package mod.lcwalker.minishaderscore;

import mod.lcwalker.minishaderscore.shader.ReShaderGroup;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
