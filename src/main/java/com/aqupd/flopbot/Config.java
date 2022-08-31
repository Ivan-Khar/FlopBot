package com.aqupd.flopbot;

import com.google.gson.*;
import java.io.*;
import static com.aqupd.flopbot.Main.*;

@SuppressWarnings({"FieldMayBeFinal", "ResultOfMethodCallIgnored", "FieldCanBeLocal", "unused"})
public class Config {

  private Config() {}
  public static final Config INSTANCE = new Config();

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private String TOKEN = "bot_token";
  private String[] OWNERS = {"459442554623098882", "1"};
  private String PREFIX = "f!";

  private final File confFile = new File("./config.json");

  public static String getToken() { return INSTANCE.TOKEN; }
  public static String getPrefix() { return INSTANCE.PREFIX; }
  public static String[] getOwners() { return INSTANCE.OWNERS;}

  public void load() {
    if (!confFile.exists() || confFile.length() == 0) save();
    try {
      JsonObject jo = gson.fromJson(new FileReader(confFile), JsonObject.class);
      JsonElement jE;
      if ((jE = jo.get("token")) != null) TOKEN = jE.getAsString();
      if ((jE = jo.get("prefix")) != null) PREFIX = jE.getAsString();
      if ((jE = jo.get("owners")) != null) OWNERS = gson.fromJson(jE.getAsJsonArray(), String[].class);
      save();
    } catch (FileNotFoundException ex) {
      LOGGER.trace("Conf. file not found (strange)", ex);
    }
  }

  public void save() {
    try {
      if (!confFile.exists()) confFile.createNewFile();
      if (confFile.exists()) {
        JsonObject jo = new JsonObject();
        jo.add("token", new JsonPrimitive(TOKEN));
        jo.add("prefix", new JsonPrimitive(PREFIX));

        JsonArray owners = new JsonArray();
        for(String owner: OWNERS) { owners.add(owner); }
        jo.add("owners", owners);

        PrintWriter printwriter = new PrintWriter(new FileWriter(confFile));
        printwriter.print(gson.toJson(jo));
        printwriter.close();
      }
    } catch (IOException ex) {
      LOGGER.trace("Got problems saving conf. file", ex);
    }
  }
}
