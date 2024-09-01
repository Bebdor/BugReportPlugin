package net.kissenpvp.commands.player;

import net.kissenpvp.BugReport;
import net.kissenpvp.commands.PlayerReportNode;
import net.kissenpvp.core.api.command.CommandPayload;
import net.kissenpvp.core.api.command.annotations.ArgumentName;
import net.kissenpvp.core.api.command.annotations.CommandData;
import net.kissenpvp.core.api.command.exception.OperationException;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DeletePlayerReportCommand {

    @CommandData(value = "deleteplayerreport", aliases = {"removeplayerreport", "delplayerreport"})
    public void deletePlayerReportCommand(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("id") @NotNull String reportId) {
        Player player = (Player) commandPayload.getSender();
        BugReport plugin = BugReport.getPlugin(BugReport.class);

        Optional<PlayerReportNode> reportOptional = plugin.getPlayerReports().stream()
                .filter(report -> report.id().equals(reportId))
                .findFirst();

        if (reportOptional.isPresent()) {
            plugin.getPlayerReports().remove(reportOptional.get());
            player.sendMessage(Component.translatable("server.playerreport.delete.success", Component.text(reportId)));
        } else {
            throw new OperationException(Component.translatable("server.playerreport.delete.notfound", Component.text(reportId)));
        }
    }
}
