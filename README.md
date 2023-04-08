<div align="center">
<img width="40%" height = "40%" src="https://user-images.githubusercontent.com/58837451/230625789-8d64b4a0-2d54-446c-81b5-6facd383c5db.png" />
</div>

# Firetor
Firetor is Ktor plugin to help you simplified connecting to firebase admin

# Dependency

Firetor was distributed through jitpack and this sample is using gradle.kts file. 

Configured this code in `build.gradle.kts`
```groovy
repositories {
    ...
    maven("https://jitpack.io")
}
```

```groovy
dependencies {
    ...
    implementation("com.github.KylixEza:firetor:$firetor_version")
}
```

`firetor_version` depends on released tag, you can check released tag in jitpack banner above.


# Setup

This is some steps to configure Firetor:

- Go to your [firebase console](https://console.firebase.google.com)
- Create new project
- Go to project dashboard and choose `Project Settings`
- Choose `Service accounts` tab and click button `Generate new private key`
- Save admin key into proper folder
- Moved that admin key to `resource` folder in your Ktor Project
- Create new Kotlin file in `plugins` package named `Firetor`

`Firetor.kt`
```kotlin
fun Application.configureFiretor() {
    install(Firetor) {
        setAdminKey("your-admin-key-file-name.json")
        setBasicConfiguration {
            //Setup other Firebase Options configuration manually
        }
    }
}
```

| Function      | Parameters    | Parameter Type | Example | Mandatory |
| ------------- |:-------------:| :------------: | :-----: | :-------: |
| `setAdminKey()` | fileName | String | "firetor-sample-admin-key.json" | Yes |
| `enableFirebaseStorage()` | bucketUrl | String | "firetor-sample.appspot.com" | No |
| `setBasicConfiguration()` | config | FirbaseOptions.Builder.() -> FirebaseOptions.Builder? | setDatabaseUrl("") | No |

`Application.kt`
```kotlin
fun main() {
    ...
}

fun Application.module() {
    ...
    configureFiretor()
}
```


## Features

- [Firebase Storage](example.com)
- Firebase Auth
- Firebase Realtime DB
