package antivoland.sandbox.covid19;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.lang.String.format;

/**
 * @author antivoland
 */
public class Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://ru.wikipedia.org/wiki/Хронология_распространения_COVID-19_в_России").get();
        LOGGER.info(format("Title: %s", document.title()));

        Elements tables = document.select("table.wikitable");
        for (Element table : tables) {
            // todo: parse and export to csv;
        }
    }
}