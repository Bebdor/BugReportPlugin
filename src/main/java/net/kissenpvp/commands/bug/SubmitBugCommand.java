package net.kissenpvp.commands.bug;

import net.kissenpvp.BugReport;
import net.kissenpvp.commands.BugReportNode;
import net.kissenpvp.core.api.command.CommandPayload;
import net.kissenpvp.core.api.command.annotations.ArgumentName;
import net.kissenpvp.core.api.command.annotations.CommandData;
import net.kissenpvp.core.api.database.meta.list.MetaList;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SubmitBugCommand {

    @CommandData(value = "reportcreate", aliases = "createbug")
    public void createBugReportCommand(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("id") String reportId, @ArgumentName("details") String... reportDetails) {
        Player player = (Player) commandPayload.getSender();

        MetaList<BugReportNode> reports = BugReport.getPlugin(BugReport.class).getBugReports();

        Component reportComponent = Component.text(reportId);
        BugReportNode report = new BugReportNode(reportId, String.join(" ", reportDetails), player.getName());

        if (reports.stream().anyMatch(existingReport -> existingReport.id().equals(reportId))) {
            player.sendMessage(Component.translatable("server.bugreport.id.exist", Component.text(reportId)));
            commandPayload.confirmRequest(() -> {
                reports.replaceOrInsert(report);
                player.sendMessage(Component.translatable("server.bugreport.update.success", Component.text(reportId)));
            }).suppressMessage(true).send();
            return;
        }

        reports.add(report);
        player.sendMessage(Component.translatable("server.bugreport.create.success", reportComponent));
    }

}
