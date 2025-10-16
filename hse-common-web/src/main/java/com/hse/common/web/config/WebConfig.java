package com.hse.common.web.config;

import com.hse.common.core.utils.MessageUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Web配置
 *
 * @author HSE
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 国际化消息源
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // 设置国际化资源文件基础名（不包含语言和扩展名）
        messageSource.setBasenames("i18n/messages");
        // 设置默认编码
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // 设置缓存时间（秒），生产环境建议设置，开发环境可以设置为-1不缓存
        messageSource.setCacheSeconds(3600);
        // 设置当找不到消息时返回消息码本身而不是抛出异常
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    /**
     * 国际化解析器
     * 从请求头 Accept-Language 获取语言信息
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        // 设置默认语言为中文
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }

    /**
     * 国际化拦截器
     * 支持通过请求参数 lang 切换语言
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // 设置请求参数名
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * 注册国际化拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * 初始化 MessageUtils
     */
    @Bean
    public MessageUtils messageUtils(MessageSource messageSource) {
        return new MessageUtils(messageSource);
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Content-Language")
                .allowCredentials(true)
                .maxAge(3600);
    }
}