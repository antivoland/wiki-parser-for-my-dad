package antivoland.wiki;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static antivoland.wiki.Utils.appPath;
import static java.lang.String.format;

/**
 * @author antivoland
 */
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    public char separator;
    public String encoding;
    public List<Profile> profiles;

    public static class Profile {
        public String inUrl;
        public String outPath;
    }

    public static Config read() {
        Path file = appPath().resolve("extractor.yml");
        if (!Files.exists(file)) {
            LOGGER.error(format("Configuration file '%s' not found", file));
            throw new RuntimeException(format("Configuration file '%s' not found", file));
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(file.toFile(), Config.class);
        } catch (IOException e) {
            LOGGER.error(format("Failed to read configuration file '%s'", file), e);
            throw new RuntimeException(format("Failed to read configuration file '%s'", file), e);
        }
    }
}