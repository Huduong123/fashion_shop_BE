package e_commerce.monolithic.config;

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

        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            properties.setProperty(entry.getKey(), entry.getValue());
        });

        environment.getPropertySources().addFirst(new PropertiesPropertySource("dotenv", properties));
    }
}