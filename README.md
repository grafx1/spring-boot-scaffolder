
# 🧰 Spring Boot Scaffolder Plugin

> Générateur de code pour Spring Boot : entités, DTO, repositories, services, controllers et mappers, basé sur FreeMarker.

---

## 🚀 Installation

Ajoutez ce plugin à votre projet Spring Boot :

### 1. Dans `settings.gradle` (si local)
#### 1.1 Sans publication sur le maven local
```groovy
pluginManagement {
    includeBuild('../spring-boot-scaffolder') // chemin relatif vers le plugin
}
```
#### 1.2 Avec publication sur le maven local
```groovy
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
```


### 2. Dans `build.gradle` du projet utilisateur :
```groovy
plugins {
    id "sn.consultit.scaffolder" version "1.0.0"
}
```

---

## ⚙️ Prérequis

- Java 21+
- Gradle 8.10+
- Structure classique Spring Boot : `src/main/java/...`

---

## 🛠 Utilisation

### 🔨 Générer une seule classe :
```bash
./gradlew scaffold -Pfqcn="sn.example.domain.User"
```

### 🔨 Générer plusieurs classes d’un coup :
```bash
./gradlew scaffold -Pfqcn="sn.example.domain.User,sn.example.domain.Product"
```

### ❓ Afficher l’aide :
```bash
./gradlew scaffold -PshowHelp=true
```

---

## 📦 Fichiers générés

Pour chaque entité, le plugin génère :

```
src/main/java/{package}/entity/{ClassName}Entity.java
src/main/java/{package}/dto/{ClassName}Dto.java
src/main/java/{package}/repository/{ClassName}Repository.java
src/main/java/{package}/service/{ClassName}Service.java
src/main/java/{package}/service/impl/{ClassName}ServiceImpl.java
src/main/java/{package}/controller/{ClassName}Controller.java
src/main/java/{package}/mapper/{ClassName}Mapper.java
```

Les fichiers sont générés à partir des templates FreeMarker dans `resources/templates`.

---

## 📁 Exemple

```bash
./gradlew scaffold -Pfqcn="com.example.hr.Employee"
```

Créera automatiquement les fichiers :

```
src/main/java/com/example/hr/entity/EmployeeEntity.java
src/main/java/com/example/hr/dto/EmployeeDto.java
...
```

---

## 📐 Templates

Les templates sont définis dans :
```
src/main/resources/templates/*.ftl
```

> Vous pouvez personnaliser vos propres templates dans un fork ou une extension du plugin.

---

## ⚠️ Conseils

- ❗ N’utilisez pas `-Pclass` ou `-Phelp`, ce sont des mots réservés par Gradle.
- ✅ Préférez `-Pfqcn` pour la/les classes, `-PshowHelp=true` pour l’aide.
- 📁 Les chemins sont générés automatiquement selon le `FQCN`.

---

## 🔧 Débogage

Pour afficher le répertoire courant du projet appelant :
```bash
./gradlew scaffold -Pfqcn="com.example.MyEntity" --info
```

Pour voir les logs détaillés :
```bash
./gradlew scaffold -Pfqcn="com.example.MyEntity" --stacktrace
```

---

## 📣 Contribution

Les pull requests sont les bienvenues !  
Merci de suivre la structure standard Spring + FreeMarker.


