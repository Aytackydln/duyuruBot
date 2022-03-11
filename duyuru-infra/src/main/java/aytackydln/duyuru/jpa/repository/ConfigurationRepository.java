package aytackydln.duyuru.jpa.repository;

import aytackydln.duyuru.jpa.entity.ConfigurationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends CrudRepository<ConfigurationEntity,String> {
}
