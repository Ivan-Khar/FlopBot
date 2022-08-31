package com.aqupd.flopbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Mentions;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.aqupd.flopbot.Main.*;

@SuppressWarnings("ConstantConditions")
public class Listener extends ListenerAdapter {
  @Override
  public void onReady(@NotNull ReadyEvent event) {
  }

  @Override
  public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
    LOGGER.info(event.getUser().getName() + " joined");
    event.getGuild().addRoleToMember(UserSnowflake.fromId(event.getUser().getId()), jda.getRoleById("832293356151373844")).queue();

    MessageEmbed eb = new EmbedBuilder()
        .setTitle("Welcome " + event.getUser().getName() + "!")
        .setFooter("Joined")
        .setColor(Color.decode("#05a8a8"))
        .setTimestamp(Instant.now()).build();
    event.getGuild().getTextChannelById(1014548081821351936L).sendMessageEmbeds(eb).queue();
  }

  @Override
  public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
    event.getGuild().retrieveAuditLogs().queueAfter(1, TimeUnit.SECONDS, (logs) -> {
      boolean isBan = false, isKick = false;
      String admin = "";
        for (AuditLogEntry log : logs) {
          if (log.getTargetIdLong() == event.getUser().getIdLong()) {
            isBan = log.getType() == ActionType.BAN;
            isKick = log.getType() == ActionType.KICK;
            admin = log.getUser().getName();
            break;
          }
        }
        String title = (isBan) ? " got beaned" : ((isKick) ? " got kicked" : " left the guild!");
        MessageEmbed eb = new EmbedBuilder()
            .setTitle(event.getUser().getName() + title)
            .setDescription((isBan || isKick) ? ("by " + admin) : "")
            .setColor(Color.decode("#ae002e"))
            .setTimestamp(Instant.now()).build();
        event.getGuild().getTextChannelById(1014548081821351936L).sendMessageEmbeds(eb).queue();
    });
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    if(event.getAuthor().isBot()) return;

    AtomicBoolean adminM = new AtomicBoolean(false);
    Mentions mentions = event.getMessage().getMentions();
    mentions.getRoles().forEach(r -> { if(r.getIdLong() == 832293373347233862L) adminM.set(true); });
    mentions.getMembers().forEach(m -> m.getRoles().forEach(r -> { if(r.getIdLong() == 832293373347233862L) adminM.set(true); }));

    if(adminM.get() && event.getMember().getRoles().stream().noneMatch(role -> (role.getIdLong() == 832293373347233862L || role.getIdLong() == 845698513719001168L))) event.getMessage().reply("<:pinged:1014581861613322270>").queue();
  }
}
