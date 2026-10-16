package nz.co.mirality.jadecolonies.minecolonies;

import com.minecolonies.api.colony.IVisitorViewData;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;

import java.util.ArrayList;
import java.util.List;

import static nz.co.mirality.jadecolonies.JadeColonies.ID;

/**
 * Shows information about a visitor's recruitment cost.
 */
class VisitorRecruitmentComponentProvider implements IEntityComponentProvider
{
    private static final Identifier UID = Identifier.fromNamespaceAndPath(ID, "colony.visitor_cost");

    private static final VisitorRecruitmentComponentProvider INSTANCE = new VisitorRecruitmentComponentProvider();

    public static VisitorRecruitmentComponentProvider getInstance()
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
        return -1000;
    }

    @Override
    public void appendTooltip(@NotNull final ITooltip tooltip,
                              @NotNull final EntityAccessor entityAccessor,
                              @NotNull final IPluginConfig pluginConfig)
    {
        if (entityAccessor.getEntity() instanceof final AbstractEntityCitizen citizen)
        {
            if (citizen.getCitizenDataView() instanceof final IVisitorViewData visitor)
            {
                final List<Element> elements = new ArrayList<>();
                elements.add(JadeUI.text(Component.translatable("jadecolonies.jade.colony.visitor_cost")));
                elements.add(JadeUI.spacer(4, 1));
                elements.add(JadeUI.smallItem(visitor.getRecruitCost()));
                tooltip.add(elements);
            }
        }
    }
}