# HTTP Status Codes - Complete Guide

## Overview
HTTP status codes are three-digit numbers returned by a server in response to a client's request. They indicate whether the request was successful or if an error occurred.

## Status Code Categories

### 1xx - Informational Responses
These codes indicate that the request was received and is being processed.

---

### 2xx - Success
These codes indicate that the request was successfully received, understood, and accepted.

#### **200 OK**
- **Meaning**: The request was successful, and the server returned the requested data.
- **When it occurs**:
  - GET request successfully retrieves data
  - POST request successfully creates a resource
  - PUT/PATCH request successfully updates a resource
  - DELETE request successfully deletes a resource
- **Example in Clixify**:
  ```java
  @GetMapping("/api/urls")
  public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal) {
      List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(principal.getName());
      return ResponseEntity.ok(urls); // Returns 200 OK
  }
  ```
- **How to handle**: No fix needed - this is the desired response.

---

#### **201 Created**
- **Meaning**: The request was successful, and a new resource was created.
- **When it occurs**:
  - POST request successfully creates a new resource
  - Typically includes a `Location` header pointing to the new resource
- **Example in Clixify**:
  ```java
  @PostMapping("/api/urls/shorten")
  public ResponseEntity<UrlMappingDTO> shortenUrl(@RequestBody Map<String, String> request, Principal principal) {
      UrlMappingDTO urlMapping = urlMappingService.shortenUrl(request.get("originalUrl"), principal.getName());
      return ResponseEntity.status(201).body(urlMapping); // Returns 201 Created
  }
  ```
- **How to handle**: No fix needed - indicates successful resource creation.

---

#### **204 No Content**
- **Meaning**: The request was successful, but there's no content to return.
- **When it occurs**:
  - DELETE request successfully deletes a resource
  - PUT/PATCH request updates a resource without returning data
- **Example**:
  ```java
  @DeleteMapping("/api/urls/{id}")
  public ResponseEntity<Void> deleteUrl(@PathVariable Long id) {
      urlMappingService.deleteUrl(id);
      return ResponseEntity.noContent().build(); // Returns 204 No Content
  }
  ```
- **How to handle**: No fix needed - indicates successful operation with no response body.

---

### 3xx - Redirection
These codes indicate that further action needs to be taken to complete the request.

#### **301 Moved Permanently**
- **Meaning**: The requested resource has been permanently moved to a new URL.
- **When it occurs**:
  - Resource has been permanently relocated
  - Old URLs need to redirect to new ones
- **Example**:
  ```java
  @GetMapping("/old-endpoint")
  public ResponseEntity<Void> oldEndpoint() {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Location", "/new-endpoint");
      return ResponseEntity.status(301).headers(headers).build();
  }
  ```
- **How to handle**: Update bookmarks/links to use the new URL.

---

#### **302 Found (Temporary Redirect)**
- **Meaning**: The requested resource temporarily resides at a different URL.
- **When it occurs**:
  - Temporary redirects (like URL shortener redirects)
  - Session-based redirects
- **Example in Clixify**:
  ```java
  @GetMapping("/{shortUrl}")
  public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
      UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
      if(urlMapping != null) {
          HttpHeaders headers = new HttpHeaders();
          headers.add("Location", urlMapping.getOriginalUrl());
          return ResponseEntity.status(302).headers(headers).build(); // Returns 302 Found
      }
      return ResponseEntity.notFound().build();
  }
  ```
- **How to handle**: No fix needed - browser automatically follows the redirect.

---

#### **304 Not Modified**
- **Meaning**: The resource has not been modified since the last request.
- **When it occurs**:
  - Client sends conditional request with `If-Modified-Since` header
  - Resource hasn't changed since specified date
- **How to handle**: Use cached version of the resource.

---

### 4xx - Client Errors
These codes indicate that the request contains bad syntax or cannot be fulfilled.

#### **400 Bad Request**
- **Meaning**: The server cannot process the request due to client error (malformed syntax, invalid data).
- **When it occurs**:
  - Missing required fields in request body
  - Invalid JSON format
  - Invalid data types
  - Validation failures
- **Example in Clixify**:
  ```java
  @PostMapping("/api/urls/shorten")
  public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> request) {
      if(request.get("originalUrl") == null || request.get("originalUrl").isEmpty()) {
          return ResponseEntity.badRequest().body("Original URL is required"); // Returns 400
      }
      // Process request...
  }
  ```
- **Common causes**:
  - Empty or null required fields
  - Invalid URL format
  - Malformed JSON
  - Type mismatch (sending string instead of number)
