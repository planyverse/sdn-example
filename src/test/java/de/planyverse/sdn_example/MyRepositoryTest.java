package de.planyverse.sdn_example;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // make auto-wiring work in @SpringBootTest
class MyRepositoryTest {

    private final MyRepository myRepository;

    private static final Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:5.21.0-community")
            .withReuse(true);

    @DynamicPropertySource
    private static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }

    @BeforeAll
    public static void initContainerAndConnection() {
        neo4jContainer.start();
    }

    @Test
    void getAllProjects() {
        // set up data in database
        MyModel myNestedModel = new MyModel();
        myNestedModel.setName("nested");

        MyModel myRootModel = new MyModel();
        myRootModel.setName("root");
        myRootModel.setMyNestedModel(myNestedModel);

        String uuid = myRepository.save(myRootModel).getUuid();

        // first, try built-in repository method for getting data out of database
        Optional<MyModel> rootModelFromDb = myRepository.findById(uuid);
        assertTrue(rootModelFromDb.isPresent());
        assertNotNull(rootModelFromDb.get().getMyNestedModel());

        // second, try custom query
        Optional<MyModel> rootModelFromDbCustom = myRepository.getByUuidCustomQuery(uuid);
        assertTrue(rootModelFromDbCustom.isPresent());
        assertNotNull(rootModelFromDbCustom.get().getMyNestedModel()); // <-- fails here starting from SDN 3.3.2
    }
}
