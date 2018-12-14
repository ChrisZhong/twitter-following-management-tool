import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    eclipse
    idea
    java
    kotlin("jvm") version Version.KOTLIN_VERSION
    application
    jacoco
    id("com.github.kt3k.coveralls") version "2.4.0x"
    id("io.spring.dependency-management") version Version.SPRING_DEPENDENCY_MANAGEMENT_VERSION
    id("org.springframework.boot") version Version.SPRING_BOOT_VERSION
}

repositories { jcenter() }

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-starter-parent:${Version.SPRING_BOOT_VERSION}")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.social:spring-social-twitter:${Version.springSocialTwitterVersion}")
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "$group.Application"
}

jacoco { toolVersion = "0.7.5.201505241946" }

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    jar { baseName = "twitter-following-management-tool" }
    test {
        //		maxParallelForks = 2
//		forkEvery = 150
    }
    jacocoTestReport {
        dependsOn(test)
        reports {
            html.isEnabled = true
            xml.isEnabled = true
            csv.isEnabled = false
        }
    }
    wrapper {
        gradleVersion = "5.0"
        distributionType = Wrapper.DistributionType.ALL
    }
}

//task stage { dependsOn build }
