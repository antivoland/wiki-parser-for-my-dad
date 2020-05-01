package antivoland.wiki.model;

import org.jsoup.nodes.Element;

/**
 * @author antivoland
 */
class Cell {
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
            return 1;
        }
    }
}