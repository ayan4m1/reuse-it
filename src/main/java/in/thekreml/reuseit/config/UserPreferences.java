package in.thekreml.reuseit.config;

import java.util.HashMap;
import java.util.Map;

public class UserPreferences {
  private static final Map<String, Boolean> PLAYER_STATES = new HashMap<>();

  public static void enable(String playerName) {
    PLAYER_STATES.put(playerName, true);
  }

  public static void disable(String playerName) {
    PLAYER_STATES.put(playerName, false);
  }

  public static boolean isEnabled(String playerName) {
    return PLAYER_STATES.getOrDefault(playerName, true);
  }

  public static boolean toggle(String playerName) {
    final boolean newValue = !isEnabled(playerName);

    PLAYER_STATES.put(playerName, newValue);

    return newValue;
  }
}
