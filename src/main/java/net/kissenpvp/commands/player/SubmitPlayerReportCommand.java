package net.kissenpvp.commands.player;

import net.kissenpvp.BugReport;
import net.kissenpvp.commands.PlayerReportNode;
import net.kissenpvp.core.api.command.CommandPayload;
import net.kissenpvp.core.api.command.annotations.ArgumentName;
import net.kissenpvp.core.api.command.annotations.CommandData;
import net.kissenpvp.core.api.database.meta.list.MetaList;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubmitPlayerReportCommand {

    @CommandData(value = "playerreportcreate", aliases = "createplayerreport")
    public void createPlayerReportCommand(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("id") String reportId, @ArgumentName("reportedPlayer") OfflinePlayer reportedPlayer, @ArgumentName("details") String... reportDetails) {
        Player player = (Player) commandPayload.getSender();

        MetaList<PlayerReportNode> reports = BugReport.getPlugin(BugReport.class).getPlayerReports();

        Component reportComponent = Component.text(reportId);
        PlayerReportNode report = new PlayerReportNode(reportId, player.getName(), reportedPlayer.getUniqueId(), String.join(" ", reportDetails));

        if (reports.stream().anyMatch(existingReport -> existingReport.id().equals(reportId))) {
            player.sendMessage(Component.translatable("server.playerreport.id.exist", Component.text(reportId)));
            commandPayload.confirmRequest(() -> {
                reports.replaceOrInsert(report);
                player.sendMessage(Component.translatable("server.playerreport.update.success", Component.text(reportId)));
            }).suppressMessage(true).send();
            return;
        }

        reports.add(report);
        player.sendMessage(Component.translatable("server.playerreport.create.success", reportComponent));
    }
}
