package pl.fuzjajadrowa.atomcraft;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public final class ModContent {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Atomcraft.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Atomcraft.MODID);
    public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Atomcraft.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Atomcraft.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(Registries.RECIPE_TYPE, Atomcraft.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Atomcraft.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Atomcraft.MODID);

    public static final DeferredBlock<Block> URANIUM_ORE = BLOCKS.register("uranium_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final DeferredBlock<Block> DEEPSLATE_URANIUM_ORE = BLOCKS.register("deepslate_uranium_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredBlock<MachineBlock> GENERATOR = machine(MachineKind.GENERATOR);
    public static final DeferredBlock<MachineBlock> CRUSHER = machine(MachineKind.CRUSHER);
    public static final DeferredBlock<MachineBlock> ELECTRIC_FURNACE = machine(MachineKind.ELECTRIC_FURNACE);

    public static final DeferredItem<Item> RAW_URANIUM = ITEMS.registerSimpleItem("raw_uranium");
    public static final DeferredItem<Item> URANIUM_DUST = ITEMS.registerSimpleItem("uranium_dust");
    public static final DeferredItem<Item> URANIUM_INGOT = ITEMS.registerSimpleItem("uranium_ingot");
    public static final DeferredItem<Item> IRON_DUST = ITEMS.registerSimpleItem("iron_dust");
    public static final DeferredItem<Item> COPPER_DUST = ITEMS.registerSimpleItem("copper_dust");
    public static final DeferredItem<Item> GOLD_DUST = ITEMS.registerSimpleItem("gold_dust");
    public static final DeferredItem<Item> BASIC_CIRCUIT = ITEMS.registerSimpleItem("basic_circuit");
    public static final DeferredItem<Item> MACHINE_CASING = ITEMS.registerSimpleItem("machine_casing");

    public static final Supplier<BlockEntityType<MachineBlockEntity>> MACHINE_ENTITY = ENTITIES.register("machine",
            () -> BlockEntityType.Builder.of(MachineBlockEntity::new, GENERATOR.get(), CRUSHER.get(), ELECTRIC_FURNACE.get()).build(null));
    public static final Supplier<MenuType<MachineMenu>> MACHINE_MENU = MENUS.register("machine", () -> IMenuTypeExtension.create(MachineMenu::new));
    public static final Supplier<RecipeType<MachineRecipe>> MACHINE_RECIPE = RECIPES.register("machine", () -> new RecipeType<>() {
        @Override public String toString() { return "atomcraft:machine"; }
    });
    public static final Supplier<RecipeSerializer<MachineRecipe>> MACHINE_SERIALIZER = SERIALIZERS.register("machine", MachineRecipe.Serializer::new);

    static {
        ITEMS.registerSimpleBlockItem(URANIUM_ORE);
        ITEMS.registerSimpleBlockItem(DEEPSLATE_URANIUM_ORE);
        ITEMS.registerSimpleBlockItem(GENERATOR);
        ITEMS.registerSimpleBlockItem(CRUSHER);
        ITEMS.registerSimpleBlockItem(ELECTRIC_FURNACE);
        TABS.register("main", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.atomcraft"))
                .icon(() -> new ItemStack(URANIUM_INGOT.get()))
                .displayItems((parameters, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get()))).build());
    }
    private static DeferredBlock<MachineBlock> machine(MachineKind kind) {
        return BLOCKS.register(kind.id, () -> new MachineBlock(kind, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                .strength(3.5F).lightLevel(state -> state.getValue(MachineBlock.ACTIVE) ? 5 : 0)));
    }
    public static void register(IEventBus bus) {
        BLOCKS.register(bus); ITEMS.register(bus); ENTITIES.register(bus); MENUS.register(bus);
        RECIPES.register(bus); SERIALIZERS.register(bus); TABS.register(bus);
    }
    private ModContent() {}
}