package antivoland.sandbox.covid19;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author antivoland
 */
public class Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    public static void main(String[] args) throws IOException {
        // todo: create configuration
        Document document = Jsoup.connect("https://ru.wikipedia.org/wiki/Хронология_распространения_COVID-19_в_России").get();
        Elements tables = document.select("table.wikitable");
        for (Element table : tables) {
            new Table(table);
            // todo: export to csv
        }
    }

    static class Table {
        final String[][] cells;

        Table(Element table) {
            int height = table.select("tr").size();
            cells = new String[height][];
            for (Element row : table.select("tr")) {
                row.select("th,td").stream().map(Table::cleanup).map(Cell::new).forEach(cell -> {
                    // todo: implement
                });
            }
        }

        static Element cleanup(Element cell) {
            cell.select(".collapseButton").remove();
            cell.select("a").forEach(link -> {
                if (link.attr("href").startsWith("#")) {
                    link.remove();
                }
            });
            return cell;
        }
    }

    static class Cell {
        final String text;
        final int width;
        final int height;

        Cell(Element cell) {
            text = cell.text();
            width = parseInt(cell.attr("colspan"));
            height = parseInt(cell.attr("rowspan"));
        }

        static int parseInt(String value) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }
}