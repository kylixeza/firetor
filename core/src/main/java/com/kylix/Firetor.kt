package com.kylix

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import io.ktor.util.*

class Firetor(configuration: Configuration) {

    private val adminKeyFileName: String? = configuration.adminKeyFileName
    private val storageBucket: String? = configuration.storageBucket
    private val basicConfiguration: FirebaseOptions.Builder.() -> FirebaseOptions.Builder? = configuration.basicConfiguration

    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline, Configuration, Firetor> {
        override val key: AttributeKey<Firetor>
            get() = AttributeKey("FiretorPluginKey")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): Firetor = run {
            val configuration = Configuration.apply(configure)
            Firetor(configuration).apply {
                pipeline.intercept(ApplicationCallPipeline.Plugins) {
                    val serviceAccount = getServiceAccount(adminKeyFileName)

                    val baseOptions = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))

                    if (checkIsFirebaseStorageEnabled(storageBucket)) {
                        baseOptions.setStorageBucket(getStorageBucket(storageBucket))
                    }

                    baseOptions.basicConfiguration()

                    if (FirebaseApp.getApps().isEmpty()) {
                        FirebaseApp.initializeApp(baseOptions.build())
                    }
                }
            }
        }

        private fun getServiceAccount(adminKeyFileName: String?) = run {
            checkNotNull(adminKeyFileName, "Admin key file name must not be null")
            this::class.java.classLoader.getResourceAsStream(adminKeyFileName)
        }

        private fun getStorageBucket(storageBucket: String?) = run {
            checkIsBucketUrlRight(storageBucket)
            storageBucket
        }
    }

}