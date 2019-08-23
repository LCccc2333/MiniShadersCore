package mod.lcwalker.minishaderscore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mod.lcwalker.minishaderscore.util.UseShader;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
    public void preInit(FMLPreInitializationEvent event) {

    	SomeEvents.preInit(event);
    	
    	FMLCommonHandler.instance().bus().register(new UseShader());
    	MinecraftForge.EVENT_BUS.register(new UseShader());
    }
    
    public void init(FMLInitializationEvent event) {
    	
    }
    
    public void postInit(FMLPostInitializationEvent event) {
    	SomeEvents.postInit(event);
    }
}
