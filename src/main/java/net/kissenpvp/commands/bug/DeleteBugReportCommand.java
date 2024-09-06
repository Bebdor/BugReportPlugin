package net.kissenpvp.commands.bug;

import net.kissenpvp.BugReport;
import net.kissenpvp.commands.BugReportNode;
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

public class DeleteBugReportCommand
{
    @CommandData(value = "deltebugreport", aliases = "delbug")
    public void delBugReport(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("id") @NotNull String reportId)
    {

        Player player = (Player) commandPayload.getSender();
        BugReport plugin = BugReport.getPlugin(BugReport.class);

        Optional<BugReportNode> reportOptional = plugin.getBugReports().stream()
                .filter(report -> report.id().equals(reportId))
                .findFirst();

        if (reportOptional.isPresent()) {
            plugin.getBugReports().remove(reportOptional.get());
            player.sendMessage(Component.translatable("server.bugreport.delete.success", Component.text(reportId)));
        } else {
            throw new OperationException(Component.translatable("server.bugreport.delete.notfound", Component.text(reportId)));
        }
    }
}
