# 🧰 Spring Boot Scaffolder - Guide d'utilisation complet

Spring Boot Scaffolder est un outil puissant qui génère automatiquement la structure de code pour des applications Spring Boot en utilisant une architecture orientée domaine. Cette documentation présente toutes les fonctionnalités disponibles et comment les utiliser efficacement.

## 📋 Table des matières

1. [Structure générée](#structure-générée)
2. [Prérequis](#prérequis)
3. [Installation et configuration](#installation-et-configuration)
4. [Utilisation de la ligne de commande (CLI)](#utilisation-de-la-ligne-de-commande-cli)
5. [Génération sélective de composants](#génération-sélective-de-composants)
6. [Mode interactif](#mode-interactif)
7. [Personnalisation des templates](#personnalisation-des-templates)
8. [Bonnes pratiques](#bonnes-pratiques)
9. [Résolution de problèmes](#résolution-de-problèmes)
10. [Test de la bibliothèque en local](#test-de-la-bibliothèque-en-local)
11. [Contribuer au projet](#contribuer-au-projet)

## Structure générée

Spring Boot Scaffolder génère une structure orientée domaine comme celle-ci :

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

Les composants suivants sont générés pour chaque entité :
- **Entity** : Classe JPA avec annotations
- **DTO** : Objet de transfert de données
- **Repository** : Interface Spring Data JPA
- **Service** : Interface définissant les opérations métier
- **ServiceImpl** : Implémentation concrète du service
- **Controller** : Endpoints REST
- **Mapper** : Conversion entre Entity et DTO (avec MapStruct)

## Prérequis

### Pour Gradle

```groovy
dependencies {
    // Spring Boot et Spring Data JPA sont requis
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    
    // Support pour Java Record, Optional, Stream et types temporels
    implementation 'com.fasterxml.jackson.module:jackson-module-parameter-names'
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jdk8'
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'

    // MapStruct pour le mapping Entity <-> DTO
    implementation 'org.mapstruct:mapstruct:1.6.3'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.6.3'

    // Lombok (optionnel mais recommandé)
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

### Pour Maven

```xml
<properties>
    <org.mapstruct.version>1.6.3</org.mapstruct.version>
</properties>

<dependencies>
    <!-- Spring Boot et Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- MapStruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>
    
    <!-- Support pour Java Record, Optional, Stream et types temporels -->
    <dependency>
        <groupId>com.fasterxml.jackson.module</groupId>
        <artifactId>jackson-module-parameter-names</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jdk8</artifactId>
    </dependency>
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
            <version>3.11.0</version>
            <configuration>
                <source>17</source> <!-- Ajustez selon votre version de Java -->
                <target>17</target> <!-- Ajustez selon votre version de Java -->
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok-mapstruct-binding</artifactId>
                        <version>0.2.0</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Installation et configuration

### Avec Gradle

```groovy
// Dans votre build.gradle
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath "io.github.grafx1.scaffolder:spring-boot-scaffolder:2.1.1"
  }
}

apply plugin: 'io.github.grafx1.scaffolder'
```

### Avec Maven

```xml
<!-- Dans votre pom.xml -->
<dependency>
  <groupId>io.github.grafx1.scaffolder</groupId>
  <artifactId>spring-boot-scaffolder</artifactId>
  <version>2.1.1</version>
</dependency>

<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>3.1.0</version>
  <executions>
    <execution>
      <id>scaffold</id>
      <phase>none</phase> <!-- Exécution manuelle -->
      <goals><goal>java</goal></goals>
    </execution>
  </executions>
  <configuration>
    <mainClass>io.github.grafx1.scaffolder.cli.SpringBootScaffolderCLI</mainClass>
    <classpathScope>runtime</classpathScope>
    <arguments>
      <argument>${entityName}</argument>
      <argument>${withTests}</argument>
    </arguments>
  </configuration>
</plugin>
```

## Utilisation de la ligne de commande (CLI)

### Installation du CLI

Pour faciliter l'utilisation, utilisez les scripts `sbs` (Linux/macOS) ou `sbs.bat` (Windows) fournis dans le dossier `build/scripts/`. Rendez-les exécutables et ajoutez-les à votre PATH :

```bash
# Linux/macOS
chmod +x /chemin/vers/spring-boot-scaffolder/build/scripts/sbs
ln -s /chemin/vers/spring-boot-scaffolder/build/scripts/sbs /usr/local/bin/sbs

# Windows - Ajoutez le chemin aux variables d'environnement ou créez un raccourci
```

### Syntaxe de base

```
sbs generate|g <type> <nom1>[,nom2,...] [options]
```

Où :
- `generate` ou `g` est la commande principale
- `<type>` est le type de composant à générer (voir ci-dessous)
- `<nom1>[,nom2,...]` sont les noms d'entités, séparés par des virgules
- `[options]` sont des options supplémentaires comme `--withTests`

### Types de composants disponibles

| Type long     | Alias | Description                             |
|--------------:|:-----:|:----------------------------------------|
| `entity`      | `e`   | Génère uniquement des entités JPA       |
| `dto`         | `d`   | Génère uniquement des DTOs              |
| `repository`  | `r`   | Génère uniquement des repositories      |
| `service`     | `s`   | Génère les interfaces et impls service  |
| `controller`  | `c`   | Génère uniquement des controllers REST  |
| `mapper`      | `m`   | Génère uniquement des mappers MapStruct |
| `crud`        |       | Génère tous les composants ci-dessus    |

### Options

| Option        | Description                             |
|--------------:|:----------------------------------------|
| `--withTests` | Génère également des tests unitaires    |

## Génération sélective de composants

### Générer uniquement des entités

```bash
sbs g entity User,Product
# ou avec l'alias
sbs g e User,Product
```

**Résultat :** Génère uniquement `UserEntity.java` et `ProductEntity.java`.

### Générer uniquement des DTOs

```bash
sbs g dto Customer
# ou avec l'alias
sbs g d Customer
```

**Résultat :** Génère uniquement `CustomerDto.java`.

### Générer uniquement des repositories

```bash
sbs g repository Order,Invoice
# ou avec l'alias
sbs g r Order,Invoice
```

**Résultat :** Génère uniquement `OrderRepository.java` et `InvoiceRepository.java`.

### Générer uniquement des services

```bash
sbs g service Payment
# ou avec l'alias
sbs g s Payment
```

**Résultat :** Génère `PaymentService.java` et `PaymentServiceImpl.java`.

### Générer uniquement des controllers

```bash
sbs g controller Category,Tag
# ou avec l'alias
sbs g c Category,Tag
```

**Résultat :** Génère uniquement `CategoryController.java` et `TagController.java`.

### Générer uniquement des mappers

```bash
sbs g mapper Address
# ou avec l'alias
sbs g m Address
```

**Résultat :** Génère uniquement `AddressMapper.java`.

### Générer tous les composants (CRUD complet)

```bash
sbs g crud User,Product --withTests
```

**Résultat :** Génère tous les composants pour les entités User et Product, ainsi que leurs tests unitaires.

## Mode interactif

Le mode interactif vous permet d'exécuter plusieurs commandes sans avoir à relancer le script :

```bash
sbs interactive
```

Un prompt `spring>` apparaîtra, où vous pourrez saisir vos commandes :

```
spring> g e User
spring> g c Product
spring> g crud Customer --withTests
spring> help
spring> exit
```

## Personnalisation des templates

Les templates utilisés pour la génération se trouvent dans le dossier `src/main/resources/templates/`. Vous pouvez les personnaliser selon vos besoins :

1. Copiez le dossier `templates` dans votre propre projet
2. Modifiez les fichiers `.ftl` selon vos besoins
3. Configurez le chemin vers vos templates personnalisés

## Bonnes pratiques

- Générez vos entités au début du développement
- Personnalisez les ensuite pour ajouter des validations, relations, etc.
- Utilisez les tests générés comme point de départ pour écrire vos tests réels
- Intégrez la génération dans votre workflow de développement

## Résolution de problèmes

### Problèmes courants

1. **Les classes ne sont pas générées au bon endroit**
   - Assurez-vous d'exécuter la commande à la racine de votre projet
   - Vérifiez la structure de vos packages

2. **Erreurs de compilation dans les mappers générés**
   - Vérifiez que MapStruct est correctement configuré
   - Assurez-vous que les annotations processors sont correctement configurés

3. **La classe PagedResponse n'est pas trouvée**
   - Cette classe est générée automatiquement dans le package dto de votre application
   - Si vous avez des erreurs, générez-la manuellement : `sbs g dto PagedResponse`

## Test de la bibliothèque en local

Pour tester et utiliser la bibliothèque Spring Boot Scaffolder en local, suivez ces étapes :

### 1. Construire le projet

Commencez par compiler et construire le projet :

```bash
# À la racine du projet spring-boot-scaffolder
./gradlew build
```

Cette commande compile le code, exécute les tests et génère les fichiers JAR dans le dossier `build/libs/`.

### 2. Installation locale

#### Option 1 : Installer dans votre dépôt local Maven

```bash
# À la racine du projet spring-boot-scaffolder
./gradlew publishToMavenLocal
```

Cela publiera la bibliothèque dans votre dépôt Maven local (`~/.m2/repository/`), vous permettant de l'utiliser dans vos projets locaux.

##### Résolution des problèmes de signature GPG

Si vous rencontrez une erreur comme celle-ci :
```
> Task :signMavenJavaPublication FAILED
A problem occurred starting process 'command 'gpg''
```

Vous avez deux options :

1. **Désactiver la signature GPG** (recommandé pour les tests locaux) :
   ```bash
   # Option 1: Avec une variable d'environnement
   export ORG_GRADLE_PROJECT_signingRequired=false
   ./gradlew publishToMavenLocal
   
   # Option 2: Avec une propriété Gradle
   ./gradlew publishToMavenLocal -PsigningRequired=false
   ```

2. **Configurer GPG correctement** (nécessaire seulement pour la publication externe) :
   - Installez GPG : `brew install gnupg` (macOS) ou `apt-get install gnupg` (Linux)
   - Générez une clé : `gpg --gen-key`
   - Utilisez cette clé dans votre fichier `~/.gradle/gradle.properties` :
     ```
     signing.keyId=VOTREKEYID
     signing.password=VOTREMOTDEPASSE
     signing.secretKeyRingFile=/chemin/vers/fichier/secring.gpg
     ```

#### Option 2 : Utiliser directement les fichiers JAR

Les fichiers JAR générés se trouvent dans le dossier `build/libs/` :
- `spring-boot-scaffolder-2.1.1.jar` - Le JAR principal
- `spring-boot-scaffolder-2.1.1-sources.jar` - Les sources
- `spring-boot-scaffolder-2.1.1-javadoc.jar` - La Javadoc

### 3. Utiliser les scripts CLI directement

Les scripts CLI sont générés dans le dossier `build/scripts/` :

```bash
# Rendre le script exécutable (Linux/macOS)
chmod +x build/scripts/sbs

# Exécuter en mode interactif
./build/scripts/sbs interactive

# Ou générer un composant
./build/scripts/sbs g crud User
```

### 4. Tester dans un projet Spring Boot

#### Projet Gradle

Dans le `build.gradle` de votre projet Spring Boot :

```groovy
buildscript {
    repositories {
        mavenLocal() // Ajouter pour utiliser le dépôt local
        mavenCentral()
    }
    dependencies {
        classpath "io.github.grafx1.scaffolder:spring-boot-scaffolder:2.1.1"
    }
}

plugins {
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
    id 'java'
}

apply plugin: 'io.github.grafx1.scaffolder'
```

Puis exécutez :

```bash
./gradlew scaffold -PentityName="User" -PwithTests=true
```

#### Projet Maven

Dans le `pom.xml` de votre projet Spring Boot :

```xml
<dependency>
    <groupId>io.github.grafx1.scaffolder</groupId>
    <artifactId>spring-boot-scaffolder</artifactId>
    <version>2.1.1</version>
</dependency>
```

Puis exécutez :

```bash
mvn exec:java -DmainClass=io.github.grafx1.scaffolder.cli.SpringBootScaffolderCLI -Dexec.args="g crud User --withTests"
```

### 5. Déboguer la bibliothèque

Pour déboguer la bibliothèque pendant son utilisation :

1. Créez une configuration de débogage dans votre IDE pointant vers `io.github.grafx1.scaffolder.cli.SpringBootScaffolderCLI`
2. Ajoutez les arguments nécessaires (ex: `g crud User`)
3. Lancez en mode débogage

## Contribuer au projet

Nous accueillons vos contributions ! Voici comment vous pouvez participer :

1. Consultez notre fichier [CONTRIBUTION_SUGGESTIONS.md](CONTRIBUTION_SUGGESTIONS.md)
2. Créez une issue pour discuter de votre proposition
3. Soumettez une pull request avec vos modifications

---

© 2023-2025 Spring Boot Scaffolder | Licence MIT
