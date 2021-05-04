package de.cerus.jdasc.command.permissions;

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
}
