package nz.co.mirality.jadecolonies.minecolonies;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.IRSComponent;
import com.minecolonies.api.tileentities.AbstractTileEntityColonyBuilding;
import com.minecolonies.core.tileentities.TileEntityDecorationController;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

import static nz.co.mirality.jadecolonies.JadeColonies.ID;

/**
 * Prepares the server-side data (structure pack name) that {@link BuildingPackComponentProvider} displays on the
 * client. Jade (since Minecraft 1.21.6) requires server data providers and client component providers to be
 * separate objects, even when -- as here -- they share a UID and represent one logical feature.
 */
class BuildingPackServerDataProvider implements IServerDataProvider<BlockAccessor>
{
    static final Identifier UID = Identifier.fromNamespaceAndPath(ID, "colony.pack");
    static final String PACK_NAME = UID.toString();
    private static final BuildingPackServerDataProvider INSTANCE = new BuildingPackServerDataProvider();

    public static BuildingPackServerDataProvider getInstance()
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
    public void appendServerData(@NotNull final CompoundTag compoundTag,
                                 @NotNull final BlockAccessor blockAccessor)
    {
        if (blockAccessor.getBlockEntity() instanceof final AbstractTileEntityColonyBuilding entity)
        {
            final IBuilding building = entity.getBuilding();
            if (building == null || building instanceof IRSComponent) { return; }

            compoundTag.putString(PACK_NAME, building.getStructurePack());
        }
        else if (blockAccessor.getBlockEntity() instanceof final TileEntityDecorationController deco)
        {
            compoundTag.putString(PACK_NAME, deco.getPackName());
        }
    }
}
