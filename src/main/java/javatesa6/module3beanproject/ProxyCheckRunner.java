package javatesa6.module3beanproject;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
final class FinalService {
    public void performTask() {
        System.out.println("Final task executed.");
    }
}

@Service
class NormalService {
    public void performTask() {
        System.out.println("Normal task executed.");
    }
}

@Component
class ProxyCheckRunner {

    private final FinalService finalService;
    private final NormalService normalService;

    public ProxyCheckRunner(FinalService finalService, NormalService normalService) {
        this.finalService = finalService;
        this.normalService = normalService;
    }

    @Bean
    ApplicationRunner checkProxies() {
        return args -> {
            System.out.println("\n--- LAB 2: THE FINAL BUG ---");
            boolean isFinalProxied = AopUtils.isAopProxy(finalService);
            boolean isNormalProxied = AopUtils.isAopProxy(normalService);

            System.out.println("Is FinalService proxied? " + isFinalProxied + " (Warning: AOP / Transactional magic is DEAD here!)");
            System.out.println("Is NormalService proxied? " + isNormalProxied + " (Safe: Spring can subclass this)");
        };
    }
}