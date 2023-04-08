<div align="center">
<img width="40%" height = "40%" src="https://user-images.githubusercontent.com/58837451/230625789-8d64b4a0-2d54-446c-81b5-6facd383c5db.png" />
</div>

# Firetor
Firetor is Ktor plugin to help you simplified connecting to firebase admin

## Dependency

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


## Setup

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

- ### [Firebase Storage](example.com)

- ### Firebase Auth (TBA)

- ### Firebase Realtime DB (TBA)

## Contributing

Please fork this repository and contribute back using pull request.

Any contributions, large or small, major features, bug fixes, are welcomed and appreciated
but will be thoroughly reviewed .

## Contact
<a href="https://linkedin.com/in/kylix-eza-saputra-1bb1b7192" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/linked-in-alt.svg" alt="kylix-eza-saputra-1bb1b7192" height="30" width="40" /></a>
<a href="https://instagram.com/k_ylix" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/instagram.svg" alt="k_ylix" height="30" width="40" /></a>
<a href="https://discord.gg/#Kylix3272" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/discord.svg" alt="#Kylix3272" height="30" width="40" /></a>
</p>

<p>
Don't forget to ⭐ this repository to motivates me to share more open source library
</p>
