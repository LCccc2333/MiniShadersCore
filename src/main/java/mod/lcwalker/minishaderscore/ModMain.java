package mod.lcwalker.minishaderscore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModMain.MODID, name = ModMain.MODID, version = ModMain.VERSION)
public class ModMain {
	
    public static final String MODID = "minishaderscore";
    public static final String NAME = "MiniShadersCore";
    public static final String VERSION = "s1";
    
    @SidedProxy(clientSide = "mod.lcwalker.minishaderscore.ClientProxy", serverSide = "mod.lcwalker.minishaderscore.CommonProxy")
    public static CommonProxy proxy;
    
    @Instance(ModMain.MODID)
    public static ModMain instance = new ModMain();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
}
