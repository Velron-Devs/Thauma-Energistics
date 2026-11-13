package velrondev.thaumaenergistics.api.key;

import com.mojang.serialization.MapCodec;

import appeng.api.stacks.AEKeyType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import velrondev.thaumaenergistics.TUU;
import velrondev.thaumaenergistics.config.TUUConfig;

public final class EssentiaKeyType extends AEKeyType {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TUU.MOD_ID, "essentia");

    public EssentiaKeyType() {
        super(ID, EssentiaKey.class, Component.translatable("gui.thaumaenergistics.essentia"));
    }

    @Override
    public MapCodec<? extends EssentiaKey> codec() {
        return EssentiaKey.MAP_CODEC;
    }

    @Override
    public EssentiaKey readFromPacket(RegistryFriendlyByteBuf input) {
        return EssentiaKey.fromPacket(input);
    }

    @Override
    public int getAmountPerOperation() {
        return TUUConfig.SPEC.isLoaded() ? TUUConfig.ESSENTIA_PER_OPERATION.get() : 8;
    }

    @Override
    public int getAmountPerByte() {
        return TUUConfig.SPEC.isLoaded() ? TUUConfig.ESSENTIA_PER_BYTE.get() : 8;
    }
}
