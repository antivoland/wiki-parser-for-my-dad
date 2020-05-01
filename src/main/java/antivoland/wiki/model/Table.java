package antivoland.wiki.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.TreeMap;

/**
 * @author antivoland
 */
public class Table {
    private final TreeMap<Integer, Row> rows = new TreeMap<>();

    public Table(Element table) {
        Elements rows = table.select("tr");
        for (int row = 0; row < rows.size(); ++row) {
            for (Element cell : rows.get(row).select("th,td")) {
                cleanupCell(cell);
                add(row, new Cell(cell));
            }
        }
    }

    private void add(int row, Cell cell) {
        int column = row(row).lastUnsetColumn();
        for (int i = 0; i < cell.height; ++i) {
            for (int j = 0; j < cell.width; ++j) {
                row(row + i).set(column + j, cell);
            }
        }
    }

    private Row row(int row) {
        if (!rows.containsKey(row)) {
            rows.put(row, new Row());
        }
        return rows.get(row);
    }

    private static void cleanupCell(Element cell) {
        cell.select(".collapseButton").remove();
        cell.select("a").forEach(link -> {
            if (link.attr("href").startsWith("#")) {
                link.remove();
            }
        });
    }
}