package pl.fuzjajadrowa.atomcraft.menu;

import pl.fuzjajadrowa.atomcraft.registry.AtomcraftBlocks;
import pl.fuzjajadrowa.atomcraft.registry.AtomcraftMenus;
import pl.fuzjajadrowa.atomcraft.block.MachineKind;
import pl.fuzjajadrowa.atomcraft.block.entity.MachineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public final class MachineMenu extends AbstractContainerMenu {
    public final MachineKind kind;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public MachineMenu(int id, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        this(id, inventory, buffer.readBlockPos(), buffer.readEnum(MachineKind.class));
    }
    private MachineMenu(int id, Inventory inventory, BlockPos pos, MachineKind kind) {
        this(id, inventory, kind, new ItemStackHandler(2), new SimpleContainerData(6), ContainerLevelAccess.NULL);
    }
    public MachineMenu(int id, Inventory inventory, MachineBlockEntity machine) {
        this(id, inventory, machine.kind(), machine.inventory(), machine.data(), ContainerLevelAccess.create(machine.getLevel(), machine.getBlockPos()));
    }
    private MachineMenu(int id, Inventory inventory, MachineKind kind, ItemStackHandler items, ContainerData data, ContainerLevelAccess access) {
        super(AtomcraftMenus.MACHINE.get(), id);
        this.kind = kind; this.data = data; this.access = access;
        checkContainerDataCount(data, 6);
        addSlot(new SlotItemHandler(items, 0, 56, 35) {
            @Override public boolean mayPlace(ItemStack stack) { return kind != MachineKind.GENERATOR || stack.getBurnTime(RecipeType.SMELTING) > 0; }
        });
        addSlot(new SlotItemHandler(items, 1, 116, 35) { @Override public boolean mayPlace(ItemStack stack) { return false; } });
        for (int row = 0; row < 3; row++) for (int column = 0; column < 9; column++)
            addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
        for (int column = 0; column < 9; column++) addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        addDataSlots(data);
    }
    public int value(int index) { return data.get(index); }
    @Override public boolean stillValid(Player player) {
        return stillValid(access, player, switch (kind) {
            case GENERATOR -> AtomcraftBlocks.GENERATOR.get(); case CRUSHER -> AtomcraftBlocks.CRUSHER.get(); case ELECTRIC_FURNACE -> AtomcraftBlocks.ELECTRIC_FURNACE.get();
        });
    }
    @Override public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), original = stack.copy();
        if (index < 2) {
            if (!moveItemStackTo(stack, 2, 38, true)) return ItemStack.EMPTY;
        } else if (!slots.get(0).mayPlace(stack) || !moveItemStackTo(stack, 0, 1, false)) {
            if (index < 29) { if (!moveItemStackTo(stack, 29, 38, false)) return ItemStack.EMPTY; }
            else if (!moveItemStackTo(stack, 2, 29, false)) return ItemStack.EMPTY;
        }
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        slot.onTake(player, stack);
        return original;
    }
}