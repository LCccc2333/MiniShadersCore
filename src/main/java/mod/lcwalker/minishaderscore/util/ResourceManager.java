package mod.lcwalker.minishaderscore.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ResourceManager implements IResourceManager {
	
	private static IResourceManager manager;
	
	public static final String shaders_post = "shaders/post/";
	public static final String shaders_program = "shaders/program/";
	
	public ResourceManager(IResourceManager manager) {
		this.manager = manager;
	}

	public Set getResourceDomains() {
		return this.manager.getResourceDomains();
	}

	public IResource getResource(ResourceLocation res) throws IOException {
		return this.manager.getResource(res);
	}

	public List getAllResources(ResourceLocation res) throws IOException {
		return this.manager.getAllResources(res);
	}
}
