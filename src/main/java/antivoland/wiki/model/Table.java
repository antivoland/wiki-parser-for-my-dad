package antivoland.wiki.model;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

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

    public void export(Path path) throws IOException {
        if (rows.isEmpty()) {
            return;
        }
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        List<List<String>> table = rows.values().stream().map(row ->
                row.cells.values().stream().map(cell -> cell.text).collect(toList())).collect(toList());

        Path file = path.resolve(row(0).firstCell().text + ".csv");
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(List.class).withColumnSeparator(';');
        ObjectWriter writer = mapper.writer(schema);
        writer.writeValue(file.toFile(), table);
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