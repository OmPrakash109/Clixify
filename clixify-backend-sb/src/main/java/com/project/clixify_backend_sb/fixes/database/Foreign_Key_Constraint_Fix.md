# Foreign Key Constraint Issue - UrlMapping to User Relationship

## Issue Date
**2025-10-04**

## Problem Description

### Error Encountered
When attempting to create a short URL via POST request to `/api/urls/shorten`, the application threw a `500 Internal Server Error` with the following exception:

```
java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: 
a foreign key constraint fails (`clixify_db`.`url_mappings`, 
CONSTRAINT `FKatnqn60wab4vuafu7sliw97gu` 
FOREIGN KEY (`u_map_id`) REFERENCES `users` (`user_id`))
```

### Root Cause

The `url_mappings` table had **two foreign key constraints**, one of which was incorrectly configured:

1. **Incorrect Constraint**: `FKatnqn60wab4vuafu7sliw97gu`
   - Column: `u_map_id` (PRIMARY KEY of url_mappings)
   - References: `users.user_id`
   - **Problem**: Using the primary key column as a foreign key

2. **Correct Constraint**: `FKpvq6ci6x5jvjoea6ehOfp0o3`
   - Column: `user_id` (FOREIGN KEY in url_mappings)
   - References: `users.user_id`
   - **Status**: This was the correct relationship

### Why This Happened

This issue occurred because:
- Hibernate auto-generated the database schema at some point with incorrect entity mappings
- Multiple schema updates created duplicate/conflicting foreign key constraints
- The `@JoinColumn` annotation was pointing to the correct column (`user_id`), but the database had a conflicting constraint on `u_map_id`

---

## Diagnosis Steps

### Step 1: Review the Error Log
The error log clearly showed the constraint name and the incorrect column mapping:
```
CONSTRAINT `FKatnqn60wab4vuafu7sliw97gu` FOREIGN KEY (`u_map_id`) REFERENCES `users` (`user_id`)
```

### Step 2: Inspect Database Schema
Run the following SQL query to view all foreign key constraints on the `url_mappings` table:

```sql
SELECT 
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_SCHEMA = 'clixify_db' 
    AND TABLE_NAME = 'url_mappings'
    AND REFERENCED_TABLE_NAME IS NOT NULL;
```

**Result:**
```
CONSTRAINT_NAME                  | COLUMN_NAME | REFERENCED_TABLE_NAME | REFERENCED_COLUMN_NAME
---------------------------------|-------------|----------------------|----------------------
FKatnqn60wab4vuafu7sliw97gu      | u_map_id    | users                | user_id
FKpvq6ci6x5jvjoea6ehOfp0o3      | user_id     | users                | user_id
```

This confirmed that there were two foreign key constraints, and the first one was using the wrong column.

### Step 3: Review Entity Mapping
Checked the `UrlMapping` entity to verify the JPA mapping:

```java
@Entity
@Table(name = "UrlMappings")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uMapId;  // Primary key - should NOT be a foreign key
    
    // Other fields...
    
    @ManyToOne
    @JoinColumn(name = "user_id")  // Correct: points to user_id column
    private User user;
}
```

The entity mapping was correct, but the database had an extra incorrect constraint.

---

## Solution

### Step 1: Drop the Incorrect Foreign Key Constraint

Run the following SQL command to remove the incorrect constraint:

```sql
ALTER TABLE url_mappings 
DROP FOREIGN KEY FKatnqn60wab4vuafu7sliw97gu;
```

**Important Notes:**
- Only drop `FKatnqn60wab4vuafu7sliw97gu` (the incorrect one)
- Do NOT drop `FKpvq6ci6x5jvjoea6ehOfp0o3` (the correct one)

### Step 2: Verify the Fix

After dropping the constraint, verify that only the correct constraint remains:

```sql
SELECT 
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_SCHEMA = 'clixify_db' 
    AND TABLE_NAME = 'url_mappings'
    AND REFERENCED_TABLE_NAME IS NOT NULL;
```

**Expected Result:**
```
CONSTRAINT_NAME                  | COLUMN_NAME | REFERENCED_TABLE_NAME | REFERENCED_COLUMN_NAME
---------------------------------|-------------|----------------------|----------------------
FKpvq6ci6x5jvjoea6ehOfp0o3      | user_id     | users                | user_id
```

