package de.cerus.jdasc.router;

import co.aikar.commands.JDACommandManager;
import de.cerus.jdasc.command.ApplicationCommand;
import de.cerus.jdasc.interaction.Interaction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.DataMessage;
import org.jetbrains.annotations.NotNull;

public class ACFRouter extends CommandRouter {

    private final JDACommandManager commandManager;

    public ACFRouter(final JDACommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void route(final JDA jda, final ApplicationCommand command, final Interaction interaction) {
        final Message message = new DataMessage(false, this.makePath(interaction), null, null,
                null, new String[0], new String[0]) {
            @NotNull
            @Override
            public Guild getGuild() {
                return interaction.getGuild();
            }

            @NotNull
            @Override
            public TextChannel getTextChannel() {
                return (TextChannel) this.getChannel();
            }

            @NotNull
            @Override
            public MessageChannel getChannel() {
                return interaction.getChannel();
            }

            @NotNull
            @Override
            public User getAuthor() {
                return interaction.getMember().getUser();
            }

            @Override
            public Member getMember() {
                return interaction.getMember();
            }
        };

        final MessageReceivedEvent event = new MessageReceivedEvent(jda, jda.getResponseTotal(), message);
        try {
            final Method method = this.commandManager.getClass().getDeclaredMethod("dispatchEvent", MessageReceivedEvent.class);
            method.setAccessible(true);
            method.invoke(this.commandManager, event);
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.err.println("Failed to route interaction to ACF");
        }
    }

}
