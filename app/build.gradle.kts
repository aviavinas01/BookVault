plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
}

dependencies {
    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    // ViewModel + Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Ktor (HTTP Client — your Axios)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Kotlinx Serialization (JSON parsing)
    implementation(libs.kotlinx.serialization.json)

    // Room (Local DB — your MongoDB)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // DataStore (Preferences)
    implementation(libs.datastore.preferences)

    // Koin (Dependency Injection)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}
