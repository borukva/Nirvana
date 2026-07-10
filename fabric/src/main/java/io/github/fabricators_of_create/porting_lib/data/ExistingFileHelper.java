package io.github.fabricators_of_create.porting_lib.data;

public class ExistingFileHelper {

    public interface IResourceType {}

    public static class ResourceType implements IResourceType {
        public ResourceType(net.minecraft.server.packs.PackType type, String suffix, String prefix) {}
    }

    public boolean exists(net.minecraft.resources.ResourceLocation loc, IResourceType type) {
        return true;
    }

    public boolean exists(net.minecraft.resources.ResourceLocation loc, net.minecraft.server.packs.PackType packType, String suffix, String prefix) {
        return true;
    }
}
