package dev.cerus.jdasc.components;

public enum ComponentType {

    ACTION_ROW(1),
    BUTTON(2);

    private final int val;

    ComponentType(final int val) {
        this.val = val;
    }

    public static ComponentType getByVal(final int val) {
        for (final ComponentType value : values()) {
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
