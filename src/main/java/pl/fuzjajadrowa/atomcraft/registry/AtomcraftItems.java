package pl.fuzjajadrowa.atomcraft.registry;

import pl.fuzjajadrowa.atomcraft.Atomcraft;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AtomcraftItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Atomcraft.MODID);

    public static final DeferredItem<Item> RAW_URANIUM = ITEMS.registerSimpleItem("raw_uranium");
    public static final DeferredItem<Item> URANIUM_DUST = ITEMS.registerSimpleItem("uranium_dust");
    public static final DeferredItem<Item> URANIUM_INGOT = ITEMS.registerSimpleItem("uranium_ingot");
    public static final DeferredItem<Item> IRON_DUST = ITEMS.registerSimpleItem("iron_dust");
    public static final DeferredItem<Item> COPPER_DUST = ITEMS.registerSimpleItem("copper_dust");
    public static final DeferredItem<Item> GOLD_DUST = ITEMS.registerSimpleItem("gold_dust");
    public static final DeferredItem<Item> BASIC_CIRCUIT = ITEMS.registerSimpleItem("basic_circuit");
    public static final DeferredItem<Item> MACHINE_CASING = ITEMS.registerSimpleItem("machine_casing");

    public static final DeferredItem<BlockItem> URANIUM_ORE = ITEMS.registerSimpleBlockItem(AtomcraftBlocks.URANIUM_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_URANIUM_ORE = ITEMS.registerSimpleBlockItem(AtomcraftBlocks.DEEPSLATE_URANIUM_ORE);
    public static final DeferredItem<BlockItem> GENERATOR = ITEMS.registerSimpleBlockItem(AtomcraftBlocks.GENERATOR);
    public static final DeferredItem<BlockItem> CRUSHER = ITEMS.registerSimpleBlockItem(AtomcraftBlocks.CRUSHER);
    public static final DeferredItem<BlockItem> ELECTRIC_FURNACE = ITEMS.registerSimpleBlockItem(AtomcraftBlocks.ELECTRIC_FURNACE);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private AtomcraftItems() {}
}