- **How to fix**:
  - Validate request data on client side
  - Check API documentation for required fields
  - Ensure correct data types
  - Use proper JSON formatting
  - Add validation annotations:
    ```java
    public class UrlShortenRequest {
        @NotBlank(message = "URL cannot be blank")
        @URL(message = "Invalid URL format")
        private String originalUrl;
    }
    ```

---

#### **401 Unauthorized**
- **Meaning**: Authentication is required and has failed or not been provided.
- **When it occurs**:
  - Missing authentication token
  - Invalid or expired JWT token
  - Incorrect credentials
- **Example in Clixify**:
  ```java
  // In JwtAuthenticationFilter
  if(token == null || !jwtUtils.validateToken(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Returns 401
      return;
  }
  ```
- **Common causes**:
  - No `Authorization` header in request
  - Expired JWT token
  - Invalid token format
  - Token signature verification failed
- **How to fix**:
  - Include valid JWT token in `Authorization: Bearer <token>` header
  - Re-authenticate if token expired
  - Check token expiration time
  - Verify token is properly formatted
  - Example fix:
    ```javascript
    // Frontend fix
    axios.get('/api/urls', {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    });
    ```

---

#### **403 Forbidden**
- **Meaning**: The server understood the request but refuses to authorize it.
- **When it occurs**:
  - User is authenticated but lacks permissions
  - Trying to access another user's resources
  - Role-based access control denial
- **Example in Clixify**:
  ```java
  @GetMapping("/api/analytics/{shortUrl}")
  @PreAuthorize("hasRole('ADMIN') or @urlSecurityService.isOwner(#shortUrl, principal)")
  public ResponseEntity<?> getAnalytics(@PathVariable String shortUrl, Principal principal) {
      // If user is not owner or admin, returns 403
  }
  ```
- **Common causes**:
  - Insufficient privileges
  - Accessing resources owned by another user
  - Missing required role (USER, ADMIN)
- **How to fix**:
  - Verify user has correct role
  - Check ownership of resources
  - Request elevated permissions if needed
  - Implement proper authorization checks:
    ```java
    if(!urlMapping.getUser().getUsername().equals(principal.getName())) {
        return ResponseEntity.status(403).body("Access denied");
    }
    ```

---

#### **404 Not Found**
- **Meaning**: The requested resource could not be found on the server.
- **When it occurs**:
  - URL/endpoint doesn't exist
  - Resource with given ID doesn't exist in database
  - Incorrect URL path
- **Example in Clixify**:
  ```java
  @GetMapping("/{shortUrl}")
  public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
      UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
      if(urlMapping == null) {
          return ResponseEntity.notFound().build(); // Returns 404
      }
      // Redirect logic...
  }
  ```
- **Common causes**:
  - Typo in URL
  - Resource deleted
  - Wrong endpoint path
  - Database record doesn't exist
- **How to fix**:
  - Verify URL is correct
  - Check if resource exists in database
  - Use correct endpoint path
  - Handle null cases properly:
    ```java
    public UrlMapping getUrlMapping(Long id) {
        return urlMappingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("URL mapping not found"));
    }
    ```

---

#### **405 Method Not Allowed**
- **Meaning**: The HTTP method used is not supported for the requested resource.
- **When it occurs**:
  - Using POST when only GET is allowed
  - Using GET when POST is required
  - Method not defined for endpoint
- **Example**:
  ```java
  // Only GET is defined
  @GetMapping("/api/urls")
  public ResponseEntity<?> getUrls() { }
  
  // Trying to POST to this endpoint will return 405
  ```
- **Common causes**:
  - Wrong HTTP method in request
  - Endpoint doesn't support the method
- **How to fix**:
  - Check API documentation for correct HTTP method
  - Use appropriate method (GET, POST, PUT, DELETE)
  - Frontend fix:
    ```javascript
    // Wrong
    axios.post('/api/urls'); // If endpoint only accepts GET
    
    // Correct
    axios.get('/api/urls');
    ```

---

#### **409 Conflict**
- **Meaning**: The request conflicts with the current state of the server.
- **When it occurs**:
  - Duplicate resource creation
  - Concurrent modification conflicts
  - Business logic violations
- **Example in Clixify**:
  ```java
  @PostMapping("/api/auth/public/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
      if(userRepository.findByUsername(request.getUsername()).isPresent()) {
          return ResponseEntity.status(409).body("Username already exists"); // Returns 409
      }
      // Register user...
  }
  ```
- **Common causes**:
  - Duplicate username/email
  - Unique constraint violations
  - Version conflicts in optimistic locking
- **How to fix**:
  - Check for existing resources before creation
  - Use unique identifiers
  - Implement proper validation:
    ```java
    @Column(unique = true)
    private String username;
    ```

