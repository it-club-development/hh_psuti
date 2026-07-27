plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(26)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	//обновление 26.07.26
	implementation("org.springframework.boot:spring-boot-starter-web")		//Создаёт веб-приложение с REST API
	implementation("org.springframework.boot:spring-boot-starter-security")	// Добавляет безопасность и шифрование паролей
	implementation("io.jsonwebtoken:jjwt:0.9.1")							// Создаёт и проверяет JWT токены
	implementation("javax.xml.bind:jaxb-api:2.3.1")							// Преобразует Java объекты в XML и обратно
	//
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
