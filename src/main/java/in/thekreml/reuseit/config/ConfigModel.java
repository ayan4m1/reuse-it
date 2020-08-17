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
  private Set<Material> materials;

  public Set<Material> getMaterials() {
    return materials;
  }

  public void setMaterials(Set<Material> materials) {
    this.materials = materials;
  }

  public static ConfigModel loadConfigModel(JavaPlugin plugin) {
    final ConfigModel result = new ConfigModel();

    try {
      plugin.getConfig().load(Constants.CONFIG_PATH);

      final List<String> materialNames = plugin.getConfig().getStringList(Constants.CONFIG_KEY_MATERIALS);

      if (materialNames.size() < 1) {
        throw new InvalidConfigurationException(Constants.CONFIG_ERROR_EMPTY_MATERIALS);
      }

      result.setMaterials(materialNames
          .stream()
          .distinct()
          .map(Material::getMaterial)
          .collect(Collectors.toSet())
      );
    } catch (IOException e) {
      plugin.getLogger().severe(String.join("" , "IOException during config read: ", e.getMessage()));
    } catch (InvalidConfigurationException e) {
      plugin.getLogger().severe(String.join("" , "InvalidConfigurationException during config read: ", e.getMessage()));
    }

    return result;
  }
}
