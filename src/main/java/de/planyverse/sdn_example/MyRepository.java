package de.planyverse.sdn_example;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MyRepository extends CrudRepository<MyModel, String> {

    @Query("""
            MATCH (root:MyModel {uuid: $uuid})
            RETURN root {
                     .*, MyModel_REL_TO_MY_NESTED_MODEL_MyModel: [
                       (root)-[:REL_TO_MY_NESTED_MODEL]->(nested:MyModel) | nested {. *}
                     ]
                   }
            """)
    Optional<MyModel> getByUuidCustomQuery(String uuid);
}
