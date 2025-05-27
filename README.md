
# 🧰 Spring Boot Scaffolder Plugin - User Guide

This plugin automatically generates the structure of a Spring Boot module using a **domain-oriented package layout**:

```
📂com
  📂 example
    📂 myapplication
        - MyApplication.java
        |
        📂 customer
        |  📂 entity
        |     - CustomerEntity.java
        |  📂 controller
        |      - CustomerController.java
        |  📂 service
        |      📂 impl
        |         - CustomerServiceImpl.java
        |       - CustomerService.java
        |  📂 repository
        |      - CustomerRepository.java
        |  📂 dto
        |      - CustomerDto.java
        |  📂 mapper
        |      - CustomerMapper.java
```

It scaffolds the following layers for each entity:
- Entity
- DTO
- Repository
- Service
- Controller
- Mapper

---


## Required Dependencies

### If you're using Gradle

```groovy
dependencies {
	...
	// Java record support
	implementation 'com.fasterxml.jackson.module:jackson-module-parameter-names'
	
	// Support for Optional, Stream, etc.
	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jdk8'
	
	// Support for java.time.* types (LocalDate, Instant, etc.)
	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'

	// Mapstruct
	implementation 'org.mapstruct:mapstruct:1.6.3'
	annotationProcessor 'org.mapstruct:mapstruct-processor:1.6.3'

	// Lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

	...
}
```

### If you're using Maven
```xml
<properties>
	<org.mapstruct.version>1.6.3</org.mapstruct.version>
</properties>
<dependencies>
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Mapstruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>
    <!-- Java record support -->
    <dependency>
	  <groupId>com.fasterxml.jackson.module</groupId>
	  <artifactId>jackson-module-parameter-names</artifactId>
     </dependency>
	
     <!-- Support for Optional, Stream, etc. -->
     <dependency>
	  <groupId>com.fasterxml.jackson.datatype</groupId>
	  <artifactId>jackson-datatype-jdk8</artifactId>
     </dependency>
	
     <!-- Support for java.time.* types (LocalDate, Instant, etc.) -->
     <dependency>
	  <groupId>com.fasterxml.jackson.datatype</groupId>
	  <artifactId>jackson-datatype-jsr310</artifactId>
      </dependency>

</dependencies>

<build>
    <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.8.1</version>
          <configuration>
                <source>1.8</source> <!-- depending on your project -->
                <target>1.8</target> <!-- depending on your project -->
                <annotationProcessorPaths>
                   <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                  </path>
                  <!-- other annotation processors -->
                </annotationProcessorPaths>
            </configuration>
        </plugin>

	<!-- Lombok -->
	<plugin>
	      <groupId>org.apache.maven.plugins</groupId>
	        <artifactId>maven-compiler-plugin</artifactId>
		<configuration>
		  <annotationProcessorPaths>
		    <path>
			<groupId>org.projectlombok</groupId>
		  	<artifactId>lombok</artifactId>
		    </path>
		  </annotationProcessorPaths>
	        </configuration>
         </plugin>
         <plugin>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-maven-plugin</artifactId>
	        <configuration>
	          <excludes>
	                <exclude>
	                  <groupId>org.projectlombok</groupId>
	                  <artifactId>lombok</artifactId>
	                </exclude>
	          </excludes>
	        </configuration>
         </plugin>
    </plugins>
</build>
```

## Manual Registration for Jackson (Optional)
Spring Boot often auto-configures these modules if they're available on the classpath. Otherwise, you can register them manually like this:
```java
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}

```

### Why this is important

Without these modules, Jackson may not be able to properly deserialize:
- Java `record` (unrecognized implicit constructors)
- `Optional` or `Stream` types
- Dates and times (`LocalDate`, `ZonedDateTime`, etc.)
---

## 🛠️ Usage with a Gradle Project

### 1. Add the plugin to your `build.gradle`
```groovy
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath "io.github.grafx1.scaffolder:spring-boot-scaffolder:2.1.1"
  }
}

plugins {
   ...
}

apply plugin: 'io.github.grafx1.scaffolder'
repositories {
    mavenCentral()
}
```

### 2. Run the scaffold generator
```bash
./gradlew scaffold -PentityName="Customer,Invoice" -PwithTests=true
```

---

## 📦 Usage with a Maven Project

### 1. Add the dependency in `pom.xml`

```xml
<dependency>
  <groupId>io.github.grafx1.scaffolder</groupId>
  <artifactId>spring-boot-scaffolder</artifactId>
  <version>2.1.1</version>
</dependency>
```

### 2. Configure `exec-maven-plugin`

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>3.1.0</version>
  <executions>
    <execution>
      <id>scaffold</id>
      <phase>none</phase> <!-- Run manually -->
      <goals><goal>java</goal></goals>
    </execution>
  </executions>
  <configuration>
    <mainClass>io.github.grafx1.scaffolder.Main</mainClass>
    <classpathScope>runtime</classpathScope>
    <arguments>
      <argument>${entityName}</argument>
      <argument>${withTests}</argument>
    </arguments>
  </configuration>
</plugin>
```

### 3. Launch the scaffolding process

```bash
mvn exec:java -DentityName="Customer,Invoice" -DwithTests="true"
```

---

## 📂 Output Structure

The scaffolder will generate:
- Business logic files in `src/main/java`
- Test files in `src/test/java` if `withTests=true` ✅

  _______________________________________________________________________

## v2.1.1 Release Notes

### Key Features:
- Replaced `PageImpl` with a generated `PagedResponse<T>` wrapper for stable pagination
- `PagedResponse` class is now generated automatically if it doesn’t exist
- README updated with recommended Jackson dependencies for proper record serialization

### Bug Fixes:
- Auto-normalization of artifactIds with dashes (`-`) → `my-project` becomes `my_project`
- `version` field is now automatically ignored in MapStruct mappers

