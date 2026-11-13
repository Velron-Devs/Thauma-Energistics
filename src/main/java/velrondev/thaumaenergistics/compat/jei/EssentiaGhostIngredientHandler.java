package velrondev.thaumaenergistics.compat.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.compat.jei.ingredient.AspectIngredientType;

import appeng.api.stacks.GenericStack;
import appeng.client.gui.AEBaseScreen;
import appeng.client.integrations.itemlists.DropTarget;
import appeng.client.integrations.itemlists.DropTargets;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.Holder;
import velrondev.thaumaenergistics.api.key.EssentiaKey;

public class EssentiaGhostIngredientHandler<T extends AEBaseScreen<?>> implements IGhostIngredientHandler<T> {

    private final Predicate<T> screenFilter;

    public EssentiaGhostIngredientHandler(Predicate<T> screenFilter) {
        this.screenFilter = screenFilter;
    }

    @Override
    public <I> List<Target<I>> getTargetsTyped(T gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();
        if (!screenFilter.test(gui)) {
            return targets;
        }
        GenericStack stack = toStack(ingredient.getIngredient());
        if (stack == null) {
            return targets;
        }
        for (DropTarget dropTarget : DropTargets.getTargets(gui)) {
            if (dropTarget.canDrop(stack)) {
                targets.add(new SlotTarget<>(dropTarget, stack));
            }
        }
        return targets;
    }

    @Override
    public void onComplete() {
    }

    private static GenericStack toStack(Object ingredient) {
        Holder<IAspect> aspect = null;
        if (ingredient instanceof AspectInstance instance) {
            aspect = instance.aspect();
        } else if (AspectIngredientType.INSTANCE.getIngredientBaseClass().isInstance(ingredient)) {
            @SuppressWarnings("unchecked")
            Holder<IAspect> holder = (Holder<IAspect>) ingredient;
            aspect = holder;
        }
        if (aspect == null) {
            return null;
        }
        EssentiaKey key = EssentiaKey.of(aspect);
        return key == null ? null : new GenericStack(key, 1);
    }

    private record SlotTarget<I>(DropTarget dropTarget, GenericStack stack) implements Target<I> {

        @Override
        public Rect2i getArea() {
            return dropTarget.area();
        }

        @Override
        public void accept(I ingredient) {
            dropTarget.drop(stack);
        }
    }
}
