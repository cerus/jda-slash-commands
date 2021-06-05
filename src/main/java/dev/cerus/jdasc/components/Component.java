package dev.cerus.jdasc.components;

public interface Component {

    ComponentType getType();

    default <T extends Component> T cast() {
        return (T) this;
    }

}
