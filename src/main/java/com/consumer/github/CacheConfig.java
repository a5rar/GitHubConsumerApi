package com.consumer.github;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.config.CacheConfiguration;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

	
	 @Bean(destroyMethod="shutdown")
	    public net.sf.ehcache.CacheManager ehCacheManager() {
	        CacheConfiguration cacheConfiguration = new CacheConfiguration();
	        cacheConfiguration.setName("repos");
	        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
	        cacheConfiguration.setMaxEntriesLocalHeap(1000);
	        cacheConfiguration.setTimeToLiveSeconds(600);
	        cacheConfiguration.setTimeToIdleSeconds(600);

	        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
	        config.addCache(cacheConfiguration);

	        return net.sf.ehcache.CacheManager.newInstance(config);
	    }

	    @Bean
	    @Override
	    public CacheManager cacheManager() {
	        return new EhCacheCacheManager(ehCacheManager());
	    }


	@Override
	public CacheResolver cacheResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Bean
	public KeyGenerator keyGenerator() {
		// TODO Auto-generated method stub
		return new SimpleKeyGenerator();
	}

	@Override
	public CacheErrorHandler errorHandler() {
		// TODO Auto-generated method stub
		return null;
	}

}
