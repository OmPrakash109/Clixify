If the URL is authenticated, and we don't pass in the token then URL shortening will not work.

While testing in Postman, on POST : http://localhost:8080/api/urls/shorten

we passed:

{
"originalUrl":"https://example.com"
}

in Body->raw->JSON format

And we got the response:

{
"timestamp": "2025-10-01T15:54:47.883+00:00",
"status": 403,
"error": "Forbidden",
"path": "/api/urls/shorten"
}

This is because we are not passing the token in the request header.

To fix this, we need to pass the token in the request header,
and to get this token we can go to API POST : http://localhost:8080/api/auth/public/login
We make a POST request to this API with Body->raw->JSON format as:

{
"username": "user5",
"password": "pass741"
}

and get the response containing token as:

{
"token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyNSIsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzU5MzM0NjA2LCJleHAiOjE3NTk1MDc0MDZ9.FtzyuuiBXs6Z23p0n8RBRcekNQFOT7s9x44ZwwqTFBP59lBa4tQ-FKF4rb3u-swJ"
}

And then we can copy this token and move back to POST : http://localhost:8080/api/urls/shorten
and there in Header, we assign -> Key: Authorization and Value: Bearer <token>

After doing this make a call again to POST : http://localhost:8080/api/urls/shorten

And we should get the response as:

{
"id": 1,
"originalUrl": "https://example.com",
"shortUrl": "EACcqmJW",
"clickCount": 0,
"createdDate": "2025-10-01T21:39:09.7187169",
"username": "user5"
}
