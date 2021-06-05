package dev.cerus.jdasc.components;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.dv8tion.jda.api.entities.Emote;

public class Button implements Component {

    private final ComponentType type = ComponentType.BUTTON;
    private final Style style;
    private final String label;
    private final PartialEmoji emoji;
    @SerializedName("custom_id")
    private final String customId;
    private final String url;
    private final boolean disabled;

    public Button(final Style style, final String label, final PartialEmoji emoji, final String customId, final String url, final boolean disabled) {
        this.style = style;
        this.label = label;
        this.emoji = emoji;
        this.customId = customId;
        this.url = url;
        this.disabled = disabled;
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
