package in.thekreml.reuseit.config;

import in.thekreml.reuseit.Constants;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigModel {
  private boolean interactEnabled;
  private Set<Material> interactMaterials;
  private boolean breakEnabled;
  private Set<Material> breakMaterials;
  private boolean throwEnabled;
  private Set<Material> throwMaterials;
  private boolean consumeEnabled;
  private Set<Material> consumeMaterials;
  private boolean autoConsumeEnabled;

  public boolean isInteractEnabled() {
    return interactEnabled;
  }

  public void setInteractEnabled(boolean interactEnabled) {
    this.interactEnabled = interactEnabled;
  }

  public Set<Material> getInteractMaterials() {
    return interactMaterials;
  }

  public void setInteractMaterials(Set<Material> interactMaterials) {
    this.interactMaterials = interactMaterials;
  }

  public boolean isBreakEnabled() {
    return breakEnabled;
  }

  public void setBreakEnabled(boolean breakEnabled) {
    this.breakEnabled = breakEnabled;
  }

  public Set<Material> getBreakMaterials() {
    return breakMaterials;
  }

  public void setBreakMaterials(Set<Material> breakMaterials) {
    this.breakMaterials = breakMaterials;
  }

  public boolean isThrowEnabled() {
    return throwEnabled;
  }

  public void setThrowEnabled(boolean throwEnabled) {
    this.throwEnabled = throwEnabled;
  }

  public Set<Material> getThrowMaterials() {
    return throwMaterials;
  }

  public void setThrowMaterials(Set<Material> throwMaterials) {
    this.throwMaterials = throwMaterials;
  }

  public boolean isConsumeEnabled() {
    return consumeEnabled;
  }

  public void setConsumeEnabled(boolean consumeEnabled) {
    this.consumeEnabled = consumeEnabled;
  }

  public Set<Material> getConsumeMaterials() {
    return consumeMaterials;
  }

  public void setConsumeMaterials(Set<Material> consumeMaterials) {
    this.consumeMaterials = consumeMaterials;
  }

  public boolean isAutoConsumeEnabled() {
    return autoConsumeEnabled;
  }

  public void setAutoConsumeEnabled(boolean autoConsumeEnabled) {
    this.autoConsumeEnabled = autoConsumeEnabled;
  }

  public static ConfigModel loadConfigModel(JavaPlugin plugin) {
    final ConfigModel result = new ConfigModel();

    try {
      plugin.saveDefaultConfig();
      plugin.getConfig().load(Constants.CONFIG_PATH);

      final boolean interactEnabled = plugin.getConfig().getBoolean(Constants.CONFIG_KEY_INTERACT_ENABLED);
      final boolean breakEnabled = plugin.getConfig().getBoolean(Constants.CONFIG_KEY_BREAK_ENABLED);
      final boolean throwEnabled = plugin.getConfig().getBoolean(Constants.CONFIG_KEY_THROW_ENABLED);
      final boolean consumeEnabled = plugin.getConfig().getBoolean(Constants.CONFIG_KEY_CONSUME_ENABLED);
      final boolean autoConsumeEnabled = plugin.getConfig().getBoolean(Constants.CONFIG_KEY_AUTO_CONSUME_ENABLED);

      result.setInteractEnabled(interactEnabled);
      result.setBreakEnabled(breakEnabled);
      result.setThrowEnabled(throwEnabled);
      result.setConsumeEnabled(consumeEnabled);
      result.setAutoConsumeEnabled(autoConsumeEnabled);

      if (interactEnabled) {
        final List<String> materialNames = plugin.getConfig().getStringList(Constants.CONFIG_KEY_INTERACT_MATERIALS);

        if (materialNames.size() < 1) {
          throw new InvalidConfigurationException(Constants.CONFIG_ERROR_EMPTY_MATERIALS);
        }

        result.setInteractMaterials(materialNames
            .stream()
            .distinct()
            .map(Material::getMaterial)
            .collect(Collectors.toSet())
        );
      }

      if (breakEnabled) {
        final List<String> materialNames = plugin.getConfig().getStringList(Constants.CONFIG_KEY_BREAK_MATERIALS);

        if (materialNames.size() < 1) {
          throw new InvalidConfigurationException(Constants.CONFIG_ERROR_EMPTY_MATERIALS);
        }

        result.setBreakMaterials(materialNames
            .stream()
            .distinct()
            .map(Material::getMaterial)
            .collect(Collectors.toSet())
        );
      }

      if (throwEnabled) {
        final List<String> materialNames = plugin.getConfig().getStringList(Constants.CONFIG_KEY_THROW_MATERIALS);

        if (materialNames.size() < 1) {
          throw new InvalidConfigurationException(Constants.CONFIG_ERROR_EMPTY_MATERIALS);
        }

        result.setThrowMaterials(materialNames
            .stream()
            .distinct()
            .map(Material::getMaterial)
            .collect(Collectors.toSet())
        );
      }

      if (consumeEnabled) {
        final List<String> materialNames = plugin.getConfig().getStringList(Constants.CONFIG_KEY_CONSUME_MATERIALS);

        if (materialNames.size() < 1) {
          throw new InvalidConfigurationException(Constants.CONFIG_ERROR_EMPTY_MATERIALS);
        }

        result.setConsumeMaterials(materialNames
            .stream()
            .distinct()
            .map(Material::getMaterial)
            .collect(Collectors.toSet())
        );
      }
    } catch (IOException e) {
      plugin.getLogger().severe(String.join("" , "IOException during config read: ", e.getMessage()));
    } catch (InvalidConfigurationException e) {
      plugin.getLogger().severe(String.join("" , "InvalidConfigurationException during config read: ", e.getMessage()));
    }

    return result;
  }
}
