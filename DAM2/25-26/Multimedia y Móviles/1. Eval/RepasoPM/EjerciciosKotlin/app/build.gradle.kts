plugins {
    kotlin("jvm") // SIN version
    application
}

group = "com.danielalonso"
version = "1.0-SNAPSHOT"


dependencies {
    implementation(kotlin("stdlib"))
    // Puedes agregar más dependencias JVM si lo necesitas
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}
