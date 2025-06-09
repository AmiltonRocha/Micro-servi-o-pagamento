package unifor.pagamento.pagamento.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;

@Configuration
public class EurekaConfig {

    @Value("${eureka.instance.hostname}")
    private String hostname;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        config.setHostname(hostname);
        config.setPreferIpAddress(false);
        config.setNonSecurePortEnabled(true);
        config.setSecurePortEnabled(false);
        return config;
    }

    @Bean
    public InetUtils inetUtils() {
        return new InetUtils(new InetUtilsProperties());
    }
} 