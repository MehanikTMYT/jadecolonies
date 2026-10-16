package nz.co.mirality.jadecolonies.minecolonies;

import com.ldtteam.blockui.UiRenderMacros;
import com.minecolonies.api.util.constant.WindowConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.ui.Element;

/**
 * Renders a colonist's gender.
 */
class GenderElement extends Element
{
    private static final GenderElement MALE = new GenderElement(WindowConstants.FEMALE_SOURCE.replace("female", "male"));
    private static final GenderElement FEMALE = new GenderElement(WindowConstants.FEMALE_SOURCE);

    /**
     * Gets a {@link GenderElement} for the specified gender.
     * @param isFemale true for female, false for male.
     * @return the corresponding element.
     */
    public static Element get(final boolean isFemale)
    {
        return isFemale ? FEMALE : MALE;
    }

    private final Identifier textureLocation;

    private GenderElement(final String texture)
    {
        this.textureLocation = Identifier.parse(texture);
        this.width = 15;
        this.height = 15;
    }

    @Override
    public void extractRenderState(@NotNull final GuiGraphicsExtractor guiGraphicsExtractor, final int x, final int y, final float partialTick)
    {
        UiRenderMacros.blit(guiGraphicsExtractor, this.textureLocation, x, y, 15, 15, 0, 0, 30, 30, 30, 30);
    }

    @NotNull
    @Override
    public Component getNarration()
    {
        return Component.translatable("jadecolonies.jade.colony.gender." + (this == FEMALE ? "female" : "male"));
    }
}
