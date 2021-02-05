# jda-slash-commands

[Slash commands](https://discord.com/developers/docs/interactions/slash-commands) for [JDA](https://github.com/DV8FromTheWorld/JDA)

## Note

This library is only a temporary solution until JDA officially supports slash commands. I decided against contributing to JDA because I don't want to
mess up their code base.

This library is also lacking some documentation. If you have any questions feel free to open a issue or contact me.

## Features

- Commands
- Interactions
- Interactions responses
- [Command routers](https://github.com/cerus/jda-slash-commands/wiki/Command-Routers) (Automatically route interactions to your command framework)

## Missing features

- Deleting / editing interaction responses
- Sending / deleting / editing followup messages

## Example

<details>
  <summary>Installation with Maven</summary>

  ```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.cerus</groupId>
        <artifactId>jda-slash-commands</artifactId>
        <version>47f3ff2ac6</version>
    </dependency>
</dependencies>
  ```

</details>

```java
JDASlashCommands.initialize(jda, BOT_TOKEN, APPLICATION_ID);
JDASlashCommands.submitGlobalCommand(new CommandBuilder()
        .name("test-command") // Set command name to '/test-command'
        .desc("My cool test command")
        .option(new CommandBuilder.SubCommandGroupBuilder()
                .name("some-group") // Specify a group that can hold multiple sub commands
                .desc("This is a wonderful group")
                .option(new CommandBuilder.SubCommandBuilder()
                        .name("hello") // Specify sub command 'hello' (/test-command some-group hello)
                        .desc("Greet a user")
                        .option(new ApplicationCommandOption(
                                ApplicationCommandOptionType.USER,
                                "user", // Note the lower case name - Names have to be lower case or else things could break
                                "Specify a user to greet",
                                true
                        ))
                        .build())
                .option(new CommandBuilder.SubCommandBuilder()
                        .name("animal") // Specify sub command 'animal' (/test-command some-group animal)
                        .desc("Show a animal picture")
                        .choices( // Only allow certain values: Cat, Dog and Platypus
                                ApplicationCommandOptionType.STRING, // Specify type of the choice: STRING or INTEGER
                                "animal", // Note the lower case name - Names have to be lower case or else things could break
                                "Specify the animal",
                                new ApplicationCommandOptionChoice("Cat", "cat"),
                                new ApplicationCommandOptionChoice("Dog", "cat"),
                                new ApplicationCommandOptionChoice("Platypus", "platypus")
                        )
                        .build())
                .build())
        .build(), new ApplicationCommandListener() {
    
    @Override
    public void onInteraction(final Interaction interaction) {
        System.out.println("We got an interaction! Yay!");
    }
    
    @Override
    public void handleArgument(final Interaction interaction, final String argumentName, final InteractionResponseOption option) {
        switch (argumentName) {
            case "user":
                interaction.respond(false, "Hello, " + jda.getUserById(Long.parseLong(option.getValue())).getAsMention());
                break;
            case "animal":
                interaction.respond(false, "Here's your imaginary picture of a " + option.getValue());
                break;
        }
    }
    
});
```

<details>
  <summary>Pictures</summary>

![Group](https://i.imgur.com/qL7nL8q.png)

![Animal](https://i.imgur.com/qm8xLI6.png)

![Hello](https://i.imgur.com/5JMolqh.png)

![Hello Response](https://i.imgur.com/bXng3nG.png)

![Animal Response](https://i.imgur.com/6sEOMBk.png)

</details>
