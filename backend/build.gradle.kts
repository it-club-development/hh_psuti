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
	implementation("org.springframework.boot:spring-boot-starter-web:3.4.0")		//Создаёт веб-приложение с REST API
	implementation("org.springframework.boot:spring-boot-starter-security:3.4.0")	// Добавляет безопасность и шифрование паролей
	implementation("io.jsonwebtoken:jjwt:0.12.6")							// Создаёт и проверяет JWT токены
	//implementation("javax.xml.bind:jaxb-api:4.0.1")							// Преобразует Java объекты в XML и обратно
	//обновление 30.07.26
	implementation("javax.servlet:javax.servlet-api:4.0.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql")
	//обновление 05.08.26
	implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	// обновление 13.08.2026

	compileOnly("org.projectlombok:lombok:1.18.36")		//lombok
	//annotationProcessor("org.projectlombok:lombok:1.18.36")
	//testCompileOnly("org.projectlombok:lombok:1.18.36")		//для тестов
	//testAnnotationProcessor("org.projectlombok:lombok:1.18.36")		//для тестов


	//
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
