package seedu.duke.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerUtil {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "/fintrack.log";
    private static boolean isInitialized = false;

    /**
     * Returns a logger for the given class.
     *
     * @param inputClass The class requesting the logger.
     * @return Configured logger instance.
     */
    public static synchronized Logger getLogger(Class<?> inputClass) {
        if (!isInitialized) {
            initializeLogging();
        }
        return Logger.getLogger(inputClass.getName());
    }

    /**
     * Initializes logging configuration.
     */
    private static void initializeLogging() {
        try {
            createLogDirectory();

            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(Level.INFO);

            // Prevent logs from also appearing in terminal
            rootLogger.setUseParentHandlers(false);

            clearExistingHandlers(rootLogger);

            Formatter formatter = createFormatter();
            FileHandler fileHandler = createFileHandler(formatter);

            rootLogger.addHandler(fileHandler);

            isInitialized = true;
        } catch (IOException e) {
            Logger.getLogger(LoggerUtil.class.getName()) .log(Level.SEVERE, "Failed to initialize logging system", e);
        }
    }

    /**
     * Creates the logs directory if it does not exist.
     */
    private static void createLogDirectory() throws IOException {
        Path logDirPath = Paths.get(LOG_DIR);
        if (!Files.exists(logDirPath)) {
            Files.createDirectories(logDirPath);
        }
    }

    /**
     * Removes and closes all existing handlers on the root logger.
     */
    private static void clearExistingHandlers(Logger rootLogger) {
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
            handler.close();
        }
    }

    /**
     * Creates the formatter used for log messages.
     */
    private static Formatter createFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format(
                        "[%1$tF %1$tT] [%2$s] %3$s - %4$s%n",
                        record.getMillis(),
                        record.getLevel().getName(),
                        record.getLoggerName(),
                        record.getMessage()
                );
            }
        };
    }

    /**
     * Creates and configures the file handler.
     */
    private static FileHandler createFileHandler(Formatter formatter) throws IOException {
        FileHandler fileHandler = new FileHandler(LOG_FILE, true);
        fileHandler.setLevel(Level.INFO);
        fileHandler.setEncoding("UTF-8");
        fileHandler.setFormatter(formatter);
        return fileHandler;
    }
}
