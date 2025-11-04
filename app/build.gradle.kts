plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Adicionar o plugin KSP (Kotlin Symbol Processing) para o Room
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" // Versão 1.9.20-1.0.14 ou mais recente
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.academia"
    compileSdk = 34 // Manter uma versão mais comum (34)
    // No seu código original, você usou `release(36)` que depende de uma definição
    // em `libs.versions.toml`. Usei 34 como um fallback seguro.
    // Mantenha o original se tiver certeza da definição `release(36)`.

    defaultConfig {
        applicationId = "com.example.academia"
        minSdk = 24
        targetSdk = 34 // Use targetSdk 34 para consistência
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Mudar para JavaVersion.VERSION_1_8 se tiver problemas, mas 11 é o padrão moderno
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core Android/Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // Adicionado

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- DEPENDÊNCIAS DO PROJETO (Adicionadas) ---

    // Room (Banco de Dados) - Use a versão mais recente
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion") // Compiler usando KSP

    // Retrofit (API) - Use a versão mais recente
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.6") // Use a versão mais recente

    // --- DEPENDÊNCIAS DE TESTE ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}