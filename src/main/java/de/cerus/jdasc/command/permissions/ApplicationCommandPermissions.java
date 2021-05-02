package de.cerus.jdasc.command.permissions;

public class ApplicationCommandPermissions {
    private final long id;
    private final ApplicationCommandPermissionType type;
    private final boolean permission;

    public ApplicationCommandPermissions(long id, ApplicationCommandPermissionType type, boolean permission) {
        this.id = id;
        this.type = type;
        this.permission = permission;
    }

    public long getId() {
        return id;
    }

    public ApplicationCommandPermissionType getType() {
        return type;
    }

    public boolean isPermission() {
        return permission;
    }
}
