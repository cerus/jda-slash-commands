package de.cerus.jdasc.interaction.response;

public class InteractionResponse {

    private final InteractionResponseType type;
    private final InteractionApplicationCommandCallbackData data;

    public InteractionResponse(final InteractionResponseType type, final InteractionApplicationCommandCallbackData data) {
        this.type = type;
        this.data = data;
    }

    public InteractionResponseType getType() {
        return this.type;
    }

    public InteractionApplicationCommandCallbackData getData() {
        return this.data;
    }

}
