package pl.fuzjajadrowa.atomcraft.energy;

import java.util.function.IntSupplier;
import net.neoforged.neoforge.energy.IEnergyStorage;

public final class MachineEnergy implements IEnergyStorage {
    private final boolean generator;
    private final IntSupplier capacity, transfer;
    private final Runnable changed;
    private int stored;
    public MachineEnergy(boolean generator, IntSupplier capacity, IntSupplier transfer, Runnable changed) {
        this.generator = generator; this.capacity = capacity; this.transfer = transfer; this.changed = changed;
    }
    @Override public int receiveEnergy(int amount, boolean simulate) {
        return generator ? 0 : add(Math.min(Math.max(0, amount), transfer.getAsInt()), simulate);
    }
    @Override public int extractEnergy(int amount, boolean simulate) {
        return generator ? remove(Math.min(Math.max(0, amount), transfer.getAsInt()), simulate) : 0;
    }
    public int add(int amount, boolean simulate) {
        int accepted = Math.min(Math.max(0, amount), Math.max(0, getMaxEnergyStored() - getEnergyStored()));
        if (!simulate && accepted > 0) { stored = getEnergyStored() + accepted; changed.run(); }
        return accepted;
    }
    public int remove(int amount, boolean simulate) {
        int removed = Math.min(Math.max(0, amount), getEnergyStored());
        if (!simulate && removed > 0) { stored = getEnergyStored() - removed; changed.run(); }
        return removed;
    }
    public void load(int value) { stored = Math.max(0, value); }
    @Override public int getEnergyStored() { return Math.min(stored, getMaxEnergyStored()); }
    @Override public int getMaxEnergyStored() { return capacity.getAsInt(); }
    @Override public boolean canExtract() { return generator; }
    @Override public boolean canReceive() { return !generator; }
}