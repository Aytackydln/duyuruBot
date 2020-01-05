package com.noname.duyuru.app.jpa.repositories;

import com.noname.duyuru.app.jpa.models.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration,String> {
}
