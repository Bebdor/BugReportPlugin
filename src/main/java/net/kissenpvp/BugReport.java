package net.kissenpvp;

import net.kissenpvp.commands.BugReportNode;
import net.kissenpvp.commands.PlayerReportNode;
import net.kissenpvp.commands.bug.BugReportCommand;
import net.kissenpvp.commands.bug.ListBugReport;
import net.kissenpvp.commands.bug.SubmitBugCommand;
import net.kissenpvp.commands.player.DeletePlayerReportCommand;
import net.kissenpvp.commands.player.ListPlayerReports;
import net.kissenpvp.commands.player.PlayerReportCommand;
import net.kissenpvp.commands.player.SubmitPlayerReportCommand;
import net.kissenpvp.core.api.command.exception.OperationException;
import net.kissenpvp.core.api.database.connection.DatabaseImplementation;
import net.kissenpvp.core.api.database.meta.Table;
import net.kissenpvp.core.api.database.meta.list.MetaList;
import net.kissenpvp.core.api.util.PageBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.UUID;

public class BugReport extends JavaPlugin {

    private static BugReport instance;
    private MetaList<BugReportNode> bugReports;
    private MetaList<PlayerReportNode> playerReports;

    private static @NotNull Table getTable() {
        DatabaseImplementation databaseImplementation = Bukkit.getPulvinar().getImplementation(DatabaseImplementation.class);
        return databaseImplementation.getPrimaryConnection().createTable("report_table");
    }

    @Override
    public void onEnable() {
        this.bugReports = getTable().registerMeta(this).getCollection("bug_list", BugReportNode.class).join();
        this.playerReports = getTable().registerMeta(this).getCollection("playerreport_list", PlayerReportNode.class).join();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerCommand(this, new BugReportCommand(), new SubmitBugCommand(), new ListBugReport());
        pluginManager.registerCommand(this, new ListPlayerReports(), new PlayerReportCommand(), new SubmitPlayerReportCommand(), new DeletePlayerReportCommand());
        registerTranslations(pluginManager);
    }

    public static BugReport getPlugin() {
        return instance;
    }
    public MetaList<BugReportNode> getBugReports() {
        return bugReports;
    }

    public MetaList<PlayerReportNode> getPlayerReports() {
        return playerReports;
    }

    public Component generatePlayerReportComponent(Component title, String label, @NotNull PageBuilder<PlayerReportNode> pageBuilder, int pageNumber) {
        TextComponent.Builder builder = Component.text();
        for (int i = 0; i < pageBuilder.getEntries(pageNumber).size(); i++) {
            PlayerReportNode playerReportNode = pageBuilder.getEntries(pageNumber).get(i);
            Component[] args = {
                    Component.text(playerReportNode.id()),
                    Component.text(playerReportNode.reporter()),
                    Bukkit.getOfflinePlayer(playerReportNode.reportedPlayer()).displayName(),
                    Component.text(playerReportNode.details())
            };
            builder.appendNewline().append(Component.translatable("server.player.list.compact", args));
        }
        return builder.asComponent();
    }

    public Component generateComponent(Component title, String label, @NotNull PageBuilder<BugReportNode> pageBuilder, int pageNumber) {
        TextComponent.Builder builder = Component.text();
        for (int i = 0; i<pageBuilder.getEntries(pageNumber).size();i++)
        {
            BugReportNode bugReportNode = pageBuilder.getEntries(pageNumber).get(i);
            Component[] args = {
                    Component.text(bugReportNode.id()),
                    Component.text(bugReportNode.reporter()),
                    Component.text(bugReportNode.details())
            };
            builder.appendNewline().append(Component.translatable("server.bug.list.compact", args));
        }
        return builder.asComponent();
    }

    public void validate(boolean condition, Component errorMessage) throws OperationException {
        if (!condition) {
            throw new OperationException(errorMessage);
        }
    }

    private void registerTranslations(@NotNull PluginManager pluginManager)
    {
        String override = "Do you want to override it? Type /confirm to confirm or /cancel to cancel. This request will expire after 30 seconds.";

        pluginManager.registerTranslation("server.bugreport.create.success", new MessageFormat("Successfully submitted a bug report"), this);
        pluginManager.registerTranslation("server.bugreport.update.success", new MessageFormat("Successfully updated the bug report with the ID: {0}"), this);
        pluginManager.registerTranslation("server.bugreport.id.exist", new MessageFormat("The bug report with the ID: {0} already exist. " + override), this);
        pluginManager.registerTranslation("server.bugreport.list.empty", new MessageFormat("The bug report list is empty"), this);
        pluginManager.registerTranslation("server.bug.list.compact", new MessageFormat("[{0}] [{1}] {2}"), this);
        pluginManager.registerTranslation("server.player.submit.success", new MessageFormat("Successfully submitted a player report"), this);

        pluginManager.registerTranslation("server.playerreport.create.success", new MessageFormat("Successfully submitted a player report"), this);
        pluginManager.registerTranslation("server.playerreport.update.success", new MessageFormat("Successfully updated the player report with the ID: {0}"), this);
        pluginManager.registerTranslation("server.playerreport.id.exist", new MessageFormat("The player report with the ID: {0} already exists. " + override), this);
        pluginManager.registerTranslation("server.playerreport.list.empty", new MessageFormat("The player report list is empty"), this);
        pluginManager.registerTranslation("server.player.list.compact", new MessageFormat("[{0}] [{1}] [{2}] {3}"), this);
        pluginManager.registerTranslation("server.playerreport.delete.success", new MessageFormat("Successfully deleted the player report with the ID: {0}"), this);
        pluginManager.registerTranslation("server.playerreport.delete.notfound", new MessageFormat("No player report found with the ID: {0}"), this);
    }

    public boolean submitBugReport(@NotNull Player player, String details) {
        BugReportNode report = new BugReportNode(String.valueOf(bugReports.size() + 1), details, player.getName());
        if (bugReports.stream().anyMatch(existingReport -> existingReport.id().equals(report.id()))) {
            return false;
        }
        bugReports.add(report);
        player.sendMessage("Ja lief gut"); //TODO
        return true;
    }

    public boolean submitPlayerReport(@NotNull Player player, UUID reportedPlayer, String details) {
        PlayerReportNode report = new PlayerReportNode(String.valueOf(playerReports.size() + 1), player.getName(), reportedPlayer, details);
        if (playerReports.stream().anyMatch(existingReport -> existingReport.id().equals(report.id()))) {
            return false;
        }
        playerReports.add(report);
        player.sendMessage(Component.translatable("server.playerreport.create.success"));
        return true;
    }
}