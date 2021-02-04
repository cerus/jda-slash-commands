import de.cerus.jdasc.JDASlashCommands;
import de.cerus.jdasc.command.ApplicationCommand;
import de.cerus.jdasc.command.ApplicationCommandListener;
import de.cerus.jdasc.command.ApplicationCommandOption;
import de.cerus.jdasc.command.ApplicationCommandOptionType;
import de.cerus.jdasc.command.CommandBuilder;
import de.cerus.jdasc.interaction.Interaction;
import de.cerus.jdasc.interaction.response.InteractionResponseOption;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Test {

    public static void main(final String[] args) throws LoginException, InterruptedException {
        final String token = "ODA2OTI2ODY2NzMxNDk5NTQx.YBwjNQ.4a_XtQENno7EUIi8TsSNFXiT1wo";
        final String id = "806926866731499541";

        final JDA jda = JDABuilder.create(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES)
                .setRawEventsEnabled(true).build().awaitReady();
        JDASlashCommands.initialize(jda, token, id);

        final ApplicationCommand testCmd = new CommandBuilder()
                .name("command2")
                .desc("Desc")
                .option(new CommandBuilder.SubCommandGroupBuilder("group1")
                        .desc("Desc group1")
                        .option(new CommandBuilder.SubCommandBuilder()
                                .name("sub1")
                                .desc("Desc sub1")
                                .option(new ApplicationCommandOption(
                                        ApplicationCommandOptionType.USER,
                                        "arg1",
                                        "Desc arg1",
                                        true,
                                        null,
                                        new ArrayList<>()
                                ))
                                .build())
                        .option(new CommandBuilder.SubCommandBuilder()
                                .name("aaa")
                                .desc("aaaaaaa")
                                .option(new ApplicationCommandOption(
                                        ApplicationCommandOptionType.CHANNEL,
                                        "arg2",
                                        "Desc arg2",
                                        true,
                                        null,
                                        new ArrayList<>()
                                ))
                                .build())
                        .build())
                .build();

        JDASlashCommands.submitGuildCommand(testCmd, jda.getGuilds().get(0), new ApplicationCommandListener() {
            @Override
            public void onInteraction(final Interaction interaction) {
            }

            @Override
            public void handleArgument(final Interaction interaction, final String argumentName, final InteractionResponseOption option) {
                System.out.println(argumentName);
                if (argumentName.equals("arg1")) {
                    System.out.println("Arg1 !!!");
                }
            }
        }).whenComplete((aLong, throwable) -> {
            System.out.println(throwable.getMessage());
        });
        JDASlashCommands.submitGlobalCommand(new ApplicationCommand("cool-test-command", "Never gonna give you up"), interaction -> {
            interaction.respond(false, Arrays.asList(
                    new EmbedBuilder()
                            .setTitle("Title")
                            .setDescription("Desc")
                            .setColor(Color.PINK)
                            .setTimestamp(Instant.now())
                            .build(),
                    new EmbedBuilder()
                            .setTitle("Title")
                            .setDescription("Desc")
                            .setColor(Color.GREEN)
                            .setTimestamp(Instant.now())
                            .build()
            )).whenComplete((unused, throwable) -> {
                System.err.println(throwable.getMessage());
            });
        }).whenComplete((aLong, throwable) -> {
            if (throwable != null) {
                System.err.println(throwable.getClass().getSimpleName());
                return;
            }

            JDASlashCommands.getGlobalCommands().whenComplete((commands, t) -> {
                if (t != null) {
                    System.err.println(t.getClass().getSimpleName());
                    System.err.println(t.getMessage());
                    return;
                }

                System.out.println("global cmds:");
                for (final ApplicationCommand command : commands) {
                    System.out.println(command.toString());
                }
            });
        });
    }

}
