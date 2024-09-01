package net.kissenpvp.commands.bug;

import net.kissenpvp.BugReport;
import net.kissenpvp.commands.BugReportNode;
import net.kissenpvp.core.api.command.CommandPayload;
import net.kissenpvp.core.api.command.CommandTarget;
import net.kissenpvp.core.api.command.annotations.ArgumentName;
import net.kissenpvp.core.api.command.annotations.CommandData;
import net.kissenpvp.core.api.command.annotations.TabCompleter;
import net.kissenpvp.core.api.command.exception.OperationException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.stream.Collectors;

public class BugReportCommand {

    @CommandData(value = "report", target = CommandTarget.PLAYER)
    public void reportCommand(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("details") String reportDetails) {
        Player player = (Player) commandPayload.getSender();
        BugReport plugin = BugReport.getPlugin(BugReport.class);

        Component details = Component.text(reportDetails);
        Component message = Component.translatable("server.bugreport.submit.success", details);
        if (!plugin.submitBugReport(player, reportDetails)) {
            throw new OperationException(Component.translatable("server.bugreport.submit.failed", details));
        }
    }

    @TabCompleter("report")
    public @NotNull @Unmodifiable Set<String> reportTabCompleter() {
        BugReport plugin = BugReport.getPlugin(BugReport.class);
        return plugin.getBugReports().stream().map(BugReportNode::id).collect(Collectors.toUnmodifiableSet());
    }

}
