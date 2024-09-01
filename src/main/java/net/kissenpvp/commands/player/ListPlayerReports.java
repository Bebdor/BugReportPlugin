package net.kissenpvp.commands.player;

import net.kissenpvp.BugReport;
import net.kissenpvp.commands.PlayerReportNode;
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

public class ListPlayerReports {

    @CommandData(value = "playerreportlist", aliases = "listplayerreports")
    public void listPlayerReportCommand(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("page") @NotNull Optional<Integer> page) {
        Player player = (Player) commandPayload.getSender();
        BugReport plugin = BugReport.getPlugin(BugReport.class);

        List<PlayerReportNode> cache = plugin.getPlayerReports();
        plugin.validate(!cache.isEmpty(), Component.translatable("server.playerreport.list.empty"));

        Component title = Component.text("Player Report");
        PageBuilder<PlayerReportNode> pageBuilder = generatePageBuilder(cache);

        player.sendMessage(plugin.generatePlayerReportComponent(title, commandPayload.getLabel(), pageBuilder, page.orElse(1)));
    }

    private @NotNull PageBuilder<PlayerReportNode> generatePageBuilder(@NotNull List<PlayerReportNode> cache) {
        return new PageBuilder<>(cache);
    }
}
