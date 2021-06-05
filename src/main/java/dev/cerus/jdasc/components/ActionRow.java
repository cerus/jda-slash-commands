package dev.cerus.jdasc.components;

import java.util.Arrays;
import java.util.List;

public class ActionRow implements Component {

    private final List<Component> components;
    private final ComponentType type = ComponentType.ACTION_ROW;

    public ActionRow(final List<Component> components) {
        this.components = components;
    }

    public static ActionRow of(final Component... components) {
        return new ActionRow(Arrays.asList(components));
    }

    public List<Component> getComponents() {
        return this.components;
    }

    @Override
    public ComponentType getType() {
        return this.type;
    }

}
