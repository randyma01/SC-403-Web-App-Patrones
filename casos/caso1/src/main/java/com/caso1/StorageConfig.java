package com.caso1;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class StorageConfig {

    @Value("${firebase.json.path:firebase}")
    private String jsonPath;

    @Value("${firebase.json.file:techshop-4f012-firebase-adminsdk-fbsvc-84ad9b5cd0.json}")
    private String jsonFile;

    @Bean
    public Storage storage() throws IOException {
        File secretFile = new File("/etc/secrets" + File.separator + jsonFile);
        InputStream inputStream = secretFile.exists()
                ? new FileInputStream(secretFile)
                : new ClassPathResource(jsonPath + File.separator + jsonFile).getInputStream();

        try (inputStream) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }
}
