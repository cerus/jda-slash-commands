package de.cerus.jdasc.command.permissions;

import java.util.List;

public class GuildApplicationCommandPermissions {
    private final long id;
    private final long application_id;
    private final long guild_id;
    private final List<ApplicationCommandPermissions> permissions;

    public GuildApplicationCommandPermissions(long id, long application_id, long guild_id, List<ApplicationCommandPermissions> permissions) {
        this.id = id;
        this.application_id = application_id;
        this.guild_id = guild_id;
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public List<ApplicationCommandPermissions> getPermissions() {
        return permissions;
    }

    public long getApplicationId() {
        return application_id;
    }

    public long getGuildId() {
        return guild_id;
    }
}
