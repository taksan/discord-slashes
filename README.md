# Discord Slashes

This project allows declaring slash commands using annotations and provides classes to simplify oauth authentication
with discord.

## Basic usage

1. To implement slash commands, add a class that implements the `SlashCommand` interface and annotate it with the 
 @SlashCommand annotation

  ```java
  package my.commands.pkg;
  
  @Command(name = "my-command", description = "your awesome command", requiresGuild = true)
  public class AwesomeCommand implements SlashCommand {
  
      @Override
      public CommandReply execute(SlashContextOperator discordOperator) {
          // do some awesome stuff
          return BasicCommandReply.basicReply("It's awesome!!");
      }
  }
  ```

  Observe the following:
  * Your `execute` method must return a CommandDeploy. The simplest way to do this is to return a BasicCommandReply
  * Your slash command cannot take longer than 3 seconds. If the command might take longer, run in a separate thread

2. To initialize the bot, all you have to do is instantiate the DiscordApplication class and call `start()`:

```java
final DiscordApplication myAwesomeBot =
        DiscordApplication.create(BOT_TOKEN, "my.commands.pkg");

myAwesomeBot.start();
```

  * `BOT_TOKEN` is your bot token
  * `my.commands.pkg` is the root package for your commands. This is used to limit the class scanning
    to ensure performance, so try to avoid using a top level package that would cause reflections to
    scan too many classes

## Publishing

CI publishes snapshots to GitHub Packages on pushes to `main`/`master`.

1. Create a **classic** personal access token (fine-grained tokens are not supported by GitHub Packages) owned by the repository owner (`taksan`) with `write:packages` and `read:packages` scopes.
2. Add it as a repository secret named `PACKAGES_PUBLISH_TOKEN` (Settings → Secrets and variables → Actions).
3. Push these workflow changes to `main` — the workflow on GitHub must use `.github/maven/settings.xml` for authentication to work.
