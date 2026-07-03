// ==========================================
// ARCHIVO: FlywayConfig.java
// QUÉ HACE: Config Flyway; al arrancar repara historial y aplica migraciones SQL en MySQL.
// FLUJO: Spring Boot inicia → Flyway lee db/migration → crea/actualiza tablas antes que JPA.
// ==========================================
package com.alces.deportes.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.flyway.repair-on-migrate", havingValue = "true")
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}
