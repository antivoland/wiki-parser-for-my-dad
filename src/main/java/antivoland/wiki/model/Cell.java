package antivoland.wiki.model;

import org.jsoup.nodes.Element;

/**
 * @author antivoland
 */
public class Cell {
    final String text;
    final int width;
    final int height;

    public Cell(Element cell) {
        text = cell.text();
        width = parseInt(cell.attr("colspan"));
        height = parseInt(cell.attr("rowspan"));
    }

    public Cell(String text, int width, int height) {
        this.text = text;
        this.width = width;
        this.height = height;
    }

    static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}