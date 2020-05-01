package antivoland.wiki;

import antivoland.wiki.model.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
        document.select("table.wikitable").stream().map(Table::new).forEach(table -> {
            // todo: export to csv
        });
    }
}