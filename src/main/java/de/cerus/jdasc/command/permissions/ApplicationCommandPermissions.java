package de.cerus.jdasc.command.permissions;

import com.google.common.base.Objects;

public class ApplicationCommandPermissions {

    private final long id;
    private final ApplicationCommandPermissionType type;
    private final boolean permission;

    public ApplicationCommandPermissions(final long id, final ApplicationCommandPermissionType type, final boolean permission) {
        this.id = id;
        this.type = type;
        this.permission = permission;
    }

    public long getId() {
        return this.id;
    }

    public ApplicationCommandPermissionType getType() {
        return this.type;
    }

    public boolean isPermission() {
        return this.permission;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationCommandPermissions)) {
            return false;
        }
        final ApplicationCommandPermissions that = (ApplicationCommandPermissions) o;
        return this.getId() == that.getId() && this.isPermission() == that.isPermission() && this.getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.getType(), this.isPermission());
    }
}
