package velrondev.thaumaenergistics.behavior;

import java.util.ArrayList;
import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

public final class EssentiaTarget {

    private final List<BlockCapabilityCache<IEssentiaStorage, Direction>> caches = new ArrayList<>();

    public EssentiaTarget(ServerLevel level, BlockPos pos, Direction side) {
        List<Direction> order = new ArrayList<>();
        order.add(side);
        order.add(Direction.UP);
        for (Direction direction : Direction.values()) {
            if (!order.contains(direction)) {
                order.add(direction);
            }
        }
        for (Direction direction : order) {
            caches.add(BlockCapabilityCache.create(EssentiaCapabilities.STORAGE, level, pos, direction));
        }
    }

    public boolean available() {
        for (BlockCapabilityCache<IEssentiaStorage, Direction> cache : caches) {
            if (cache.getCapability() != null) {
                return true;
            }
        }
        return false;
    }

    public AspectList contents() {
        for (BlockCapabilityCache<IEssentiaStorage, Direction> cache : caches) {
            IEssentiaStorage storage = cache.getCapability();
            if (storage != null) {
                AspectList contents = storage.contents();
                if (!contents.isEmpty()) {
                    return contents;
                }
            }
        }
        return AspectList.EMPTY;
    }

    public int insert(Holder<IAspect> aspect, int amount, boolean simulate) {
        if (amount <= 0) {
            return 0;
        }
        for (BlockCapabilityCache<IEssentiaStorage, Direction> cache : caches) {
            IEssentiaStorage storage = cache.getCapability();
            if (storage == null) {
                continue;
            }
            int moved = storage.insert(aspect, amount, simulate);
            if (moved > 0) {
                return moved;
            }
        }
        return 0;
    }

    public int extract(Holder<IAspect> aspect, int amount, boolean simulate) {
        if (amount <= 0) {
            return 0;
        }
        for (BlockCapabilityCache<IEssentiaStorage, Direction> cache : caches) {
            IEssentiaStorage storage = cache.getCapability();
            if (storage == null) {
                continue;
            }
            int moved = storage.extract(aspect, amount, simulate);
            if (moved > 0) {
                return moved;
            }
        }
        return 0;
    }
}
