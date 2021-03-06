package dev.cerus.jdasc.components;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import dev.cerus.jdasc.JDASlashCommands;
import dev.cerus.jdasc.interaction.Interaction;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Emote;

public class Button implements Component {

    private final ComponentType type = ComponentType.BUTTON;
    private final Style style;
    private final String label;
    private final PartialEmoji emoji;
    private final String url;
    @SerializedName("custom_id")
    private final String customId;
    private final boolean disabled;

    public Button(final Style style, final String label, final PartialEmoji emoji, final String customId, final String url, final boolean disabled) {
        this.style = style;
        this.label = label;
        this.emoji = emoji;
        this.customId = customId;
        this.url = url;
        this.disabled = disabled;
    }

    public static Button emojiLinkButton(final Style style, final String label, final String url, final PartialEmoji emoji, final boolean disabled) {
        return new Button(style, label, emoji, null, url, disabled);
    }

    public static Button emojiLinkButton(final Style style, final String label, final String url, final PartialEmoji emoji) {
        return new Button(style, label, emoji, null, url, false);
    }

    public static Button linkButton(final Style style, final String label, final String url) {
        return linkButton(style, label, false, url);
    }

    public static Button linkButton(final Style style, final String label, final boolean disabled, final String url) {
        return new Button(style, label, null, null, url, disabled);
    }

    public static Button normalButton(final Style style, final String label, final String customId) {
        return normalButton(style, label, customId, false);
    }

    public static Button normalButton(final Style style, final String label, final String customId, final boolean disabled) {
        return new Button(style, label, null, customId, null, disabled);
    }

    public static Button emojiButton(final Style style, final String label, final String customId, final PartialEmoji emoji) {
        return emojiButton(style, label, customId, false, emoji);
    }

    public static Button emojiButton(final Style style, final String label, final String customId, final boolean disabled, final PartialEmoji emoji) {
        return new Button(style, label, emoji, customId, null, disabled);
    }

    public Button oneTimeListener(final Consumer<Interaction> callback) {
        if (this.style != Style.LINK) {
            JDASlashCommands.addOneTimeComponentListener(this.getCustomId(), callback::accept);
        }
        return this;
    }

    public Button listener(final Consumer<Interaction> callback) {
        if (this.style != Style.LINK) {
            JDASlashCommands.addComponentListener(interaction -> {
                final Component clickedComponent = interaction.getClickedComponent();
                if (clickedComponent instanceof Button
                        && ((Button) clickedComponent).getCustomId().equals(this.getCustomId())) {
                    callback.accept(interaction);
                }
            });
        }
        return this;
    }

    public Style getStyle() {
        return this.style;
    }

    public String getLabel() {
        return this.label;
    }

    public PartialEmoji getEmoji() {
        return this.emoji;
    }

    public String getCustomId() {
        return this.customId;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public ComponentType getType() {
        return this.type;
    }

    public enum Style {
        PRIMARY(1),
        SECONDARY(2),
        SUCCESS(3),
        DANGER(4),
        LINK(5);

        private final int val;

        Style(final int val) {
            this.val = val;
        }

        public static Style getByVal(final int val) {
            for (final Style value : values()) {
                if (value.val == val) {
                    return value;
                }
            }
            return null;
        }

        public int getVal() {
            return this.val;
        }

    }

    public static class PartialEmoji {

        private final String id;
        private final String name;
        private final boolean animated;

        public PartialEmoji(final String id, final String name, final boolean animated) {
            this.id = id;
            this.name = name;
            this.animated = animated;
        }

        public PartialEmoji(final JsonObject object) {
            this(
                    object.has("id")
                            ? object.get("id").getAsString()
                            : null,
                    object.get("name").getAsString(),
                    object.has("animated")
                            && object.get("animated").getAsBoolean()
            );
        }

        public static PartialEmoji getDefaultEmoji(final String unicode) {
            return new PartialEmoji(null, unicode, false);
        }

        public static PartialEmoji getEmojiFromEmote(final Emote emote) {
            return new PartialEmoji(emote.getId(), emote.getName(), emote.isAnimated());
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public boolean isAnimated() {
            return this.animated;
        }

    }

}
