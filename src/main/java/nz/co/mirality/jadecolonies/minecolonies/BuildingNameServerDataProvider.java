package nz.co.mirality.jadecolonies.minecolonies;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.IRSComponent;
import com.minecolonies.api.tileentities.AbstractTileEntityColonyBuilding;
import com.minecolonies.core.tileentities.TileEntityDecorationController;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

import static nz.co.mirality.jadecolonies.JadeColonies.ID;

/**
 * Prepares the server-side data (custom name / name &amp; level) that {@link BuildingNameComponentProvider}
 * displays on the client. Jade (since Minecraft 1.21.6) requires server data providers and client component
 * providers to be separate objects, even when -- as here -- they share a UID and represent one logical feature.
 */
class BuildingNameServerDataProvider implements IServerDataProvider<BlockAccessor>
{
    static final Identifier UID = Identifier.fromNamespaceAndPath(ID, "colony.hut");
    static final String OVERRIDE_NAME = "givenName";
    static final String BUILDING_NAME = UID.toString();
    private static final BuildingNameServerDataProvider INSTANCE = new BuildingNameServerDataProvider();

    public static BuildingNameServerDataProvider getInstance()
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
        return -3000;
    }

    @Override
    public void appendServerData(@NotNull final CompoundTag compoundTag,
                                 @NotNull final BlockAccessor blockAccessor)
    {
        final HolderLookup.Provider lookupProvider = blockAccessor.getLevel().registryAccess();
        if (blockAccessor.getBlockEntity() instanceof final AbstractTileEntityColonyBuilding entity)
        {
            final IBuilding building = entity.getBuilding();
            if (building == null || building instanceof IRSComponent) { return; }

            final String name = building.getCustomName();
            final MutableComponent nameLevel = Component.translatable("%s %s",
                    Component.translatable(building.getBuildingType().getTranslationKey()),
                    Integer.toString(building.getBuildingLevel()));

            if (name.isEmpty())
            {
                compoundTag.put(OVERRIDE_NAME, encode(nameLevel, lookupProvider));
            }
            else
            {
                compoundTag.put(OVERRIDE_NAME, encode(Component.literal(name), lookupProvider));
                compoundTag.put(BUILDING_NAME, encode(nameLevel.withStyle(ChatFormatting.GRAY), lookupProvider));
            }
        }
        else if (blockAccessor.getBlockEntity() instanceof final TileEntityDecorationController deco)
        {
            compoundTag.put(BUILDING_NAME, encode(Component.literal(deco.getBlueprintPath()), lookupProvider));
        }
    }

    // Component.Serializer.toJson(Component, HolderLookup.Provider) was removed; Component is now
    // encoded via its registry-aware Codec (ComponentSerialization.CODEC), directly to NBT (NbtOps)
    // instead of round-tripping through a JSON string.
    static Tag encode(final Component component, final HolderLookup.Provider lookupProvider)
    {
        return ComponentSerialization.CODEC.encodeStart(lookupProvider.createSerializationContext(NbtOps.INSTANCE), component).getOrThrow();
    }
}
