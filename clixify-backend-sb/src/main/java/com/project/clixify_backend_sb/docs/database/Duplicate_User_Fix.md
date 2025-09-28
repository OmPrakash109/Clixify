# Duplicate Users Issue Resolution

## Issue Description
When attempting to log in with a username that existed multiple times in the database, the authentication failed with the error:
```
org.hibernate.NonUniqueResultException: Query did not return a unique result: 7 results were returned
```

## Root Cause
The `users` table contained multiple entries with the same `username` value, which violates the assumption that usernames should be unique. The JPA repository's `findByUsername()` method expects at most one result, but found multiple entries.

## Steps to Reproduce
1. Attempt to log in with a username that exists multiple times in the database
2. Observe the authentication failure in the logs

## Resolution

### 1. Identify Duplicate Usernames
```sql
-- Find usernames that have duplicates
SELECT username, COUNT(*) as count
FROM users
GROUP BY username
HAVING COUNT(*) > 1;
```

### 2. Examine the Duplicate Entries
```sql
-- View all users with the duplicate username
SELECT * FROM users WHERE username = 'user1' ORDER BY user_id;
```

### 3. Remove Duplicate Entries
```sql
-- Keep only the most recent entry (highest user_id)
-- Replace '8' with the user_id you want to keep
DELETE FROM users WHERE username = 'user1' AND user_id != 8;
```

### 4. Verify the Fix
```sql
-- Confirm only one user with the username exists
SELECT * FROM users WHERE username = 'user1';
```

## Prevention
To prevent this issue from recurring, a unique constraint was added to the `username` column in the `User` entity:

```java
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username")
})
public class User {
    // ...
    @Column(unique = true)
    private String username;
    // ...
}
```

## Additional Notes
- Always validate for existing usernames before creating new users
- Consider adding database-level constraints to enforce data integrity
- Regular database maintenance can help identify and fix data inconsistencies

## Date Resolved
September 28, 2025