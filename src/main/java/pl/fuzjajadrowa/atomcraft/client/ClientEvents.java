package pl.fuzjajadrowa.atomcraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import pl.fuzjajadrowa.atomcraft.*;

@EventBusSubscriber(modid = Atomcraft.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    @SubscribeEvent public static void screens(RegisterMenuScreensEvent event) { event.register(ModContent.MACHINE_MENU.get(), MachineScreen::new); }
}
