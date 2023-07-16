# Stateless Magic Link Authentication in Ktor

This is a sample project to demonstrate how to implement stateless magic link authentication in Ktor.

## Why Magic Link? Why Statelessness?

Magic link is a great way to implement passwordless authentication. It is as secure as the user's email.

Statelessness comes from the fact that the server doesn't need to save the token in a database (Which is essentially a password). The token is generated using a secret key and the user's email. The server can verify the token using the same secret key and the user's email. This is possible because the token is signed using the secret key. The server doesn't need to save the token in a database. This is the reason why this authentication is stateless.

## How does it work?
In this quick demo I used JWT to generate the token. The token is generated using the user's email and a secret key. The token is sent to the user's email. When the user clicks on the link, the server verifies the token using the secret key and the user's email. If the token is valid, the user is authenticated.

## Disclaimer

This demo stops at the point where the token is validated. I use a similar approach for an api there I generate JWT Tokens and use JWT authentication for my exposed endpoints.

It also doesn't actually send an email. It just prints the token in page. You can use a service like [Mailtrap](https://mailtrap.io/) to test the email sending functionality.

It also discards 
## How to run the demo?
1. Clone the repo
2. Run `./gradlew run`
3. Open `http://localhost:8080` in your browser
4. Enter your email and click on the button