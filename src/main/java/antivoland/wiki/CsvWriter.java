package antivoland.wiki;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author antivoland
 */
public class CsvWriter implements AutoCloseable {
    private static final CsvMapper MAPPER = new CsvMapper();

    private final SequenceWriter writer;

    public CsvWriter(Path file, char separator) throws IOException {
        CsvSchema schema = MAPPER.schemaFor(List.class).withColumnSeparator(separator);
        writer = MAPPER.writer(schema).writeValues(file.toFile());
    }

    public void write(List<?> row) throws IOException {
        writer.write(row);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}