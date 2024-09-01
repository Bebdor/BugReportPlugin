package net.kissenpvp.commands.bug;

import net.kissenpvp.BugReport;
import net.kissenpvp.commands.BugReportNode;
import net.kissenpvp.core.api.command.CommandPayload;
import net.kissenpvp.core.api.command.annotations.ArgumentName;
import net.kissenpvp.core.api.command.annotations.CommandData;
import net.kissenpvp.core.api.util.PageBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ListBugReport {

    @CommandData(value = "reportlist", aliases = "listbugs")
    public void listBugReportCommand(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("page") @NotNull Optional<Integer> page) {
        Player player = (Player) commandPayload.getSender();
        BugReport plugin = BugReport.getPlugin(BugReport.class);

        List<BugReportNode> cache = plugin.getBugReports();
        plugin.validate(!cache.isEmpty(), Component.translatable("server.bugreport.list.empty"));

        Component title = Component.text("Bug");
        PageBuilder<BugReportNode> pageBuilder = generatePageBuilder(cache);

        player.sendMessage(plugin.generateComponent(title, commandPayload.getLabel(), pageBuilder, page.orElse(1)));
    }

    private @NotNull PageBuilder<BugReportNode> generatePageBuilder(@NotNull List<BugReportNode> cache) {
        return new PageBuilder<>(cache);
    }

}
