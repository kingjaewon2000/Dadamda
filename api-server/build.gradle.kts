plugins {
    kotlin("plugin.jpa")
}

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    runtimeOnly("com.mysql:mysql-connector-j")
}
