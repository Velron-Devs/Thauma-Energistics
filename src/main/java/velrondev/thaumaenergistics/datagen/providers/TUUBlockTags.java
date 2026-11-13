package velrondev.thaumaenergistics.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.compat.extendedae.ExtendedAeCompat;
import velrondev.thaumaenergistics.core.TUUCompat;
import velrondev.thaumaenergistics.processor.ProcessorMetal;
import velrondev.thaumaenergistics.registry.TUUBlocks;

public class TUUBlockTags extends BlockTagsProvider {

    public TUUBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existingFileHelper) {
        super(output, lookup, TUU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(TUUBlocks.INFUSION_PROVIDER.get());
        if (TUUCompat.extendedAeActive()) {
            for (ProcessorMetal metal : ProcessorMetal.values()) {
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(ExtendedAeCompat.crafter(metal).get())
                        .add(ExtendedAeCompat.pattern(metal).get())
                        .add(ExtendedAeCompat.speed(metal).get());
            }
        }
    }
}
