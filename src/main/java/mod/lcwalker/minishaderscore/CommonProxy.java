package mod.lcwalker.minishaderscore;

import mod.lcwalker.minishaderscore.util.UseShader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
