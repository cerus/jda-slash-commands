package de.cerus.jdasc.router;

import co.aikar.commands.JDACommandManager;
import de.cerus.jdasc.command.ApplicationCommand;
import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.util.FakeMessage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ACFRouter extends CommandRouter {

    private final JDACommandManager commandManager;

    public ACFRouter(final JDACommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void route(final JDA jda, final ApplicationCommand command, final Interaction interaction) {
        final Message message = new FakeMessage(false, this.makeCommandString(interaction), null, null, interaction);
        final MessageReceivedEvent event = new MessageReceivedEvent(jda, jda.getResponseTotal(), message);
        interaction.acknowledge(false).whenComplete((unused, throwable) -> {
            try {
                final Method method = this.commandManager.getClass().getDeclaredMethod("dispatchEvent", MessageReceivedEvent.class);
                method.setAccessible(true);
                method.invoke(this.commandManager, event);
            } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                System.err.println("Failed to route interaction to ACF");
            }
        });
    }

}
