package com.alces.deportes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Upload upload = new Upload();
    private Backup backup = new Backup();

    @Data
    public static class Upload {
        private String dir = "uploads/documentos";
    }

    @Data
    public static class Backup {
        private String dir = "backups";
    }
}
