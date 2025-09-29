# Sticker Collection Application

This project was developed as a group project at Pompeu Fabra University. It implements a sticker collection application using Spring Boot and is based on Component-Based Architecture. Below, we explain the key features, challenges, and solutions implemented during development, along with additional noteworthy aspects of the project.

## Contributors

- [@JordiMartinezPl](https://github.com/JordiMartinezPl) 
- [@dvpotato](https://github.com/dvpotato) 
- [@FerranMartinez27](https://github.com/FerranMartinez27) 

## **Key Features and Functionality**

### 1. **Custom Functionality in H2**
During development, we encountered limitations with the H2 database, specifically the lack of a built-in function to sum two values within a query. Instead of performing multiple requests to H2, we opted to create our own function that could be used directly in queries.

**Implementation:**
- A class called `DatabaseInitializer` was created to initialize custom functions in H2.
- Marked with `@Component`, this class automatically integrates with Spring Boot's dependency injection.
- The `DatabaseInitializer` registers an alias for the custom sum function, which is defined in another class, `MathUtils`, containing a simple `add(int a, int b)` method.
  
*Problems:*
  
  During the implementation of the custom sum functionality, we encountered issues when running tests in GitHub Actions. The problem arose because the `CREATE ALIAS` statement for the sum function caused an error, stating that the method already existed.
  To resolve this, we modified the `CREATE ALIAS` statement to use `CREATE ALIAS IF NOT EXISTS`. This ensures that the alias is only created if it doesn't already exist, preventing duplication errors during execution.
```java

    @PostConstruct
    public void initializeDatabase() {
        String createFunction = """         
                CREATE ALIAS IF NOT EXISTS add_numbers FOR "com.labproject.component.MathUtils.addNumbers";
""";

        jdbcTemplate.execute(createFunction);
    }
````

  
This approach optimized database interactions and provided the flexibility to extend H2's functionality as needed.

[For more details about H2 functions, visit H2 Functions Documentation.](https://www.h2database.com/html/functions.html)

---

### 2. **Age Restriction Validation**
Applications that include payment functionalities must adhere to legal regulations, such as requiring users to be 13 years or older. To enforce this, we implemented a custom validation annotation, `@AtLeastThirteenYears`, which is a custom `ConstraintValidator`.

**Implementation:**
- The validator checks the user's date of birth and ensures they meet the age requirement.
- This annotation can be applied to any entity or DTO field, making it reusable across the application.

This approach ensures compliance with regulations while keeping the validation logic centralized and modular.

[For more details about custom validators, visit Spring Documentation.](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html)

---

### 3. **Flexible Section Representation in `VisualizeAlbumDTO`**
The application required two different representations for album sections:
- A list of stickers within each section.
- The count of stickers in each section.

To avoid creating two nearly identical DTOs, we made the `sections` field in `VisualizeAlbumDTO` of type `Object`. This allows the same DTO to store both representations dynamically.

**Implementation:**
- Data is processed to fit the required structure before populating the `VisualizeAlbumDTO`.
- Using Java's `Collections` and `Collectors` packages, we utilized methods like `singletonList()` to convert a list of objects into a single object dynamically, depending on the visualization requirement.

[For more information, visit Collections in Java.](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html)

---
### 4. **Exception Handling**
The application employs a centralized exception-handling mechanism using Spring Boot's `@ControllerAdvice`. This ensures that exceptions are managed consistently across the project.

**Implementation:**
- Custom exceptions like `NotFoundException` and `InvalidParamsException` are handled in a global `GlobalExceptionHandler` class.
- Each exception returns a specific HTTP status and message to the client, improving error transparency and debugging.

**Example:**

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody
ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
        error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    return error;
}
````
This strategy enhances the application's maintainability and user experience by providing clear and standardized error responses.

[For more information, visit Spring Documentation.](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-exceptionhandler.html)

---
### 5.  **Security and Role Management**
The application includes a robust security layer, managed through Spring Security, to handle authentication and authorization.

**Implementation:**

-JWT Authentication: The application uses JSON Web Tokens (JWT) to manage user sessions securely. Each token encodes user details and roles, eliminating the need for server-side session storage.
-Role-Based Access Control: Users are assigned roles such as FREE, PREMIUM, or ADMIN, and these roles are checked against endpoint access policies.

This approach ensures sensitive operations are accessible only to authorized users, maintaining both security and compliance.

[For more details about JWT security, visit Spring Documentation.](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)


---
### **Conclusion**
This project demonstrates the use of modern Java and Spring Boot practices to create a flexible, modular, and secure sticker collection application. By overcoming database limitations, implementing custom validations, leveraging dynamic DTOs, and incorporating robust security with JWT and role management, the application ensures both compliance with regulations and an optimal user experience.
