package unifor.pagamento.pagamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir requisições do seu front-end
        config.addAllowedOrigin("https://vet-fofinhos-ronaldo.vercel.app");
        
        // Permitir todos os métodos HTTP
        config.addAllowedMethod("*");
        
        // Permitir todos os headers
        config.addAllowedHeader("*");
        
        // Permitir credenciais (cookies, etc)
        config.setAllowCredentials(true);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 