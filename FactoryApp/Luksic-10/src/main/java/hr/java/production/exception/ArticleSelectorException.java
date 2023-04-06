package hr.java.production.exception;

/**
 * sluzi za bacanje oznacene iznimke u slucaju visestrukog unosa istog artikla
 */
public class ArticleSelectorException extends Exception{
    /**
     * konstruktor koji prima poruku iznimke
     * @param message poruka koja ce se ispisati na ekranu u slucaju bacanja iznimke
     */
    public ArticleSelectorException(String message) {
        super(message);
    }

    /**
     * konstruktor koji prima poruku i uzrok iznimke
     * @param message poruka koja ce se ispisati na ekranu u slucaju bacanja iznimke
     * @param cause Throwable uzrok iznimke
     */

    public ArticleSelectorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * konstruktor koji prima uzrok iznimke
     * @param cause Throwable  uzrok iznimke
     */

    public ArticleSelectorException(Throwable cause) {
        super(cause);
    }
}
