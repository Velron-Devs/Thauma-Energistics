package velrondev.thaumaenergistics.behavior;

import com.google.common.primitives.Ints;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.core.localization.GuiText;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.api.key.EssentiaKey;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

public class EssentiaExternalStorage implements ExternalStorageStrategy {

    private final ServerLevel level;
    private final EssentiaTarget target;

    public EssentiaExternalStorage(ServerLevel level, BlockPos pos, Direction side) {
        this.level = level;
        this.target = new EssentiaTarget(level, pos, side);
    }

    @Nullable
    @Override
    public MEStorage createWrapper(boolean extractableOnly, Runnable injectOrExtractCallback) {
        if (!target.available()) {
            return null;
        }
        return new Wrapper(extractableOnly, injectOrExtractCallback);
    }

    private final class Wrapper implements MEStorage {

        private final boolean extractableOnly;
        private final Runnable callback;

        private Wrapper(boolean extractableOnly, Runnable callback) {
            this.extractableOnly = extractableOnly;
            this.callback = callback;
        }

        private Holder<IAspect> resolve(AEKey what) {
            return what instanceof EssentiaKey key ? key.holder(level.registryAccess()) : null;
        }

        @Override
        public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
            Holder<IAspect> aspect = resolve(what);
            if (extractableOnly || aspect == null) {
                return 0;
            }
            int inserted = target.insert(aspect, Ints.saturatedCast(amount), mode.isSimulate());
            if (inserted > 0 && mode == Actionable.MODULATE) {
                callback.run();
            }
            return inserted;
        }

        @Override
        public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
            Holder<IAspect> aspect = resolve(what);
            if (aspect == null) {
                return 0;
            }
            int extracted = target.extract(aspect, Ints.saturatedCast(amount), mode.isSimulate());
            if (extracted > 0 && mode == Actionable.MODULATE) {
                callback.run();
            }
            return extracted;
        }

        @Override
        public void getAvailableStacks(KeyCounter out) {
            for (AspectInstance entry : target.contents().entries()) {
                EssentiaKey key = EssentiaKey.of(entry.aspect());
                if (key != null && entry.amount() > 0) {
                    out.add(key, entry.amount());
                }
            }
        }

        @Override
        public Component getDescription() {
            return GuiText.ExternalStorage.text(TUUKeyTypes.essentia().getDescription());
        }
    }
}
