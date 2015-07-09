package grafter;
import clojure.lang.IMeta;
import clojure.lang.IPersistentMap;

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
        return "" + this.getCause().getClass().getName() + ": " + getMessage();
    }
}
