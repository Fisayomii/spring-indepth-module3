//package javatesa6.module3beanproject;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Positive;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//
//import java.util.Map;
//
//@SpringBootApplication
//@EnableConfigurationProperties(SettlementApplication.SettlementProperties.class)
//public class SettlementApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(SettlementApplication.class, args);
//    }
//
//    // 1. Type-safe properties bound from application.yml
//    @ConfigurationProperties("ledger.settlement")
//    public record SettlementProperties(
//            @NotBlank String payoutUrl,
//            @Positive long minPayoutMinor
//    ) {}
//
//    // 2. Custom bean utilizing those properties cleanly
//    @Bean
//    public SettlementClient settlementClient(SettlementProperties properties) {
//        return new SettlementClient(properties.payoutUrl(), properties.minPayoutMinor());
//    }
//
//    public static class SettlementClient {
//        private final String payoutUrl;
//        private final long minPayoutMinor;
//
//        public SettlementClient(String payoutUrl, long minPayoutMinor) {
//            this.payoutUrl = payoutUrl;
//            this.minPayoutMinor = minPayoutMinor;
//        }
//    }
//
//    // 3. The Check: Programmatically fetch and print the Condition Evaluation Report
//    @Bean
//    public CommandLineRunner runConditionCheck(ConfigurableListableBeanFactory beanFactory) {
//        return args -> {
//            // Retrieve the report directly using the bean factory
//            ConditionEvaluationReport evaluationReport = ConditionEvaluationReport.get(beanFactory);
//
//            System.out.println("\n=======================================================");
//            System.out.println("          AUTO-CONFIGURATION CONDITION REPORT CHECK       ");
//            System.out.println("=======================================================");
//
//            Map<String, ConditionEvaluationReport.ConditionAndOutcomes> matchResults =
//                    evaluationReport.getConditionAndOutcomesBySource();
//
//            System.out.println("Total Auto-Configuration Sources Evaluated: " + matchResults.size());
//
//            // Print a few examples of evaluated classes
//            System.out.println("\nSample Evaluated Auto-Configuration Sources:");
//            matchResults.entrySet().stream()
//                    .limit(5)
//                    .forEach(entry -> {
//                        String className = entry.getKey();
//                        if (className != null && className.contains(".")) {
//                            className = className.substring(className.lastIndexOf('.') + 1);
//                        }
//                        System.out.println("  [EVALUATED] " + className);
//                    });
//
//            System.out.println("=======================================================\n");
//        };
//    }
//}
