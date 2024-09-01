package net.kissenpvp.commands;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Die BugReportNode Klasse repräsentiert einen einzelnen Bugbericht.
 *
 * <p>Die {@code BugReportNode} Klasse ist eine unveränderliche Datenstruktur, die verwendet wird, um einen Bugbericht zu speichern,
 * einschließlich einer eindeutigen ID, den Details des Fehlers und dem Namen des Spielers, der den Bericht eingereicht hat.</p>
 *
 * @param id       die eindeutige ID des Bugberichts
 * @param details  die Details des Fehlers oder Problems
 * @param reporter der Name des Spielers, der den Bericht eingereicht hat
 */

public record BugReportNode(@NotNull String id, @NotNull String details, @NotNull String reporter) {

    /**
     * Konstruktor für die BugReportNode Klasse.
     *
     * @param id       die eindeutige ID des Bugberichts
     * @param details  die Details des Fehlers oder Problems
     * @param reporter der Name des Spielers, der den Bericht eingereicht hat
     * @throws NullPointerException wenn einer der Parameter {@code null} ist
     */

    public BugReportNode {
        Objects.requireNonNull(id, "ID darf nicht null sein");
        Objects.requireNonNull(details, "Details dürfen nicht null sein");
        Objects.requireNonNull(reporter, "Reporter darf nicht null sein");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BugReportNode that = (BugReportNode) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
