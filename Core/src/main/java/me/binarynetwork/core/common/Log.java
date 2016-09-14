package me.binarynetwork.core.common;

/**
 * Created by Bench on 9/14/2016.
 */
public class Log {
    private Log() {}

    public static void main(String[] args)
    {
        infof("Format test: %s", "hello");
    }

    public static void info(Object object)
    {
        log(LogLevel.INFO, object);

    }

    public static void infof(String format, Object... args)
    {
        log(LogLevel.INFO, String.format(format, args));
    }

    public static void debug(Object object)
    {
        log(LogLevel.DEBUG, object);
    }

    public static void debugf(String format, Object... args)
    {
        log(LogLevel.DEBUG, String.format(format, args));
    }

    public static void error(Object object)
    {
        log(LogLevel.ERROR, object);
    }

    public static void errorf(String format, Object... args)
    {
        log(LogLevel.DEBUG, String.format(format, args));
    }

    public static void log(LogLevel level, Object object)
    {
        switch (level) {
            case INFO: System.out.println("[Info] " + object);
                break;
            case DEBUG: System.err.println("[Debug] " + object);
                break;
            case ERROR: System.err.println("[Error] " + object);
        }
    }

    enum LogLevel {
        INFO,
        DEBUG,
        ERROR;
    }
}
