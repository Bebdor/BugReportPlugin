package net.kissenpvp.commands;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public record PlayerReportNode(@NotNull String id, @NotNull String reporter, @NotNull UUID reportedPlayer, @NotNull String details) {

    public PlayerReportNode {
        Objects.requireNonNull(id, "ID darf nicht null sein");
        Objects.requireNonNull(reporter, "Reporter darf nicht null sein");
        Objects.requireNonNull(reportedPlayer, "Reported Player darf nicht null sein");
        Objects.requireNonNull(details, "Details dürfen nicht null sein");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerReportNode that = (PlayerReportNode) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}