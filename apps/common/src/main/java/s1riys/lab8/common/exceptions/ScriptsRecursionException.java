package s1riys.lab8.common.exceptions;

/**
 * Thrown if scripts cause recursion
 */
public class ScriptsRecursionException extends Exception {
    public final String recursionCause;

    public ScriptsRecursionException(String recursionCause) {
        this.recursionCause = recursionCause;
    }
}
