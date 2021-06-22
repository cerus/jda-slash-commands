[![Maven Central](https://img.shields.io/maven-central/v/dev.cerus/jda-slash-commands)](https://search.maven.org/artifact/dev.cerus/jda-slash-commands) ![GitHub](https://img.shields.io/github/license/cerus/jda-slash-commands) ![GitHub Sponsors](https://img.shields.io/github/sponsors/cerus)

# Notice
JDA now officially supports interactions (slash commands and buttons) in release 4.3.0. That means that this library will no longer receive updates. 

---

<br><br>

# jda-slash-commands

[Slash commands](https://discord.com/developers/docs/interactions/slash-commands) for [JDA](https://github.com/DV8FromTheWorld/JDA)

This library is only a temporary solution until JDA officially supports slash commands. I decided against contributing to JDA because I don't want to
mess up their code base.

This library is also lacking some documentation. If you have any questions feel free to open an issue.

## Features

- Commands
- Interactions
- Interactions responses
- Followup messages
- Command permissions
- [Command routers](https://github.com/cerus/jda-slash-commands/wiki/Command-Routers) (Automatically route interactions to your command framework)
- Message components

## Example

Slash Commands:

```java
public class MyBot {

    private static final String BOT_TOKEN = "********";
    private static final String APPLICATION_ID = "12345654321";

    public static void main(String[] args) {
        JDA jda = JDABuilder.setRawEventsEnabled(true).createDefault(BOT_TOKEN).build();
        initCommands(jda);
    }

    public void initCommands(JDA jda) {
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

        });
    }

}
```

<details>
  <summary>Pictures</summary>

![Group](https://i.imgur.com/qL7nL8q.png)

![Animal](https://i.imgur.com/qm8xLI6.png)

![Hello](https://i.imgur.com/5JMolqh.png)

![Hello Response](https://i.imgur.com/bXng3nG.png)

![Animal Response](https://i.imgur.com/6sEOMBk.png)

</details>

Message components:

```java
public class MyBot {

    private static final String BOT_TOKEN = "********";
    private static final String APPLICATION_ID = "12345654321";

    public static void main(String[] args) {
        JDA jda = JDABuilder.setRawEventsEnabled(true).createDefault(BOT_TOKEN).build();
        initCommands(jda);
    }

    public void initCommands(JDA jda) {
        JDASlashCommands.initialize(jda, BOT_TOKEN, APPLICATION_ID);

        // Add a component listener that will get called
        // every time a component is clicked
        JDASlashCommands.addComponentListener(interaction -> {
            final Component clickedComponent = interaction.getClickedComponent();
            if (clickedComponent instanceof Button) {
                final Button button = clickedComponent.cast();
                interaction.respond("You clicked " + button.getLabel() + "!");
            }
        });

        // Add a one time component listener that will 
        // get called when a button with the provided 
        // button id is clicked
        JDASlashCommands.addOneTimeComponentListener("my_btn", interaction -> {
            final Component clickedComponent = interaction.getClickedComponent();
            final Button button = clickedComponent.cast();
            interaction.respond("You clicked " + button.getLabel() + "!");
        });

        // Create an example command and respond to 
        // interactions with message components
        JDASlashCommands.submitGlobalCommand(new CommandBuilder()
                .name("test")
                .desc("Test command")
                .build(), interaction -> {
            interaction.respond("Available actions:", Arrays.asList(
                    ActionRow.of(
                            Button.normalButton(Button.Style.PRIMARY, "Action 1", "my_btn"),
                            Button.normalButton(Button.Style.SECONDARY, "Action 2", "my_btn_0"),
                            Button.normalButton(Button.Style.DANGER, "Action 3", "my_btn_1"),
                            Button.normalButton(Button.Style.SUCCESS, "Action 4", "my_btn_2")
                    ),
                    ActionRow.of(
                            Button.emojiButton(Button.Style.SUCCESS, "Emoji!", "my_btn_3",
                                    Button.PartialEmoji.getDefaultEmoji("❤️")),
                            Button.emojiButton(Button.Style.DANGER, "Another emoji!", "my_btn_4",
                                    Button.PartialEmoji.getEmojiFromEmote(jda.getEmoteById(850779306803986442L)))
                            //
                            // Link buttons seem to be broken at the moment
                            //
                            //Button.emojiLinkButton(Button.Style.DANGER, "Emoji with link!", "https://cerus.dev",
                            //        Button.PartialEmoji.getDefaultEmoji("✨")),
                            //Button.linkButton(Button.Style.SECONDARY, "Link!", "https://discord.com")
                    )
            ));
        });
    }

}
```

<details>
  <summary>Pictures</summary>

![Img 1](https://i.imgur.com/xlg2hYm.png)

![Img 2](https://i.imgur.com/vpGX60r.png)

</details>

## Installation

Since version 1.2.2, you can install `jda-slash-commands` from the central repository.

<details>
  <summary>Before 1.2.2</summary>

**Maven**

``` xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
	
<!--Replace TAG with the version-->
<dependency>
    <groupId>com.github.cerus</groupId>
    <artifactId>jda-slash-commands</artifactId>
    <version>Tag</version>
</dependency>

```

**Gradle**

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.cerus:jda-slash-commands:TAG'
}

```

</details>

**Maven**

```xml

<dependencies>
    <dependency>
        <groupId>dev.cerus</groupId>
        <artifactId>jda-slash-commands</artifactId>
        <version>1.3.1</version> <!-- Replace with latest version -->
        <scope>compile</scope>
    </dependency>
</dependencies>
```

**Gradle**

```
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}

dependencies {
    implementation 'dev.cerus:jda-slash-commands:1.3.1'
}
```
