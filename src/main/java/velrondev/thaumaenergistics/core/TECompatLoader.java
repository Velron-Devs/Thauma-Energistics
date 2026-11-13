package velrondev.thaumaenergistics.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.bus.api.IEventBus;
import velrondev.thaumaenergistics.datagen.providers.TERecipeProvider;

public final class TECompatLoader {

    private TECompatLoader() {
    }

    public static void register(String className, IEventBus bus) {
        try {
            Method method = Class.forName(className).getMethod("register", IEventBus.class);
            method.invoke(null, bus);
        } catch (ClassNotFoundException ignored) {
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void models(String className, BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        try {
            Method method = Class.forName(className).getMethod("register", BlockModelGenerators.class, ItemModelGenerators.class);
            method.invoke(null, blockModels, itemModels);
        } catch (ClassNotFoundException ignored) {
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static TERecipeProvider.RecipeFactory recipes(String className) {
        return (registries, output) -> create(className, registries, output);
    }

    private static TERecipeProvider create(String className, HolderLookup.Provider registries, RecipeOutput output) {
        try {
            Constructor<?> constructor = Class.forName(className).getConstructor(HolderLookup.Provider.class, RecipeOutput.class);
            return (TERecipeProvider) constructor.newInstance(registries, output);
        } catch (ClassNotFoundException ignored) {
            return new TERecipeProvider(registries, output) {
                @Override
                protected void buildRecipes() {
                }
            };
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
