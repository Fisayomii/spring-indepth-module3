package javatesa6.module3beanproject;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// 1. The shared interface
interface NotificationService2 {
    void send(String message);
}

// 2. Marked with @Primary - Spring picks this by default if no qualifier is named
@Component("emailGateway")
@Primary
class EmailNotificationService implements NotificationService2 {
    @Override
    public void send(String msg) {
        System.out.println(">>> [Primary Winner] Email sent: " + msg);
    }
}

// 3. The alternative implementation
@Component("smsGateway")
class SmsNotificationService implements NotificationService2 {
    @Override
    public void send(String msg) {
        System.out.println(">>> [Qualifier Target] SMS sent: " + msg);
    }
}

// 4. Testing both in action
@Component
class QualifierLabRunner {

    // Expects the @Primary default because no qualifier is stated here
    private final NotificationService2 defaultService;

    // Explicitly demands the "smsGateway" bean, bypassing @Primary
    private final NotificationService2 targetedService;

    public QualifierLabRunner(
            NotificationService2 defaultService,
            NotificationService2 targetedService) {
        this.defaultService = defaultService;
        this.targetedService = targetedService;
    }

    @Bean
    ApplicationRunner runQualifierLab() {
        return args -> {
            System.out.println("\n--- LAB: @PRIMARY & @QUALIFIER ---");

            // This will use EmailNotificationService because of @Primary
            defaultService.send("Checking default behavior...");

            // This will use SmsNotificationService because of @Qualifier("smsGateway")
            targetedService.send("Checking explicit override...");
        };
    }
}