### Step 3: Restart the Application

1. Stop the Spring Boot application
2. Restart the application
3. The application should now start without issues

### Step 4: Test the Fix

Test the URL shortening endpoint:

**Request:**
```http
POST http://localhost:8080/api/urls/shorten
Content-Type: application/json
Authorization: Bearer <your-jwt-token>

{
    "originalUrl": "https://www.google.com"
}
```

**Expected Response:**
```json
{
    "uMapId": 18,
    "originalUrl": "https://www.google.com",
    "shortUrl": "abc123",
    "clickCount": 0,
    "createdDate": "2025-10-04T18:00:00"
}
```

**Status Code:** `201 Created` or `200 OK`

---

## Understanding the Correct Relationship

### Database Schema
```
users table:
- user_id (PRIMARY KEY)
- username
- email
- password
- role

url_mappings table:
- u_map_id (PRIMARY KEY)
- original_url
- short_url
- click_count
- created_date
- user_id (FOREIGN KEY → users.user_id)  ← This is the correct foreign key
```

### Entity Relationship
```
User (1) ←→ (Many) UrlMapping

One user can have many URL mappings.
Each URL mapping belongs to one user.
```

### JPA Mapping
```java
// In UrlMapping entity
@ManyToOne
@JoinColumn(name = "user_id")  // Foreign key column in url_mappings table
private User user;
```

---

## Prevention

To prevent this issue in the future:

### 1. Use Explicit Constraint Names
Instead of letting Hibernate auto-generate constraint names, define them explicitly:

```java
@ManyToOne
@JoinColumn(
    name = "user_id",
    foreignKey = @ForeignKey(name = "fk_url_mappings_user")
)
private User user;
```

### 2. Disable Auto Schema Generation in Production
In `application.properties`:

```properties
# Development
spring.jpa.hibernate.ddl-auto=validate

# Production
spring.jpa.hibernate.ddl-auto=none
```

### 3. Use Database Migration Tools
Use tools like **Flyway** or **Liquibase** for controlled schema changes:

```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

### 4. Review Schema Before Deployment
Always review the generated schema before deploying:

```sql
SHOW CREATE TABLE url_mappings;
```

### 5. Regular Database Audits
Periodically check for duplicate or incorrect constraints:

```sql
-- Check all foreign keys in the database
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_SCHEMA = 'clixify_db'
    AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY 
    TABLE_NAME, CONSTRAINT_NAME;
```

---

## Key Takeaways

1. **@JoinColumn does NOT auto-convert camelCase to snake_case**
   - Always use the exact database column name
   - Example: `@JoinColumn(name = "user_id")` not `@JoinColumn(name = "userId")`

2. **Foreign Key Constraints Must Reference the Correct Column**
   - Foreign key should point to the referenced table's primary key
   - In this case: `url_mappings.user_id` → `users.user_id`

3. **Primary Keys Should Not Be Foreign Keys**
   - `u_map_id` is the primary key of `url_mappings` and should not reference another table
   - Only dedicated foreign key columns should have foreign key constraints

4. **Multiple Constraints Can Cause Conflicts**
   - Always check for duplicate or conflicting constraints
   - Use the `INFORMATION_SCHEMA` to inspect database constraints

5. **Hibernate Schema Auto-Generation Can Create Issues**
   - Use `validate` or `none` in production
   - Prefer manual schema management or migration tools

---

## Related Files

- **Entity**: `com.project.clixify_backend_sb.model.UrlMapping`
- **Entity**: `com.project.clixify_backend_sb.model.User`
- **Service**: `com.project.clixify_backend_sb.service.UrlMappingService`
- **Controller**: `com.project.clixify_backend_sb.controller.UrlMappingController`

---

## Additional Resources

- [JPA @JoinColumn Documentation](https://docs.oracle.com/javaee/7/api/javax/persistence/JoinColumn.html)
- [MySQL Foreign Key Constraints](https://dev.mysql.com/doc/refman/8.0/en/create-table-foreign-keys.html)
- [Hibernate Schema Generation](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#schema-generation)
- [Spring Data JPA Best Practices](https://spring.io/guides/gs/accessing-data-jpa/)

---

## Status
✅ **RESOLVED** - Incorrect foreign key constraint dropped successfully. Application now works as expected.
