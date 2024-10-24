package de.planyverse.sdn_example;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
@Data
public class MyModel {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;

    private String name;

    @Relationship(value = "REL_TO_MY_NESTED_MODEL")
    private MyModel myNestedModel;
}
