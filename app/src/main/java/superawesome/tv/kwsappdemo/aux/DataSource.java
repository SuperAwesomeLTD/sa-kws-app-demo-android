package superawesome.tv.kwsappdemo.aux;

/**
 * Created by gabriel.coman on 15/08/16.
 */
public interface DataSource {
    void update(DataSourceInterface start, DataSourceInterface success, DataSourceInterface error);
}
