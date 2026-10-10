package pl.fuzjajadrowa.atomcraft.registry;

import pl.fuzjajadrowa.atomcraft.Atomcraft;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AtomcraftCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Atomcraft.MODID);

    public static final Supplier<CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.atomcraft"))
                .icon(() -> new ItemStack(AtomcraftItems.URANIUM_INGOT.get()))
                .displayItems((parameters, output) -> AtomcraftItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()))).build());

    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }

    private AtomcraftCreativeTabs() {}
}