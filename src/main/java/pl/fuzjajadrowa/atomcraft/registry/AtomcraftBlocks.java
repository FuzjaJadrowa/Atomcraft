package pl.fuzjajadrowa.atomcraft.registry;

import pl.fuzjajadrowa.atomcraft.block.MachineKind;
import pl.fuzjajadrowa.atomcraft.block.MachineBlock;
import pl.fuzjajadrowa.atomcraft.Atomcraft;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AtomcraftBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Atomcraft.MODID);

    public static final DeferredBlock<Block> URANIUM_ORE = BLOCKS.register("uranium_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final DeferredBlock<Block> DEEPSLATE_URANIUM_ORE = BLOCKS.register("deepslate_uranium_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredBlock<MachineBlock> GENERATOR = machine(MachineKind.GENERATOR);
    public static final DeferredBlock<MachineBlock> CRUSHER = machine(MachineKind.CRUSHER);
    public static final DeferredBlock<MachineBlock> ELECTRIC_FURNACE = machine(MachineKind.ELECTRIC_FURNACE);

    private static DeferredBlock<MachineBlock> machine(MachineKind kind) {
        return BLOCKS.register(kind.id, () -> new MachineBlock(kind, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                .strength(3.5F).lightLevel(state -> state.getValue(MachineBlock.ACTIVE) ? 5 : 0)));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    private AtomcraftBlocks() {}
}