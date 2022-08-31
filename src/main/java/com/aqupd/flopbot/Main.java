package com.aqupd.flopbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

import java.util.Arrays;
import java.util.EnumSet;

import static com.aqupd.flopbot.Config.getToken;

public class Main {

  public static JDA jda;
  public static final Logger LOGGER = LogManager.getLogger("FlopBot");
  public static final GatewayIntent[] INTENTS = {
      GatewayIntent.MESSAGE_CONTENT,
      GatewayIntent.GUILD_PRESENCES,
      GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
      GatewayIntent.DIRECT_MESSAGES,
      GatewayIntent.GUILD_MESSAGES,
      GatewayIntent.GUILD_MESSAGE_REACTIONS,
      GatewayIntent.GUILD_VOICE_STATES,
      GatewayIntent.GUILD_MEMBERS
  };

  public static void main(String[] args) {
    Config.INSTANCE.load();
    try {
      jda = JDABuilder.create(getToken(), Arrays.asList(INTENTS))
          .enableCache(EnumSet.allOf(CacheFlag.class))
          .setActivity(Activity.watching("you"))
          .setStatus(OnlineStatus.ONLINE)
          .addEventListeners(new Listener())
          .build();
    } catch (LoginException e) {
      LOGGER.traceExit("Couldn't log into bot account", e);
    }
  }
}