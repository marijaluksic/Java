package hr.java.production.exception;

/**
 *sluzi za bacanje neoznacene iznimke u slucaju visestrukog unosa istog naziva kategorije
 */

public class CategoryCreatorException extends RuntimeException{
    /**
     * konstruktor koji prima poruku iznimke
     * @param message unos poruke koja ce se ispisati na ekranu u slucaju bacanja iznimke
     */
    public CategoryCreatorException(String message) {
        super(message);
    }

    /**
     * konstruktor koji prima poruku i uzrok iznimke
      * @param message poruka koja ce se ispisati na ekranu u slucaju bacanja iznimke
     * @param cause Throwable uzrok iznimke
     */
    public CategoryCreatorException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * konstruktor koji prima uzrok iznimke
     * @param cause Throwable  uzrok iznimke
     */
    public CategoryCreatorException(Throwable cause) {
        super(cause);
    }
}