---

#### **422 Unprocessable Entity**
- **Meaning**: The request was well-formed but contains semantic errors.
- **When it occurs**:
  - Validation errors
  - Business rule violations
  - Invalid data combinations
- **Example**:
  ```java
  @PostMapping("/api/urls/shorten")
  public ResponseEntity<?> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
      if(!isValidUrl(request.getOriginalUrl())) {
          return ResponseEntity.status(422).body("Invalid URL format");
      }
      // Process...
  }
  ```
- **How to fix**:
  - Validate input data
  - Follow business rules
  - Use Bean Validation annotations

---

#### **429 Too Many Requests**
- **Meaning**: The user has sent too many requests in a given time period (rate limiting).
- **When it occurs**:
  - Rate limit exceeded
  - Too many API calls
- **How to fix**:
  - Wait before making more requests
  - Implement exponential backoff
  - Check rate limit headers

---

### 5xx - Server Errors
These codes indicate that the server failed to fulfill a valid request.

#### **500 Internal Server Error**
- **Meaning**: The server encountered an unexpected condition that prevented it from fulfilling the request.
- **When it occurs**:
  - Unhandled exceptions
  - Database connection failures
  - Null pointer exceptions
  - Configuration errors
- **Example scenarios in Clixify**:
  ```java
  // Scenario 1: NullPointerException
  public UrlMapping getOriginalUrl(String shortUrl) {
      UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
      return urlMapping.getOriginalUrl(); // NPE if urlMapping is null - causes 500
  }
  
  // Scenario 2: Database connection error
  @GetMapping("/api/urls")
  public ResponseEntity<?> getUrls() {
      // If database is down, this throws exception - causes 500
      List<UrlMapping> urls = urlMappingRepository.findAll();
      return ResponseEntity.ok(urls);
  }
  ```
- **Common causes**:
  - NullPointerException
  - Database connection failures
  - Missing dependencies (forgot @AllArgsConstructor or @Autowired)
  - Incorrect configuration
  - Unhandled runtime exceptions
- **How to fix**:
  - Check server logs for stack trace
  - Add null checks:
    ```java
    if(urlMapping == null) {
        throw new ResourceNotFoundException("URL not found");
    }
    ```
  - Use try-catch blocks:
    ```java
    try {
        return urlMappingRepository.findAll();
    } catch(Exception e) {
        log.error("Database error", e);
        return ResponseEntity.status(500).body("Internal server error");
    }
    ```
  - Implement global exception handler:
    ```java
    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handleException(Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    ```
  - Verify all dependencies are injected properly
  - Check database connection settings
  - Review application.properties configuration

---

#### **501 Not Implemented**
- **Meaning**: The server does not support the functionality required to fulfill the request.
- **When it occurs**:
  - Feature not yet implemented
  - Unsupported HTTP method
- **How to fix**:
  - Implement the required functionality
  - Use supported features

---

#### **502 Bad Gateway**
- **Meaning**: The server, while acting as a gateway or proxy, received an invalid response from the upstream server.
- **When it occurs**:
  - Upstream server is down
  - Network issues between servers
  - Reverse proxy configuration issues
- **How to fix**:
  - Check upstream server status
  - Verify network connectivity
  - Review proxy configuration

---

#### **503 Service Unavailable**
- **Meaning**: The server is temporarily unable to handle the request.
- **When it occurs**:
  - Server maintenance
  - Server overloaded
  - Database temporarily unavailable
- **Example**:
  ```java
  @GetMapping("/api/urls")
  public ResponseEntity<?> getUrls() {
      if(!databaseService.isAvailable()) {
          return ResponseEntity.status(503).body("Service temporarily unavailable");
      }
      // Process request...
  }
  ```
- **How to fix**:
  - Wait and retry
  - Check server status
  - Implement retry logic with exponential backoff

---

#### **504 Gateway Timeout**
- **Meaning**: The server, while acting as a gateway, did not receive a timely response from the upstream server.
- **When it occurs**:
  - Upstream server too slow
  - Network timeout
  - Long-running queries
- **How to fix**:
  - Optimize slow queries
  - Increase timeout settings
  - Implement caching

---

## Best Practices for Handling Status Codes

### 1. **Use Appropriate Status Codes**
```java
// Good
@PostMapping("/api/urls/shorten")
public ResponseEntity<UrlMappingDTO> shortenUrl(@RequestBody UrlShortenRequest request) {
    UrlMappingDTO result = urlMappingService.shortenUrl(request);
    return ResponseEntity.status(201).body(result); // 201 for creation
}

// Bad
@PostMapping("/api/urls/shorten")
public ResponseEntity<UrlMappingDTO> shortenUrl(@RequestBody UrlShortenRequest request) {
    UrlMappingDTO result = urlMappingService.shortenUrl(request);
    return ResponseEntity.ok(result); // Should be 201, not 200
}
```

