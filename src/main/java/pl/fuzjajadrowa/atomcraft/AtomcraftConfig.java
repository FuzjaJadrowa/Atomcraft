package pl.fuzjajadrowa.atomcraft;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class AtomcraftConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.IntValue GENERATOR_RATE = integer("generatorRate", 40, "Generator production in FE/t.");
    public static final ModConfigSpec.IntValue GENERATOR_CAPACITY = integer("generatorCapacity", 32000, "Generator buffer in FE.");
    public static final ModConfigSpec.IntValue GENERATOR_TRANSFER = integer("generatorTransfer", 80, "Total generator transfer budget in FE/t.");
    public static final ModConfigSpec.IntValue MACHINE_CAPACITY = integer("machineCapacity", 16000, "Machine buffer in FE.");
    public static final ModConfigSpec.IntValue MACHINE_RECEIVE = integer("machineReceive", 80, "Maximum FE received per request.");
    public static final ModConfigSpec.IntValue MACHINE_RATE = integer("machineRate", 20, "Machine consumption in FE/t.");
    public static final ModConfigSpec.IntValue CRUSHER_TIME = integer("crusherTime", 200, "Default crushing duration in ticks.");
    public static final ModConfigSpec.IntValue FURNACE_TIME = integer("furnaceTime", 100, "Default electric smelting duration in ticks.");
    public static final ModConfigSpec SPEC = BUILDER.build();

    private static ModConfigSpec.IntValue integer(String name, int value, String comment) {
        return BUILDER.comment(comment).defineInRange(name, value, 1, 10000000);
    }
    private AtomcraftConfig() {}
}