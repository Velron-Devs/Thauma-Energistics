package velrondev.thaumaenergistics.datagen.providers;

import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import velrondev.thaumaenergistics.ThaumaEnergistics;

public class TEBlockLoot extends BlockLootSubProvider {

    public TEBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        getKnownBlocks().forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.listElements()
                .filter(holder -> holder.getKey().identifier().getNamespace().equals(ThaumaEnergistics.MOD_ID))
                .map(Holder::value)
                .toList();
    }
}
