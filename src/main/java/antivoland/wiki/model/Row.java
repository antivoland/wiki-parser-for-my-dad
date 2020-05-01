package antivoland.wiki.model;

import java.util.TreeMap;

/**
 * @author antivoland
 */
public class Row {
    private final TreeMap<Integer, Cell> cells = new TreeMap<>();

    public void set(int column, Cell cell) {
        cells.put(column, cell);
    }

    int lastUnsetColumn() {
        if (cells.isEmpty()) {
            return 0;
        }
        for (int column = 0; column < cells.lastKey(); ++column) {
            if (!cells.containsKey(column)) {
                return column;
            }
        }
        return cells.lastKey() + 1;
    }
}