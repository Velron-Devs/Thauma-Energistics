package velrondev.thaumaenergistics.processor;

import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import net.minecraft.world.level.ItemLike;

public enum ProcessorKind {

    LOGIC("logic", "Logic", "lógico"),
    CALCULATION("calculation", "Calculation", "de cálculo"),
    ENGINEERING("engineering", "Engineering", "de ingeniería");

    private final String id;
    private final String english;
    private final String spanish;

    ProcessorKind(String id, String english, String spanish) {
        this.id = id;
        this.english = english;
        this.spanish = spanish;
    }

    public String id() {
        return id;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    public ItemLike press() {
        return definition(AEItems.LOGIC_PROCESSOR_PRESS, AEItems.CALCULATION_PROCESSOR_PRESS, AEItems.ENGINEERING_PROCESSOR_PRESS);
    }

    public ItemLike baseProcessor() {
        return definition(AEItems.LOGIC_PROCESSOR, AEItems.CALCULATION_PROCESSOR, AEItems.ENGINEERING_PROCESSOR);
    }

    private ItemLike definition(ItemDefinition<?> logic, ItemDefinition<?> calculation, ItemDefinition<?> engineering) {
        return switch (this) {
            case LOGIC -> logic;
            case CALCULATION -> calculation;
            case ENGINEERING -> engineering;
        };
    }
}
