package velrondev.thaumaenergistics.compat.extendedae.client;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;

public final class InfiniteCellClientRegistries {

    private InfiniteCellClientRegistries() {
    }

    @Nullable
    public static HolderLookup.Provider current() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.level != null ? minecraft.level.registryAccess() : null;
    }
}
