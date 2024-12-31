# Product Management Service

## Description
This is a simple CRUD application that uses the REST APIs for Product. It uses the Spring Boot Framework for the API and the data is stored using MySQL.

## Features
- [Add a new product.](#add-a-product)
- [Retrieve details of a product.](#retrieve-a-product)
- [Update details of an existing book.](#update-a-book)
- [Delete an existing product](#delete-book)

## Technologies Used
- **Spring Boot**: A Java-based framework used for building production-ready, stand-alone, and microservice-oriented web applications.
- **MySQL**: An open-source relational database management system widely used for storing and managing structured data.
- **Java**: A versatile, platform-independent programming language utilized for developing the API and backend logic.

## Database Schema

### Table: `product_table`
| Column         | Type           | Constraints |
|----------------|----------------|-------------|
| `product_id`   | VARCHAR(36)    | PRIMARY KEY |
| `product_name` | VARCHAR(100)   | NOT NULL    |
| `product_desc` | VARCHAR(350)   |             |
| `price`        | DECIMAL(10, 2) | NOT NULL    |

## Setup Instructions
1. Download Java 17 and verify Gradle installation
   1. Download Java from [Oracle Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [AdoptOpenJDK](https://adoptium.net/).  
      Verify the installation by running:
        ```bash
        java -version
        ```
        Ensure it outputs Java 17.
   2. **Gradle**: Verify Gradle is installed by running:
        ```bash
        gradle -v
        ```
        Alternatively, use the Gradle wrapper provided in the project (`./gradlew`).
2. Link Gradle project
   1. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse). 
   2. Import it as a Gradle project using the build.gradle file.
3.  Build the Project
    Run the following command to build and verify the project:
    ```bash
    ./gradlew build
    ```

---

## Running on Docker
1. Open terminal
2. Run ``./gradlew clean build``
3. Run ``docker compose down``
4. Run ``docker compose up -d --build``

## API Endpoints

### Add a Product
**POST** `/api/product/v1/create`

#### Request Body
```json
{
   "productName": "The Lord of the Rings",
   "productDesc": "Some Desc",
   "price": 12.00
}
```

#### Response
- **201 Created**
- **400 Bad Request** (if required fields are missing)

#### Success Sample Response
```json
{
   "status": "SUCCESS",
   "result": {
      "productId": "be5d7880-e335-4126-8424-d65cde759f21"
   }
}
```

#### Failure Sample Response
```json
{
   "status": "ERROR",
   "result": {
      "errors": [
         {
            "errorCode": "FIELD_VALIDATION_ERROR",
            "errorMessage": "Price cannot be empty"
         },
         {
            "errorCode": "FIELD_VALIDATION_ERROR",
            "errorMessage": "Product name cannot be empty"
         }
      ]
   }
}

```

### Retrieve a Product
**GET** `/api/product/v1/retrieve/details?productId=<productId>`

#### Response
- **200 OK**
- **404 Not Found** (if the product does not exist)

#### Non-empty Sample Response
```json
{
   "status": "SUCCESS",
   "result": {
      "productId": "be5d7880-e335-4126-8424-d65cde759f21",
      "productName": "Sample Product",
      "productDesc": "Sample Product Description",
      "price": 99.99
   }
}

```

#### Failure Sample Response
```json
{
   "status": "ERROR",
   "result": {
      "error": {
         "errorCode": "PROD_NOT_FOUND",
         "errorMessage": "Product not found"
      }
   }
}
```

### Update a Book
**PUT** `/api/product/v1/update`

#### Request Body
```json
{
   "productId": "53c7f6e9-0ef7-47a0-8508-b8f01472b150",
   "productName": "The Lord of the Rings",
   "productDesc": "<null>",
   "price": 12.00
}
```

#### Response
- **200 OK**
- **404 Not Found** (if the product does not exist)
- **400 Bad Request** (invalid fields)

#### Success Sample Response
```json
{
   "status": "SUCCESS",
   "result": {
      "productId": "53c7f6e9-0ef7-47a0-8508-b8f01472b150",
      "productName": "The Lord of the Rings",
      "productDesc": null,
      "price": 12.00
   }
}
```

#### Failure Sample Response (Product does not exists)
```json
{
   "status": "ERROR",
   "result": {
      "error": {
         "errorCode": "PROD_NOT_FOUND",
         "errorMessage": "Product not found"
      }
   }
}
```

#### Failure Sample Response (Invalid Fields)
```json
{
   "status": "ERROR",
   "result": {
      "errors": [
         {
            "errorCode": "FIELD_VALIDATION_ERROR",
            "errorMessage": "Price must be positive"
         },
         {
            "errorCode": "FIELD_VALIDATION_ERROR",
            "errorMessage": "Product Id is invalid"
         }
      ]
   }
}
```

### Delete Book
**GET** `/api/product/v1/delete`

#### Request Body
```json
{
   "productId": "53c7f6e9-0ef7-47a0-8508-b8f01472b150"
}
```

#### Response
- **200 OK**
- **404 Not Found** (if product does not exist)

#### Success Sample Response
```json
{
   "status": "SUCCESS",
   "result": null
}
```

#### Failure Sample Response
```json
{
   "status": "ERROR",
   "result": {
      "error": {
         "errorCode": "PROD_NOT_FOUND",
         "errorMessage": "Product not found"
      }
   }
}
```