package nl.utwente.di.OVSoftware;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class MyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register resources and features
        classes.add(MultiPartFeature.class);
        classes.add(TestResource.class);
        classes.add(LoginResource.class);
        classes.add(MainResource.class);
        classes.add(CsvWriter.class);
        classes.add(GoogleLoginResource.class);
        return classes;
    }
}
