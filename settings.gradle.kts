pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    // ------------------------------------------------------------------
    // CORREÇÃO: DECLARAÇÃO DA VERSÃO DO PLUGIN KSP PARA O ROOM FUNCIONAR
    // ------------------------------------------------------------------
    plugins {
        // Declara a versão do KSP (Kotlin Symbol Processing) para que o módulo :app a encontre


        // Você pode precisar adicionar outras declarações de plugins que usa com 'alias()' aqui também,
        // dependendo da sua configuração. Mantive apenas o KSP por enquanto.
        id("com.android.application") version "8.1.0" apply false
        id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ACADEMIA"
include(":app")