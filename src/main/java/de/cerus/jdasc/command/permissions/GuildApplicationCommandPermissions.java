package de.cerus.jdasc.command.permissions;

import java.util.List;

public class GuildApplicationCommandPermissions {
    private final long id;
    private final long applicationId;
    private final long guildId;
    private final List<ApplicationCommandPermissions> permissions;

    public GuildApplicationCommandPermissions(long id, long applicationId, long guildId, List<ApplicationCommandPermissions> permissions) {
        this.id = id;
        this.applicationId = applicationId;
        this.guildId = guildId;
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public List<ApplicationCommandPermissions> getPermissions() {
        return permissions;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public long getGuildId() {
        return guildId;
    }
}
