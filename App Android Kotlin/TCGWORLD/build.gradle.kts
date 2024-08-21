// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    alias(libs.plugins.firebase) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false

    alias(libs.plugins.dokka)
    alias(libs.plugins.detekt)
}

val rootLibs = rootProject.libs

subprojects {
    apply {
        plugin(rootLibs.plugins.detekt.get().pluginId)
        plugin(rootLibs.plugins.dokka.get().pluginId)
    }

    detekt {
        toolVersion = rootLibs.versions.detekt.get()
        config.from(rootProject.file("config/detekt/detekt.yml"))
        autoCorrect = true
        buildUponDefaultConfig = true
    }

    dependencies {
        detektPlugins(rootLibs.detekt.formatting)
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(project.rootDir.resolve("docs"))
}
