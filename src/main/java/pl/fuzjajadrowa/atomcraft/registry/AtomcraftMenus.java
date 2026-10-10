package pl.fuzjajadrowa.atomcraft.registry;

import pl.fuzjajadrowa.atomcraft.menu.MachineMenu;
import pl.fuzjajadrowa.atomcraft.Atomcraft;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AtomcraftMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Atomcraft.MODID);

    public static final Supplier<MenuType<MachineMenu>> MACHINE = MENUS.register("machine", () -> IMenuTypeExtension.create(MachineMenu::new));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }

    private AtomcraftMenus() {}
}