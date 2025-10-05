package org.dndbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.dndbot.character.DnDChar;
import org.dndbot.fifthapi.DnDAPI;
import org.jetbrains.annotations.NotNull;
import org.dndbot.DnDBot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class DiscordEventListener extends ListenerAdapter {
    public DnDBot bot;
    public DiscordEventListener(DnDBot bot) {
        this.bot = bot;
    }

    // There is a reason why we don't add the commands IMMEDIENTLY after the bot starts up. The bot has to load in all the guilds it is in before it can add commands.
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        registerCommands(bot.getShardManager());
    }

    // This method is called when the bot is ready to add commands. This is where we add the commands to the server.
    private void registerCommands(ShardManager jda) {
        Guild g = jda.getGuildById("1424104192825491456"); // Replace this with the ID of your own server.
        if (g != null) {
            CommandListUpdateAction commands = g.updateCommands();
            commands.addCommands(Commands.slash("hello", "Have the bot say hello to you in an ephemeral message!")).queue();
            commands.addCommands(Commands.slash("d20", "Roll a d20")).queue();
            commands.addCommands(Commands.slash("addchar", "Add a character")
                    .addOptions(new OptionData(OptionType.STRING, "name", "Name your character", true),
                            new OptionData(OptionType.STRING, "class", "Give your character a class", true).addChoices(classChoices)))
                    .queue();
            commands.addCommands(Commands.slash("charsheet", "Retrieve your character sheet")
                    .addOptions(new OptionData(OptionType.STRING, "name", "Name of your character", true)))
                    .queue();

            // All slash commands must be added here. They follow a strict set of rules and are not as flexible as text commands.
            // Since we only need a simple command, we will only use a slash command without any arguments.
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("hello")) { // Is the command name "hello"?
            event.reply("Hello " + event.getUser().getAsMention() + "!") // What will we reply with?
                    .setEphemeral(true) // Do we want the message hidden so only the user who ran the command can see it?
                    .queue(); // Queue the reply.
        }
        if (event.getName().equals("d20")) {
            event.reply("Your d20 roll is: " + ((int)(Math.random() * 20)+ 1)) // What will we reply with?
                    .queue(); // Queue the reply.
        }
        if (event.getName().equals("addchar")) {
            String charName = event.getOption("name").getAsString();
            String DnDClass = event.getOption("class").getAsString();
            Long userId = event.getUser().getIdLong();
            DnDChar character = new DnDChar(charName, DnDClass, userId);

            // Serialize and save character to characters.json
            ObjectMapper mapper = new ObjectMapper();
            Path filePath = Paths.get("characters.json");
            List<DnDChar> characters = new ArrayList<>();
            System.out.println("Saving to: " + filePath.toAbsolutePath());

            try {
                if (Files.exists(filePath) && Files.size(filePath) > 0) {
                    characters = mapper.readValue(filePath.toFile(), new com.fasterxml.jackson.core.type.TypeReference<List<DnDChar>>() {});
                }
                characters.add(character);
                mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), characters);
                event.reply("Character has been created!").queue();
            } catch (IOException e) {
                e.printStackTrace();
                event.reply("Failed to save character: " + e.getMessage()).queue();
            }
        }
        if (event.getName().equals("charsheet")) {
            String charName = event.getOption("name").getAsString();
            Long userId = event.getUser().getIdLong();
            ObjectMapper mapper = new ObjectMapper();
            Path filePath = Paths.get("characters.json");
            List<DnDChar> characters = new ArrayList<>();
            try {
                if (Files.exists(filePath) && Files.size(filePath) > 0) {
                    characters = mapper.readValue(filePath.toFile(), new com.fasterxml.jackson.core.type.TypeReference<List<DnDChar>>() {});
                }
                Optional<DnDChar> found = characters.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(charName) && c.getUserId() == (userId))
                        .findFirst();
                if (found.isPresent()) {
                    DnDChar c = found.get();
                    net.dv8tion.jda.api.EmbedBuilder eb = new net.dv8tion.jda.api.EmbedBuilder();
                    eb.setTitle("Character Sheet: " + c.getName());
                    eb.addField("Class", c.getDnDClass(), true);
                    eb.addField("Owner", "<@" + c.getUserId() + ">", true);
                    eb.addBlankField(false);
                    eb.addField("Strength", String.valueOf(c.getStrength()), true);
                    eb.addField("Dexterity", String.valueOf(c.getDexterity()), true);
                    eb.addField("Constitution", String.valueOf(c.getConstitution()), true);
                    eb.addField("Intelligence", String.valueOf(c.getIntelligence()), true);
                    eb.addField("Wisdom", String.valueOf(c.getWisdom()), true);
                    eb.addField("Charisma", String.valueOf(c.getCharisma()), true);
                    event.replyEmbeds(eb.build()).queue();
                } else {
                    event.reply("Character not found or you do not own this character.").setEphemeral(true).queue();
                }
            } catch (IOException e) {
                e.printStackTrace();
                event.reply("Failed to load character: " + e.getMessage()).queue();
            }
        }
    }
    String[] classNames = DnDAPI.getClassNames();
    Command.Choice[] classChoices = Arrays.stream(classNames)
            .map(name -> new Command.Choice(name, name))
            .toArray(Command.Choice[]::new);

}