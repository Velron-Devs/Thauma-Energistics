package velrondev.thaumaenergistics.api.key;

import java.util.List;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import velrondev.thaumaenergistics.registry.TUUKeyTypes;

public final class EssentiaKey extends AEKey {

    public static final MapCodec<EssentiaKey> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(key -> key.aspect.location())
    ).apply(instance, EssentiaKey::of));

    private final ResourceKey<IAspect> aspect;

    private EssentiaKey(ResourceKey<IAspect> aspect) {
        this.aspect = aspect;
    }

    public static EssentiaKey of(ResourceLocation id) {
        return new EssentiaKey(ResourceKey.create(IAspect.REGISTRY_KEY, id));
    }

    public static EssentiaKey of(ResourceKey<IAspect> aspect) {
        return new EssentiaKey(aspect);
    }

    @Nullable
    public static EssentiaKey of(Holder<IAspect> aspect) {
        return aspect.unwrapKey().map(EssentiaKey::of).orElse(null);
    }

    public static boolean is(AEKey what) {
        return what instanceof EssentiaKey;
    }

    public ResourceKey<IAspect> aspect() {
        return aspect;
    }

    @Nullable
    public Holder<IAspect> holder(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(IAspect.REGISTRY_KEY).get(aspect).map(reference -> (Holder<IAspect>) reference).orElse(null);
    }

    @Override
    public AEKeyType getType() {
        return TUUKeyTypes.essentia();
    }

    @Override
    public AEKey dropSecondary() {
        return this;
    }

    @Override
    public CompoundTag toTag(HolderLookup.Provider registries) {
        return (CompoundTag) MAP_CODEC.codec().encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this).getOrThrow();
    }

    @Override
    public Object getPrimaryKey() {
        return aspect;
    }

    @Override
    public ResourceLocation getId() {
        return aspect.location();
    }

    @Override
    public void writeToPacket(RegistryFriendlyByteBuf data) {
        data.writeResourceLocation(aspect.location());
    }

    public static EssentiaKey fromPacket(RegistryFriendlyByteBuf data) {
        return of(data.readResourceLocation());
    }

    @Override
    protected Component computeDisplayName() {
        ResourceLocation id = aspect.location();
        return Component.translatable("aspect." + id.getNamespace() + "." + id.getPath());
    }

    @Override
    public void addDrops(long amount, List<ItemStack> drops, Level level, BlockPos pos) {
    }

    @Override
    public boolean hasComponents() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof EssentiaKey key && key.aspect == aspect;
    }

    @Override
    public int hashCode() {
        return aspect.hashCode();
    }

    @Override
    public String toString() {
        return aspect.location().toString();
    }
}
