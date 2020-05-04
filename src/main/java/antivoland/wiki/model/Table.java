package antivoland.wiki.model;

import antivoland.wiki.CsvWriter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

/**
 * @author antivoland
 */
public class Table {
    private final TreeMap<Integer, Row> rows = new TreeMap<>();
    private final int no;

    public Table(Element table, int no) {
        Elements rows = table.select("tr");
        for (int row = 0; row < rows.size(); ++row) {
            for (Element cell : rows.get(row).select("th,td")) {
                cleanupCell(cell);
                add(row, new Cell(cell));
            }
        }
        this.no = no;
    }

    public int no() {
        return no;
    }

    public String name() {
        Cell cell = row(0).firstCell();
        if (cell == null || cell.text == null) {
            return "table" + no;
        }

        String name = cell.text.replaceAll("[^\\s\\p{IsAlphabetic}\\p{IsDigit}]", "");
        return name.isEmpty() ? "table" + no : name;
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

    public void export(Path path) throws IOException {
        if (rows.isEmpty()) {
            return;
        }
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        try (CsvWriter writer = new CsvWriter(path.resolve(name() + ".csv"), ';')) {
            for (Row row : rows.values()) {
                writer.write(row.cellValues());
            }
        }
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