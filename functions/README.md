# Cloud Function for FCM with FirebaseAuthentication

NOTE: You don't need to deploy functions if you have any security concerns because Cloud Function has a local mode.

## Installation

```
npm install -g firebase-tools

cd repo_root

firebase login
firebase use "${your project id}"

cd functions && npm install
```

## Where you might need to change

- `src/messaging/payload` - Add payload factories to here.
- `src/messaging/route.ts` - Add endpoints to here.

*If you want to change an authenticate method.*

- `src/auth/provider` - Implement an authenticate provider you want.
- `src/auth/authorize.ts` - Change an import statement of an authenticate provider.

## How to set up

### Create a new Firebase project which you will deploy Cloud Functions.

This project is used for Cloud Functions (and Firebase Authentication if you want).
FYI, you need to enable Google sign-in provider in Firebase Authentication section if you want to use sample android applications.

### Download a service account credential of a project which is for FCM

Download a secret key file and save it as `functions/credential.json`.
This credential will be used for the authentication of Firebase Cloud Messaging.

### Test functions

There are several ways to test a function though, serving a function locally is better.   
Because errors would be visible. For example, you can see a connection error in your shell if a credential of FCM is wrong.

```
firebase [serve|shell] --only functions:api
```

This command will launch functions on your local, so you can send push notifications if you have a device token.  
FYI, it's better to disable an authentication feature by changing `App.app(true)` to `App.app(false)` in `src/index.ts`.

### Deployment

```
# I faced an issue that only first deployment failed
# If you faced the same issue, please run the command below again.
firebase deploy --only "functions:api"
```