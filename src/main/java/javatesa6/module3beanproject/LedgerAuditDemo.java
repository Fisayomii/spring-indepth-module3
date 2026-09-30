package javatesa6.module3beanproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class LedgerAuditDemo {

    // 1. The Dependency Class
    public static class AuditWriter {
        public void writeAudit(String action) {
            System.out.println("Audit Log Recorded: " + action);
        }
    }

    // 2. The Main Service
    public static class AuditService {
        private final AuditWriter writer;

        public AuditService(AuditWriter writer) {
            this.writer = writer;
        }

        public void log(String message) {
            writer.writeAudit(message);
        }
    }

    // 3. The Auto-Configuration Class (simulating the library component)
    @Configuration
    @ConditionalOnClass(AuditWriter.class)
    @ConditionalOnProperty(prefix = "ledger.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class AuditAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AuditService auditService(AuditWriter writer) {
            System.out.println("-> AuditAutoConfiguration: Creating default AuditService bean.");
            return new AuditService(writer);
        }

        @Bean
        @ConditionalOnMissingBean
        public AuditWriter auditWriter() {
            System.out.println("-> AuditAutoConfiguration: Creating default AuditWriter bean.");
            return new AuditWriter();
        }
    }

    // 4. The Spring Boot Application with the main method
    @SpringBootApplication
    public static class Application {
        public static void main(String[] args) {
            System.out.println("Starting Spring Boot Application...\n");

            var context = SpringApplication.run(Application.class, args);

            System.out.println("\nApplication started successfully!");

            // Verify and use the auto-configured bean
            if (context.containsBean("auditService")) {
                AuditService service = context.getBean(AuditService.class);
                service.log("Payout approved by manager.");
            } else {
                System.out.println("AuditService bean was not found in the context.");
            }
        }
    }
}
