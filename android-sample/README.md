# Sample Android apps

- Push Trigger App - `push-dev-tool` / `io.github.jmatsu.cf_dev_tool`
- Push Receiver App - `pushee` / `io.github.jmatsu.cf_pushee`

Create a debug keystore in `android-sample` directory.

```
cd android-sample

keytool -genkey -v -keystore debug.keystore -alias androiddebugkey -keyalg RSA -validity 10000 -dname "CN=Android Debug,O=Android,C=US"

# copy sha1
keytool -exportcert -list -v -alias androiddebugkey -keystore debug.keystore
```

## How to try

You need to 

- Finish the deployment of Cloud Function and Firebase Authentication by following [README.md](../functions/README.md) of `functions`.
- Create a debug keystore with following the commands above
- Register sample apps and download *google-service.json* for each modules.
- Set `cloudFunctionEndpoint` which must end with `/` in *key.gradle.sample*.
- Run `./gradlew installDebug`

`NOTE that you must open a push receiver app at least once with an active network connection to get a notification token.`