# JWT Secret Key Configuration Issue

## Issue Description
When attempting to log in, the application threw the following error:
```
io.jsonwebtoken.io.DecodingException: Illegal base64 character: '#'
```

## Root Cause
The error occurred because the JWT secret key in `application.properties` was not properly Base64-encoded. The key contained special characters that are not valid in Base64 encoding.

## Error Details
- **Error Type**: `DecodingException`
- **Location**: `JwtUtils.key()` method
- **Affected Component**: JWT Token Generation
- **Impact**: Users cannot authenticate

## Solution
1. Generate a proper Base64-encoded secret key using one of these methods:
   - **Using OpenSSL** (recommended):
     ```bash
     openssl rand -base64 32
     ```
   - **Using PowerShell**:
     ```powershell
     [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
     ```

2. Update the `application.properties` file with the new key:
   ```properties
   # JWT Configuration
   jwt.secretKey=your_generated_base64_encoded_key_here
   jwt.expiration.inMs=token_expiration_time_in_milliseconds
   ```

## Example of a Valid Base64-Encoded Key
```
bXlfbmV3X3NlY3JldF9rZXlfMTIzNDU2Nzg5MEFCQ0RFRkdISUpLTE1OT1BRUlNUVVZX
```

## Prevention
1. Always use properly Base64-encoded strings for JWT secret keys
2. The key should only contain:
   - Uppercase letters (A-Z)
   - Lowercase letters (a-z)
   - Numbers (0-9)
   - Plus sign (+)
   - Forward slash (/)
   - Equal sign (=) for padding at the end

## Additional Notes
- The key length should be at least 32 bytes (256 bits) for security
- Never commit actual secret keys to version control
- Consider using environment variables or a secure secret management system for production

## Related Files
- `src/main/java/com/project/clixify_backend_sb/security/jwt/JwtUtils.java`
- `src/main/resources/application.properties`

## Date Resolved
September 28, 2025()
