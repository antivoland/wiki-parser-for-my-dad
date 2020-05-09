package antivoland.wiki;

import antivoland.wiki.model.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static antivoland.wiki.Utils.appPath;
import static java.lang.String.format;
import static java.util.Comparator.reverseOrder;

/**
 * @author antivoland
 */
public class Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    public static void main(String[] args) {
        LOGGER.info(format("Application path: %s", appPath()));

        Config config = Config.read();
        Table.Exporter exporter = new Table.Exporter(config);
        for (Config.Profile profile : config.profiles) {
            Path outPath = appPath().resolve(profile.outPath);
            try {
                reset(outPath);
            } catch (IOException e) {
                LOGGER.error(format("Failed to cleanup directory '%s'", outPath), e);
                throw new RuntimeException(format("Failed to cleanup directory '%s'", outPath), e);
            }
            tables(profile.inUrl).forEach(table -> {
                try {
                    exporter.export(table, outPath);
                } catch (IOException e) {
                    LOGGER.warn(format("Failed to export table #%s ('%s') from '%s'",
                            table.no(), table.name(), profile.inUrl));
                }
            });
            LOGGER.info(format("Exported tables from '%s' to '%s'", profile.inUrl, outPath));
        }
    }

    private static Stream<Table> tables(String url) {
        AtomicInteger no = new AtomicInteger();
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            LOGGER.warn(format("Failed to read '%s'", url));
            return Stream.of();
        }
        return document.select("table.wikitable").stream()
                .map(table -> new Table(table, no.incrementAndGet()));
    }

    public static void reset(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            return;
        }
        Files.walk(path).sorted(reverseOrder()).forEach(leaf -> {
            try {
                Files.delete(leaf);
            } catch (IOException e) {
                LOGGER.error(format("Failed to delete '%s'", leaf), e);
                throw new RuntimeException(format("Failed to delete '%s'", leaf), e);
            }
        });
        Files.createDirectories(path.resolve("segments"));
    }
}