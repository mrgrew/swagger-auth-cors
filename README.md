# swagger-auth-cors

A project to demonstrate authentication error showing as CORS issue

## Prerequisite

Java 17 is required to run the Gradle build.

## Usage

1. Clone this repo: `git clone https://github.com/mrgrew/swagger-auth-cors`
2. Change to the repo directory and run it: `./gradlew bootRun`
3. In a browser, navigate to `http://localhost:9090/actuator/swagger-ui`
4. Authorize with `Bearer api-key`
5. Open the "/api" endpoint and click "Execute".
6. You'll see a 200 status code with response `{}`
7. Use the Authorize button but "Logout" this time
8. Click "Execute" again and see an "Undocumented" code with details "Failed to fetch."
9. Expected output would be a "401" code with details "Unuauthorized"
