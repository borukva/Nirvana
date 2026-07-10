package io.github.fabricators_of_create.porting_lib.data;

import net.minecraft.resources.ResourceLocation;

public class PortingLibDataHack {
    public static String prefixNamespace(ResourceLocation location) {
        return location.getNamespace();
    }
}
