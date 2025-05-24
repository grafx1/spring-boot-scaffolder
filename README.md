
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

## 📦 Building and Publishing the Plugin (via Gradle)

```bash
./gradlew clean build publishToMavenLocal
```

---

## 🛠️ Usage with a Gradle Project

### 1. Update your `settings.gradle`
```groovy
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
```

### 2. Add the plugin to your `build.gradle`
```groovy
plugins {
    id 'io.github.grafx1.scaffolder' version '2.0'
}

repositories {
	mavenLocal()
	mavenCentral()
}
```

### 3. Run the scaffold generator
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
  <version>2.0</version>
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
