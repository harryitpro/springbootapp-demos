Step 8: Running and Testing
Run the Application:
Use your IDE or run mvn spring-boot:run from the project root.
The server starts on http://localhost:8080.
Test the Authorization Flow:
Get Authorization Code:
Open a browser and navigate to:

http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&redirect_uri=http://localhost:8080/login/oauth2/code/client&scope=read%20write
Log in with user and password.
Approve the scopes to receive an authorization code in the redirect URI.
Exchange Code for Token:
Use a tool like curl or Postman to exchange the code for an access token:
curl -X POST \
http://localhost:8080/oauth2/token \
-H "Authorization: Basic Y2xpZW50OnNlY3JldA==" \
-d "grant_type=authorization_code" \
-d "code=<your_authorization_code>" \
-d "redirect_uri=http://localhost:8080/login/oauth2/code/client"
(Note: Y2xpZW50OnNlY3JldA== is the Base64 encoding of client:secret.)
This returns a JSON response with an access token.
Access Protected Resource:
Use the access token to access the /api/resource endpoint:
curl -H "Authorization: Bearer <access_token>" http://localhost:8080/api/resource
Expected response: This is a protected resource!