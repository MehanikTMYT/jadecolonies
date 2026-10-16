package nz.co.mirality.jadecolonies.minecolonies;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * Adds the style pack that the building comes from.
 * <p>
 * The server-side data this reads is prepared by {@link BuildingPackServerDataProvider} -- Jade (since Minecraft
 * 1.21.6) requires server data providers and client component providers to be separate objects, even though the
 * two here share the same UID and represent one logical feature.
 */
class BuildingPackComponentProvider implements IBlockComponentProvider
{
    private static final Identifier UID = BuildingPackServerDataProvider.UID;
    private static final String PACK_NAME = BuildingPackServerDataProvider.PACK_NAME;
    private static final BuildingPackComponentProvider INSTANCE = new BuildingPackComponentProvider();

    public static BuildingPackComponentProvider getInstance()
    {
        return INSTANCE;
    }

    @NotNull
    @Override
    public Identifier getUid()
    {
        return UID;
    }

    @Override
    public int getDefaultPriority()
    {
        return -2999;
    }

    @Override
    public void appendTooltip(@NotNull final ITooltip tooltip,
                              @NotNull final BlockAccessor blockAccessor,
                              @NotNull final IPluginConfig pluginConfig)
    {
        final String name = blockAccessor.getServerData().getStringOr(PACK_NAME, "");

        if (!name.isEmpty())
        {
            tooltip.add(Component.literal(name).withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}
