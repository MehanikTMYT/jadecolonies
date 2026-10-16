package nz.co.mirality.jadecolonies.minecolonies;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * If the building has a custom name, displays the custom name as the main block name, with the name & level below.
 * Otherwise, replaces the block name with the name & level of the building (i.e. appends the level).
 * <p>
 * The server-side data this reads is prepared by {@link BuildingNameServerDataProvider} -- Jade (since Minecraft
 * 1.21.6) requires server data providers and client component providers to be separate objects, even though the
 * two here share the same UID and represent one logical feature.
 */
class BuildingNameComponentProvider implements IBlockComponentProvider
{
    private static final Identifier UID = BuildingNameServerDataProvider.UID;
    private static final String BUILDING_NAME = BuildingNameServerDataProvider.BUILDING_NAME;
    private static final BuildingNameComponentProvider INSTANCE = new BuildingNameComponentProvider();

    public static BuildingNameComponentProvider getInstance()
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
    public boolean isRequired()
    {
        return true;
    }

    @Override
    public int getDefaultPriority()
    {
        return -3000;
    }

    @Override
    public void appendTooltip(@NotNull final ITooltip tooltip,
                              @NotNull final BlockAccessor blockAccessor,
                              @NotNull final IPluginConfig pluginConfig)
    {
        final HolderLookup.Provider lookupProvider = blockAccessor.getLevel().registryAccess();
        final CompoundTag data = blockAccessor.getServerData();

        if (data.contains(BUILDING_NAME))
        {
            final Component name = decode(data.get(BUILDING_NAME), lookupProvider);
            tooltip.add(name);
        }
    }

    // Component.Serializer.fromJson(String, HolderLookup.Provider) was removed; Component is now
    // decoded via its registry-aware Codec (ComponentSerialization.CODEC), directly from NBT (NbtOps)
    // instead of round-tripping through a JSON string.
    private static Component decode(final Tag tag, final HolderLookup.Provider lookupProvider)
    {
        return ComponentSerialization.CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).getOrThrow();
    }
}
