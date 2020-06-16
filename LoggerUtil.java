
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.IOException;

public class LoggerUtil {
    public static Logger createLogger() throws IOException {
        LogManager lmg = LogManager.getLogManager();
        // lmg now contains a reference to the log manager.
        Logger logger = lmg.getLogger(Logger.GLOBAL_LOGGER_NAME);
        FileHandler logFileHandler = new FileHandler("logFiles\\logs.log");
        logger.addHandler(logFileHandler);
        return logger;
    }
}
