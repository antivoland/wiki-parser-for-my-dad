package antivoland.wiki;

import antivoland.wiki.model.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static antivoland.wiki.Utils.appPath;
import static java.lang.String.format;

/**
 * @author antivoland
 */
public class Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    public static void main(String[] args) throws IOException {
        LOGGER.info(format("Application path: %s", appPath()));

        Config config = Config.read();
        for (Config.Profile profile : config.profiles) {
            Path outPath = appPath().resolve(profile.outPath);
            AtomicInteger tableNo = new AtomicInteger();
            Document document = Jsoup.connect(profile.inUrl).get();
            document.select("table.wikitable").stream()
                    .map(table -> new Table(table, tableNo.incrementAndGet()))
                    .forEach(table -> {
                        try {
                            table.export(outPath);
                        } catch (IOException e) {
                            LOGGER.warn(format("Failed to export table #%s ('%s') from '%s'",
                                    table.no(), table.name(), profile.inUrl));
                        }
                    });
            LOGGER.info(format("Exported tables from '%s' to '%s'", profile.inUrl, outPath));
        }
    }
}