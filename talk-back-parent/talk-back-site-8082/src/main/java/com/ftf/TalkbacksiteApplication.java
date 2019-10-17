package com.ftf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ftf.utils.SnowflakeIdWorker;

@SpringBootApplication
public class TalkbacksiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkbacksiteApplication.class, args);
	}

	@Bean
	public SnowflakeIdWorker idWorker() {
		return new SnowflakeIdWorker(0,0);
	}
//	@Bean
//    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
//        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
//        try {
//            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
//        } catch (NoSuchBeanDefinitionException ignore) {
//        }
// 
//        // Don't save _class to mongo
//        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return mappingConverter;
//    }
}
