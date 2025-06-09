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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Configuration
public class EurekaConfig {

    @Value("${eureka.instance.hostname}")
    private String hostname;

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        config.setHostname(hostname);
        config.setPreferIpAddress(false);
        config.setNonSecurePortEnabled(true);
        config.setSecurePortEnabled(false);
        config.setInstanceId(hostname + ":" + System.currentTimeMillis());
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public InetUtils inetUtils() {
        return new InetUtils(new InetUtilsProperties());
    }
} 