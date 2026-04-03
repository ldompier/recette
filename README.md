# Recette

Application Spring Boot pour gerer des recettes avec PostgreSQL, JDBC SQL et Liquibase.

## Stack

- Java 21
- Spring Boot
- Spring JDBC
- PostgreSQL
- Liquibase

## Lancer PostgreSQL

```bash
docker compose up -d
```

## Demarrer l'application

```bash
mvn spring-boot:run
```

Note: le depot ne contient pas encore le wrapper Maven (`./mvnw`). Il faut donc avoir `mvn` installe localement, ou bien ajouter ensuite le wrapper Maven.

## API disponible

- `GET /api/recipes`
- `GET /api/recipes/{id}`
- `POST /api/recipes`
- `PUT /api/recipes/{id}`
- `DELETE /api/recipes/{id}`

## Exemple de payload

```json
{
  "title": "Lasagnes maison",
  "description": "Une recette familiale",
  "imageUrl": "https://example.com/images/lasagnes.jpg",
  "ingredients": [
    {
      "name": "Pates a lasagne",
      "quantity": "12 feuilles",
      "displayOrder": 1
    },
    {
      "name": "Sauce tomate",
      "quantity": "500 ml",
      "displayOrder": 2
    }
  ],
  "preparationSteps": [
    {
      "stepNumber": 1,
      "description": "Prechauffer le four a 180C."
    },
    {
      "stepNumber": 2,
      "description": "Monter les couches puis cuire 35 minutes."
    }
  ]
}
```
