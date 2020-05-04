package antivoland.wiki.model;

import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

/**
 * @author antivoland
 */
class Row {
    final TreeMap<Integer, Cell> cells = new TreeMap<>();

    Cell firstCell() {
        return cells.get(0);
    }

    void set(int column, Cell cell) {
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

    List<String> cellValues() {
        return cells.values().stream().map(cell -> cell.text).collect(toList());
    }
}