package superawesome.tv.kwsappdemo;

/**
 * Created by gabriel.coman on 16/06/16.
 */
public class KWSSingleton {

    private static KWSSingleton instance = new KWSSingleton();
    private KWSSingleton() {}
    public static KWSSingleton getInstance(){
        return instance;
    }
    public KWSModel model = null;
}
