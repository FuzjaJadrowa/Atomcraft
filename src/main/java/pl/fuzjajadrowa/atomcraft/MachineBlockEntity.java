package pl.fuzjajadrowa.atomcraft;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

public final class MachineBlockEntity extends BlockEntity implements MenuProvider {
    private final MachineKind kind;
    private final MachineEnergy energy;
    private int progress, duration, burnRemaining, burnTotal, decayTicks, transferCursor;
    private ItemStack processInput = ItemStack.EMPTY, processOutput = ItemStack.EMPTY;
    private String processRecipe = "";
    private final ItemStackHandler items = new ItemStackHandler(2) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }
        @Override public boolean isItemValid(int slot, ItemStack stack) {
            return slot == 0 && (kind != MachineKind.GENERATOR || stack.getBurnTime(RecipeType.SMELTING) > 0);
        }
    };
    private final ContainerData data = new ContainerData() {
        @Override public int get(int index) {
            return switch (index) {
                case 0 -> energy.getEnergyStored(); case 1 -> energy.getMaxEnergyStored();
                case 2 -> progress; case 3 -> duration; case 4 -> burnRemaining; case 5 -> burnTotal;
                default -> 0;
            };
        }
        @Override public void set(int index, int value) {}
        @Override public int getCount() { return 6; }
    };

    public MachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.MACHINE_ENTITY.get(), pos, state);
        kind = ((MachineBlock) state.getBlock()).kind();
        energy = new MachineEnergy(kind == MachineKind.GENERATOR,
                () -> kind == MachineKind.GENERATOR ? AtomcraftConfig.GENERATOR_CAPACITY.get() : AtomcraftConfig.MACHINE_CAPACITY.get(),
                () -> kind == MachineKind.GENERATOR ? AtomcraftConfig.GENERATOR_TRANSFER.get() : AtomcraftConfig.MACHINE_RECEIVE.get(), this::setChanged);
    }
    public MachineEnergy energy() { return energy; }
    public MachineKind kind() { return kind; }
    public ItemStackHandler inventory() { return items; }
    public ContainerData data() { return data; }
    @Override public Component getDisplayName() { return Component.translatable("block.atomcraft." + kind.id); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) { return new MachineMenu(id, inventory, this); }

    public static void tick(Level level, BlockPos pos, BlockState state, MachineBlockEntity machine) {
        boolean active = machine.kind == MachineKind.GENERATOR ? machine.tickGenerator() : machine.tickProcessor();
        if (state.getValue(MachineBlock.ACTIVE) != active) level.setBlock(pos, state.setValue(MachineBlock.ACTIVE, active), 3);
    }

    private boolean tickGenerator() {
        if (burnRemaining == 0 && energy.getEnergyStored() < energy.getMaxEnergyStored()) {
            ItemStack fuel = items.getStackInSlot(0);
            int burnTime = fuel.getBurnTime(RecipeType.SMELTING);
            ItemStack remainder = fuel.getCraftingRemainingItem();
            if (burnTime > 0 && fitsOutput(remainder)) {
                burnRemaining = burnTotal = burnTime;
                items.extractItem(0, 1, false);
                putOutput(remainder);
                setChanged();
            }
        }
        boolean burning = burnRemaining > 0;
        if (burning) {
            burnRemaining--;
            energy.add(AtomcraftConfig.GENERATOR_RATE.get(), false);
            setChanged();
        }
        distributeEnergy();
        return burning;
    }

    /** Share the budget first; then redistribute unused shares. Rotate rounding priority. */
    private void distributeEnergy() {
        int budget = Math.min(energy.getEnergyStored(), AtomcraftConfig.GENERATOR_TRANSFER.get());
        if (budget == 0) return;
        Direction[] directions = Direction.values();
        List<IEnergyStorage> receivers = new ArrayList<>();
        for (int i = 0; i < directions.length; i++) {
            Direction side = directions[(i + transferCursor) % directions.length];
            BlockPos target = worldPosition.relative(side);
            if (!level.hasChunkAt(target)) continue;
            IEnergyStorage receiver = level.getCapability(Capabilities.EnergyStorage.BLOCK, target, side.getOpposite());
            if (receiver != null && receiver.canReceive() && receiver.receiveEnergy(budget, true) > 0) receivers.add(receiver);
        }
        transferCursor = (transferCursor + 1) % directions.length;
        int remaining = budget;
        for (int i = 0; i < receivers.size(); i++) {
            int share = budget / receivers.size() + (i < budget % receivers.size() ? 1 : 0);
            remaining -= transfer(receivers.get(i), share);
        }
        for (IEnergyStorage receiver : receivers) {
            if (remaining == 0) break;
            remaining -= transfer(receiver, remaining);
        }
    }
    private int transfer(IEnergyStorage receiver, int limit) {
        int available = Math.min(limit, energy.getEnergyStored());
        int accepted = Math.clamp(receiver.receiveEnergy(available, false), 0, available);
        energy.remove(accepted, false);
        return accepted;
    }

    private record Process(String id, ItemStack result, int ticks) {}
    private Process findProcess(ItemStack input) {
        if (input.isEmpty()) return null;
        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        // Stable priority when multiple datapack recipes match the same input.
        var custom = level.getRecipeManager().getAllRecipesFor(ModContent.MACHINE_RECIPE.get()).stream()
                .filter(holder -> holder.value().machine().equals(kind.id) && holder.value().matches(recipeInput, level))
                .sorted(java.util.Comparator.comparing(holder -> holder.id().toString())).findFirst();
        int defaultTime = kind == MachineKind.CRUSHER ? AtomcraftConfig.CRUSHER_TIME.get() : AtomcraftConfig.FURNACE_TIME.get();
        if (custom.isPresent()) {
            var holder = custom.get();
            return new Process(holder.id().toString(), holder.value().assemble(recipeInput, level.registryAccess()),
                    holder.value().time() == 0 ? defaultTime : holder.value().time());
        }
        if (kind == MachineKind.ELECTRIC_FURNACE) {
            var smelting = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeInput, level);
            if (smelting.isPresent()) {
                var holder = smelting.get();
                return new Process(holder.id().toString(), holder.value().assemble(recipeInput, level.registryAccess()), defaultTime);
            }
        }
        return null;
    }
    private boolean tickProcessor() {
        ItemStack input = items.getStackInSlot(0);
        Process process = findProcess(input);
        if (process == null || process.result.isEmpty()) { resetProcess(); return false; }
        if (!processRecipe.equals(process.id) || !ItemStack.isSameItemSameComponents(processInput, input)
                || !ItemStack.matches(processOutput, process.result) || duration != process.ticks) {
            resetProcess();
            processInput = input.copyWithCount(1); processOutput = process.result.copy();
            processRecipe = process.id; duration = process.ticks;
            setChanged();
        }
        // Blocked output takes precedence over loss of power: paused progress is preserved.
        if (!fitsOutput(process.result)) return false;
        int cost = AtomcraftConfig.MACHINE_RATE.get();
        if (energy.getEnergyStored() < cost) {
            if (progress > 0 && ++decayTicks >= 2) { progress--; decayTicks = 0; setChanged(); }
            return false;
        }
        decayTicks = 0;
        energy.remove(cost, false);
        progress++;
        if (progress >= duration) {
            items.extractItem(0, 1, false);
            putOutput(process.result);
            progress = 0;
        }
        setChanged();
        return true;
    }
    private void resetProcess() {
        if (progress != 0 || !processRecipe.isEmpty()) setChanged();
        progress = duration = decayTicks = 0;
        processInput = processOutput = ItemStack.EMPTY; processRecipe = "";
    }
    private boolean fitsOutput(ItemStack result) {
        if (result.isEmpty()) return true;
        ItemStack output = items.getStackInSlot(1);
        int limit = Math.min(items.getSlotLimit(1), result.getMaxStackSize());
        return result.getCount() <= limit && (output.isEmpty() ||
                ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= limit);
    }
    private void putOutput(ItemStack result) {
        if (result.isEmpty()) return;
        ItemStack output = items.getStackInSlot(1).copy();
        if (output.isEmpty()) output = result.copy(); else output.grow(result.getCount());
        items.setStackInSlot(1, output);
    }
    public void dropInventory() {
        if (level == null || level.isClientSide) return;
        for (int slot = 0; slot < items.getSlots(); slot++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), items.getStackInSlot(slot));
            items.setStackInSlot(slot, ItemStack.EMPTY);
        }
    }
    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", items.serializeNBT(registries)); tag.putInt("Energy", energy.getEnergyStored());
        tag.putInt("Progress", progress); tag.putInt("Duration", duration); tag.putInt("DecayTicks", decayTicks);
        tag.putInt("BurnRemaining", burnRemaining); tag.putInt("BurnTotal", burnTotal);
        tag.putString("ProcessRecipe", processRecipe);
        if (!processInput.isEmpty()) tag.put("ProcessInput", processInput.save(registries));
        if (!processOutput.isEmpty()) tag.put("ProcessOutput", processOutput.save(registries));
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.deserializeNBT(registries, tag.getCompound("Inventory")); energy.load(tag.getInt("Energy"));
        duration = Math.clamp(tag.getInt("Duration"), 0, 10000000);
        progress = Math.clamp(tag.getInt("Progress"), 0, duration); decayTicks = Math.clamp(tag.getInt("DecayTicks"), 0, 1);
        burnTotal = Math.max(0, tag.getInt("BurnTotal")); burnRemaining = Math.clamp(tag.getInt("BurnRemaining"), 0, burnTotal);
        processRecipe = tag.getString("ProcessRecipe");
        processInput = ItemStack.parseOptional(registries, tag.getCompound("ProcessInput"));
        processOutput = ItemStack.parseOptional(registries, tag.getCompound("ProcessOutput"));
    }
}
