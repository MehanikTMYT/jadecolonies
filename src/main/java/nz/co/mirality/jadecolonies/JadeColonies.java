package nz.co.mirality.jadecolonies;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import nz.co.mirality.jadecolonies.domum_ornamentum.DomumOrnamentumPlugin;
import nz.co.mirality.jadecolonies.minecolonies.MineColoniesPlugin;
import snownee.jade.api.IWailaPlugin;

import java.util.function.Supplier;

@Mod(JadeColonies.ID)
public class JadeColonies
{
    public static final String ID = "jadecolonies";

    public static final String JADE_ID = snownee.jade.Jade.ID;
    public static final String DOMUM_ID = com.ldtteam.domumornamentum.util.Constants.MOD_ID;
    public static final String COLONY_ID = com.minecolonies.api.util.constant.Constants.MOD_ID;

    // The @EventBusSubscriber(bus = Bus.MOD) annotation attribute was removed from NeoForge's
    // EventBusSubscriber (it now only takes modid/value(Dist[]), for the game event bus). Mod-bus
    // listeners are now registered explicitly from the @Mod class's constructor via the
    // FML-injected IEventBus parameter, matching the pattern already proven in
    // ../warehouseWorkshop's main mod class.
    public JadeColonies(final IEventBus modEventBus)
    {
        modEventBus.addListener(JadeColonies::init);
    }

    public static void init(final FMLCommonSetupEvent event)
    {
        if (ModList.get().isLoaded(JADE_ID))
        {
            addPlugins();
        }
    }

    private static void addPlugins()
    {
        addPlugin(DOMUM_ID, DomumOrnamentumPlugin::new);
        addPlugin(COLONY_ID, MineColoniesPlugin::new);
    }

    private static void addPlugin(final String id, final Supplier<IWailaPlugin> supplier)
    {
        if (ModList.get().isLoaded(id))
        {
            JadePlugin.PLUGINS.add(supplier.get());
        }
    }
}
