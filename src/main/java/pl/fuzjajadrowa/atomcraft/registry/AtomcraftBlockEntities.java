package pl.fuzjajadrowa.atomcraft.registry;

import pl.fuzjajadrowa.atomcraft.block.entity.MachineBlockEntity;
import pl.fuzjajadrowa.atomcraft.Atomcraft;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AtomcraftBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Atomcraft.MODID);

    public static final Supplier<BlockEntityType<MachineBlockEntity>> MACHINE = BLOCK_ENTITIES.register("machine",
            () -> BlockEntityType.Builder.of(MachineBlockEntity::new, AtomcraftBlocks.GENERATOR.get(), AtomcraftBlocks.CRUSHER.get(), AtomcraftBlocks.ELECTRIC_FURNACE.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    private AtomcraftBlockEntities() {}
}