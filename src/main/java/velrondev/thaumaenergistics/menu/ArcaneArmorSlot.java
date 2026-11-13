package velrondev.thaumaenergistics.menu;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ArcaneArmorSlot extends Slot {

    private static final Identifier[] EMPTY_ICONS = {
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
            InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            InventoryMenu.EMPTY_ARMOR_SLOT_HELMET
    };

    private final EquipmentSlot equipment;
    private final Player player;

    public ArcaneArmorSlot(Inventory inventory, EquipmentSlot equipment) {
        super(inventory, 36 + equipment.getIndex(), 0, 0);
        this.equipment = equipment;
        this.player = inventory.player;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.canEquip(equipment, player);
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack stack = getItem();
        return (stack.isEmpty() || player.isCreative() || !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) && super.mayPickup(player);
    }

    @Override
    public Identifier getNoItemIcon() {
        return EMPTY_ICONS[equipment.getIndex()];
    }
}
