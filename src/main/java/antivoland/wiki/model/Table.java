package antivoland.wiki.model;

import antivoland.wiki.Config;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static java.nio.file.StandardOpenOption.*;

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

    private static void cleanupCell(Element cell) {
        cell.select(".collapseButton").remove();
        cell.select("a").forEach(link -> {
            if (link.attr("href").startsWith("#")) {
                link.remove();
            }
        });
    }

    public static class Exporter {
        private static final CsvMapper MAPPER = new CsvMapper();

        private final ObjectWriter writer;
        private final Charset charset;

        public Exporter(Config config) {
            writer = MAPPER.writer(schema(config));
            charset = Charset.forName(config.encoding);
        }

        public void export(Table table, Path path) throws IOException {
            if (table.rows.isEmpty()) {
                return;
            }
            List<String> serialized = new ArrayList<>(table.rows.size());
            for (Row row : table.rows.values()) {
                serialized.add(writer.writeValueAsString(row.cellValues()));
            }
            Files.write(path.resolve("segments").resolve(table.name() + ".csv"), serialized, charset, WRITE, CREATE);
            Files.write(path.resolve("all.csv"), serialized, charset, WRITE, APPEND, CREATE);
        }

        private static CsvSchema schema(Config config) {
            return MAPPER.schemaFor(List.class).withColumnSeparator(config.separator).withLineSeparator("");
        }
    }
}