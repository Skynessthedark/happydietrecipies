package com.happydieting.dev.config;

import com.happydieting.dev.model.SystemConfigModel;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.SystemConfigRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private SystemConfigRepository systemConfigRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String DATA_INITIALIZED_KEY = "data.initialized";

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Optional<SystemConfigModel> config = systemConfigRepository.findByKey(DATA_INITIALIZED_KEY);
        if (config.isPresent() && Boolean.parseBoolean(config.get().getValue())) {
            return;
        }

        createUserData();

        SystemConfigModel initializedConfig = new SystemConfigModel();
        initializedConfig.setKey(DATA_INITIALIZED_KEY);
        initializedConfig.setValue("true");
        initializedConfig.setDescription("Indicates whether initial data has been loaded");
        systemConfigRepository.save(initializedConfig);
    }

    private void createUserData() {
        UserModel user1 = new UserModel();
        user1.setFullName("Test1 Kullanıcı");
        user1.setEmail("test1@example.com");
        user1.setUsername("test1@example.com");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setBio("İlk test kullanıcısı");
        //TODO: user1.setAvatar(); mediamodel den sonra setlenecek
        entityManager.persist(user1);

        UserModel user2 = new UserModel();
        user2.setFullName("Test2 Kullanıcı");
        user2.setEmail("test2@example.com");
        user2.setUsername("test2@example.com");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setBio("İkinci test kullanıcısı");
        //TODO: user2.setAvatar(); mediamodel den sonra setlenecek
        entityManager.persist(user2);
    }

} 