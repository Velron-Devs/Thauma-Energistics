package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import velrondev.thaumaenergistics.ThaumaEnergistics;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.core.TECompat;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TEBlocks;

public class TEBlockTags extends BlockTagsProvider {

    public TEBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, ThaumaEnergistics.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(TEBlocks.INFUSION_PROVIDER.get());
        if (TECompat.extendedAeActive()) {
            for (ProcessorMetal metal : ProcessorMetal.values()) {
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(ExtendedAeCompat.crafter(metal).get())
                        .add(ExtendedAeCompat.pattern(metal).get())
                        .add(ExtendedAeCompat.speed(metal).get());
            }
        }
    }
}
