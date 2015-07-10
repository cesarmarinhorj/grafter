package grafter;
import clojure.lang.IMeta;
import clojure.lang.IPersistentMap;

/**
 * Exception class for Grafter that has support for clojure metadata.
 *
 */
public class GrafterException extends RuntimeException implements IMeta {
    public final IPersistentMap data;

    public GrafterException(String s, IPersistentMap data, Throwable throwable) {
        super(s, throwable);
        this.data = data;
    }

    public IPersistentMap meta() {
        return data;
    }

    public String toString() {
        // print the wrapped exception class (not GrafterException)
        return this.getCause().getClass().getName() + ": " + getMessage();
    }
}
