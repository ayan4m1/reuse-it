package in.thekreml.reuseit;

import in.thekreml.reuseit.command.ReuseCommand;
import in.thekreml.reuseit.config.ConfigModel;
import in.thekreml.reuseit.listener.NotificationListener;
import in.thekreml.reuseit.listener.PlayerListener;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ReuseIt extends JavaPlugin {
  private ConfigModel configModel;
  private Logger log;
  private Permission permissions;

  public ConfigModel getConfigModel() {
    return configModel;
  }

  public Logger getLog() {
    return log;
  }

  public Permission getPermissions() {
    return permissions;
  }

  @Override
  public void onEnable() {
    configModel = ConfigModel.loadConfigModel(this);
    log = Bukkit.getLogger();

    if (!setupPerms()) {
      return;
    }

    Bukkit.getPluginManager().registerEvents(new NotificationListener(), this);
    Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

    final PluginCommand command = getCommand(Constants.COMMAND_REUSE);

    if (command != null) {
      command.setExecutor(new ReuseCommand(this));
    }

    log.info("ReuseIt enabled!");
  }

  private boolean setupPerms() {
    final RegisteredServiceProvider<Permission> permsProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
    if (permsProvider == null) {
      getServer().getPluginManager().disablePlugin(this);
      log.severe("Unable to find Permissions!");
      return false;
    }

    permissions = permsProvider.getProvider();
    return true;
  }
}
