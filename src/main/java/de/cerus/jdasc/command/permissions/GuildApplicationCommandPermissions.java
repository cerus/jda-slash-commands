package de.cerus.jdasc.command.permissions;

import java.util.List;

public class GuildApplicationCommandPermissions {

    private final long id;
    private final long applicationId;
    private final long guildId;
    private final List<ApplicationCommandPermissions> permissions;

    public GuildApplicationCommandPermissions(final long id, final long applicationId, final long guildId, final List<ApplicationCommandPermissions> permissions) {
        this.id = id;
        this.applicationId = applicationId;
        this.guildId = guildId;
        this.permissions = permissions;
    }

    public long getId() {
        return this.id;
    }

    public List<ApplicationCommandPermissions> getPermissions() {
        return this.permissions;
    }

    public long getApplicationId() {
        return this.applicationId;
    }

    public long getGuildId() {
        return this.guildId;
    }
}
