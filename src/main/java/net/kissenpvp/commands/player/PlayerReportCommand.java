package net.kissenpvp.commands.player;

import net.kissenpvp.BugReport;
import net.kissenpvp.core.api.command.CommandPayload;
import net.kissenpvp.core.api.command.CommandTarget;
import net.kissenpvp.core.api.command.annotations.ArgumentName;
import net.kissenpvp.core.api.command.annotations.CommandData;
import net.kissenpvp.core.api.command.exception.OperationException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;

public class PlayerReportCommand {

    @CommandData(value = "playerreport", target = CommandTarget.PLAYER)
    public void playerReportCommand(@NotNull CommandPayload<CommandSender> commandPayload, @ArgumentName("reportedPlayer") @NotNull OfflinePlayer reportedPlayer, @ArgumentName("details") String... reportDetails) {
        Player player = (Player) commandPayload.getSender();
        BugReport plugin = BugReport.getPlugin(BugReport.class);

        String mountedMessage = String.join(" ", reportDetails);
        Component details = Component.text(mountedMessage);
        if (!plugin.submitPlayerReport(player, reportedPlayer.getUniqueId(), mountedMessage)) {
            throw new OperationException(Component.translatable("server.playerreport.submit.failed", details));
        }
    }
}
