package velrondev.thaumaenergistics.compat.extendedae.infinity;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.taint.item.ItemEssentiaCrystal;
import com.leclowndu93150.thaumaturge.registry.TTBlocks;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record InfiniteTarget(Predicate<AEKey> matches, Function<HolderLookup.Provider, AEKey> resolver, Component name) {

    public static InfiniteTarget arcaneStone() {
        return new InfiniteTarget(
                key -> key instanceof AEItemKey itemKey && itemKey.getItem() == arcaneStoneItem(),
                registries -> AEItemKey.of(arcaneStoneItem()),
                Component.translatable("block.thaumaturge.stone_arcane"));
    }

    public static InfiniteTarget visCrystal(ResourceKey<IAspect> aspect) {
        return new InfiniteTarget(
                key -> key instanceof AEItemKey itemKey && itemKey.getItem() instanceof ItemEssentiaCrystal && isAspect(itemKey, aspect),
                registries -> crystalKey(registries, aspect),
                Component.translatable("item.thaumaturge.essentia_crystal",
                        Component.translatable("aspect." + aspect.identifier().getNamespace() + "." + aspect.identifier().getPath())));
    }

    private static Item arcaneStoneItem() {
        return TTBlocks.STONE_ARCANE.get().asItem();
    }

    private static boolean isAspect(AEItemKey key, ResourceKey<IAspect> aspect) {
        Holder<IAspect> holder = ItemEssentiaCrystal.aspectOf(key.toStack());
        return holder != null && holder.is(aspect);
    }

    @Nullable
    private static AEKey crystalKey(HolderLookup.Provider registries, ResourceKey<IAspect> aspect) {
        Optional<? extends Holder<IAspect>> holder = registries.lookupOrThrow(IAspect.REGISTRY_KEY).get(aspect);
        if (holder.isEmpty()) {
            return null;
        }
        ItemStack stack = new ItemStack(TTItems.ESSENTIA_CRYSTAL.get());
        stack.set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(holder.get(), 1));
        return AEItemKey.of(stack);
    }
}
