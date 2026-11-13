package velrondev.thaumaenergistics.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.neoforged.neoforge.common.conditions.ICondition;
import velrondev.thaumaenergistics.config.TECompatConfig;

public record TEFeatureCondition(String feature) implements ICondition {

    public static final MapCodec<TEFeatureCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("feature").forGetter(TEFeatureCondition::feature)
    ).apply(instance, TEFeatureCondition::new));

    @Override
    public boolean test(IContext context) {
        return TECompatConfig.enabled(feature);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
