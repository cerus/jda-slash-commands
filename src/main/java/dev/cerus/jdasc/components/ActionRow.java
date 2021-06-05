package dev.cerus.jdasc.components;

import java.util.List;

public class ActionRow implements Component {

    private final List<Component> components;
    private final ComponentType type = ComponentType.ACTION_ROW;

    public ActionRow(final List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    @Override
    public ComponentType getType() {
        return this.type;
    }

}