### 2. **Provide Meaningful Error Messages**
```java
// Good
if(urlMapping == null) {
    return ResponseEntity.status(404)
        .body(Map.of("error", "URL not found", "shortUrl", shortUrl));
}

// Bad
if(urlMapping == null) {
    return ResponseEntity.status(404).build(); // No error message
}
```

### 3. **Implement Global Exception Handling**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
    }
}
```

### 4. **Log Errors Appropriately**
```java
@GetMapping("/api/urls/{id}")
public ResponseEntity<?> getUrl(@PathVariable Long id) {
    try {
        UrlMapping url = urlMappingService.getUrl(id);
        return ResponseEntity.ok(url);
    } catch(ResourceNotFoundException e) {
        log.warn("URL not found: {}", id); // Warn for client errors
        return ResponseEntity.notFound().build();
    } catch(Exception e) {
        log.error("Error retrieving URL: {}", id, e); // Error for server issues
        return ResponseEntity.status(500).build();
    }
}
```

### 5. **Validate Input Early**
```java
@PostMapping("/api/urls/shorten")
public ResponseEntity<?> shortenUrl(@Valid @RequestBody UrlShortenRequest request) {
    // @Valid triggers validation before method execution
    // Returns 400 automatically if validation fails
    return ResponseEntity.status(201).body(urlMappingService.shortenUrl(request));
}
```

---

## Quick Reference Table

| Code | Name | Category | Common Cause | Fix |
|------|------|----------|--------------|-----|
| 200 | OK | Success | Successful request | None needed |
| 201 | Created | Success | Resource created | None needed |
| 204 | No Content | Success | Successful deletion | None needed |
| 301 | Moved Permanently | Redirect | Permanent URL change | Update bookmarks |
| 302 | Found | Redirect | Temporary redirect | None needed |
| 400 | Bad Request | Client Error | Invalid input | Validate input |
| 401 | Unauthorized | Client Error | Missing/invalid auth | Add valid token |
| 403 | Forbidden | Client Error | Insufficient permissions | Check roles |
| 404 | Not Found | Client Error | Resource doesn't exist | Verify URL/ID |
| 405 | Method Not Allowed | Client Error | Wrong HTTP method | Use correct method |
| 409 | Conflict | Client Error | Duplicate resource | Check uniqueness |
| 422 | Unprocessable Entity | Client Error | Validation error | Fix input data |
| 429 | Too Many Requests | Client Error | Rate limit exceeded | Wait and retry |
| 500 | Internal Server Error | Server Error | Unhandled exception | Check logs, fix code |
| 502 | Bad Gateway | Server Error | Upstream server issue | Check upstream |
| 503 | Service Unavailable | Server Error | Server overloaded | Wait and retry |
| 504 | Gateway Timeout | Server Error | Timeout | Optimize queries |

---

## Testing Status Codes

### Using Postman
```
1. Send request to endpoint
2. Check "Status" in response (e.g., "200 OK")
3. Verify response body matches expected format
```

### Using cURL
```bash
# Check status code
curl -I http://localhost:8080/api/urls

# Verbose output with status code
curl -v http://localhost:8080/api/urls

# Follow redirects
curl -L http://localhost:8080/abc123
```

### Using Spring Boot Tests
```java
@Test
public void testGetUrl_NotFound() {
    mockMvc.perform(get("/api/urls/999"))
        .andExpect(status().isNotFound()); // Expects 404
}

@Test
public void testCreateUrl_Success() {
    mockMvc.perform(post("/api/urls/shorten")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"originalUrl\":\"https://example.com\"}"))
        .andExpect(status().isCreated()); // Expects 201
}
```

---

## Debugging Tips

1. **Check Server Logs**: Always check application logs for stack traces
2. **Use Browser DevTools**: Network tab shows status codes and request/response details
3. **Enable Debug Logging**: Set `logging.level.root=DEBUG` in application.properties
4. **Test with Postman**: Isolate issues by testing endpoints directly
5. **Review Exception Messages**: Error messages often indicate the root cause
6. **Check Database Connectivity**: Many 500 errors are database-related
7. **Verify Configuration**: Ensure application.properties is correct
8. **Check Dependencies**: Missing @Autowired or @AllArgsConstructor causes NPE

---

## Additional Resources

- [MDN HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [RFC 7231 - HTTP/1.1 Semantics](https://tools.ietf.org/html/rfc7231)
- [Spring Boot Error Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)
