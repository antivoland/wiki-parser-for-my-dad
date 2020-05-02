package antivoland.wiki;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author antivoland
 */
public class Utils {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Extractor.class);

    public static Path appPath() {
        Path path;
        try {
            path = Paths.get(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            LOGGER.warn("Failed to resolve application path", e);
            path = Paths.get(".");
        }
        return Files.isDirectory(path) ? path : path.getParent();
    }

    public static class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {
        private boolean started = false;

        @Override
        public boolean isStarted() {
            return started;
        }

        @Override
        public void start() {
            if (started) return;
            context.putProperty("LOG_DIRECTORY", appPath().toString());
            started = true;
        }

        @Override
        public void stop() {
            // do nothing
        }

        @Override
        public boolean isResetResistant() {
            return true;
        }

        @Override
        public void onStart(LoggerContext context) {
            // do nothing
        }

        @Override
        public void onReset(LoggerContext context) {
            // do nothing
        }

        @Override
        public void onStop(LoggerContext context) {
            // do nothing
        }

        @Override
        public void onLevelChange(Logger logger, Level level) {
            // do nothing
        }
    }
}