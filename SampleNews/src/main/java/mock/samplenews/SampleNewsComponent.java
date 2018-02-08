package mock.samplenews;
import javax.inject.Singleton;

import dagger.Component;
import mock.samplenews.service.ServiceDataClass;
import mock.samplenews.utils.JsonUtils;


@Singleton
@Component(modules = {JsonUtils.class ,ServiceDataClass.class})
public interface SampleNewsComponent {
    JsonUtils getJsonUtils();
    ServiceDataClass getService();
}