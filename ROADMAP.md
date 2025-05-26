
# 🧭 Spring Boot Scaffolder – Roadmap Produit

---

## ✅ Terminé

| Feature                          | Description                                                       | Priorité |
|----------------------------------|-------------------------------------------------------------------|----------|
| Support Gradle + Maven           | Plugin utilisable dans les deux environnements                    | Haute    |
| Parsing group/artifact           | Détection automatique du package à partir de `groupId`/`artifactId` | Haute |
| Multi-entities                   | Génération multiple via `-Pentity="User,Product"`                 | Haute    |
| Structuration DDD stricte        | Respect d’une architecture orientée domaine                       | Haute    |
| Publication Maven Central        | Plugin disponible via un dépôt public standard                    | Haute    |
| Génération des tests             | Génération automatique de tests pour Service, Controller, Mapper  | Haute    |
---

## 🚧 En cours

| Feature                          | Description                                                       | Priorité |
|----------------------------------|-------------------------------------------------------------------|----------|
| Génération ciblée                | Générer uniquement certains composants (Entity, DTO, etc.)        | Haute    |


---

## 💡 Idées / Backlog

| Feature                          | Description                                                       | Priorité |
|----------------------------------|-------------------------------------------------------------------|----------|
| Templates personnalisés          | Permettre de surcharger les templates Freemarker                  | Moyenne  |
| Tests unitaires avancés          | Cas d’erreurs, mocks, tests paramétrés                            | Haute    |
| Support architecture hexagonale  | CQRS / ports & adapters                                           | Moyenne  |
| Mode interactif (CLI wizard)     | Génération guidée par interface CLI                               | Basse    |
| Validation de classe existante   | Éviter les collisions en avertissant l’utilisateur                | Moyenne  |
| Support Kotlin                   | Génération de classes `.kt` via Freemarker                        | Basse    |
| Formulaire d’idées utilisateurs  | Collecte de feedback via Google Forms                             | Haute    |

---

## 🔬 À discuter

| Feature                          | Description                                                       | Priorité |
|----------------------------------|-------------------------------------------------------------------|----------|
| Plugin Intellij / VS Code        | Intégration via extension IDE                                     | Basse    |
| Support IA locale (LLM)          | Génération via modèle open source (CodeLLM, etc.)                 | Basse    |
| Génération de modules complets   | Création de module Spring Boot complet multi-layer                | Moyenne  |
