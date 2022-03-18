package aytackydln.duyuru.mapper.conf;

import org.hibernate.Hibernate;
import org.mapstruct.Condition;

import java.util.Collection;

public class LazyMappingConfig {

    @Condition
    public static <T> boolean isLazyCollectionAvailable(Collection<T> collection) {
        if (collection == null) {
            return false;
        }

        return Hibernate.isInitialized(collection);
    }
}
