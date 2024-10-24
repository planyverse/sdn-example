plugins {
    java
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "de.planyverse"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")

    // reduce boilerplate code with Lombok, versions managed by spring.io
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:junit-jupiter") // test database Cypher Queries
    testImplementation("org.testcontainers:neo4j") // test database Cypher Queries
    testCompileOnly("org.projectlombok:lombok") // @Slf4j annotation in tests
    testAnnotationProcessor("org.projectlombok:lombok") // @Slf4j annotation in tests
}

tasks.withType<Test> {
    useJUnitPlatform()
}
