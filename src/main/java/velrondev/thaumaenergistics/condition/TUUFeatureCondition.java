package velrondev.thaumaenergistics.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.neoforged.neoforge.common.conditions.ICondition;
import velrondev.thaumaenergistics.config.TUUCompatConfig;

public record TUUFeatureCondition(String feature) implements ICondition {

    public static final MapCodec<TUUFeatureCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("feature").forGetter(TUUFeatureCondition::feature)
    ).apply(instance, TUUFeatureCondition::new));

    @Override
    public boolean test(IContext context) {
        return TUUCompatConfig.enabled(feature);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
