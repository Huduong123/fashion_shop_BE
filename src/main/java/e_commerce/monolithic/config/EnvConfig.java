package e_commerce.monolithic.config;

import java.io.File;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties properties = new Properties();

        Dotenv dotenv = null;
        
        // Thử tìm file .env ở nhiều vị trí khác nhau
        String[] possiblePaths = {
            "./",                    // Current working directory
            "./fashion_shop_BE/",    // If running from parent directory
            "../",                   // If running from target directory
            System.getProperty("user.dir"),  // User directory
            System.getProperty("user.dir") + "/fashion_shop_BE"  // Parent/fashion_shop_BE
        };
        
        for (String path : possiblePaths) {
            try {
                File envFile = new File(path + "/.env");
                if (envFile.exists()) {
                    System.out.println("Found .env file at: " + envFile.getAbsolutePath());
                    dotenv = Dotenv.configure()
                            .directory(path)
                            .ignoreIfMalformed()
                            .ignoreIfMissing()
                            .load();
                    break;
                }
            } catch (Exception e) {
                System.out.println("Could not load .env from " + path + ": " + e.getMessage());
            }
        }
        
        // Fallback: try loading without specifying directory
        if (dotenv == null) {
            try {
                dotenv = Dotenv.configure()
                        .ignoreIfMalformed()
                        .ignoreIfMissing()
                        .load();
                System.out.println("Loaded .env using default configuration");
            } catch (Exception e) {
                System.out.println("Could not load .env file: " + e.getMessage());
            }
        }

        if (dotenv != null) {
            dotenv.entries().forEach(entry -> {
                properties.setProperty(entry.getKey(), entry.getValue());
                System.out.println("Loaded environment variable: " + entry.getKey());
            });
        } else {
            System.out.println("No .env file found. Using system environment variables only.");
        }

        environment.getPropertySources().addFirst(new PropertiesPropertySource("dotenv", properties));
    }
}