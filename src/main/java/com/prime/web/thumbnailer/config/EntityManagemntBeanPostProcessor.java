package com.prime.web.thumbnailer.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;

public class EntityManagemntBeanPostProcessor implements BeanPostProcessor{

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof LocalContainerEntityManagerFactoryBean){
			try {
				LocalContainerEntityManagerFactoryBean emFactory = (LocalContainerEntityManagerFactoryBean) bean;
				Field field = emFactory.getClass().getDeclaredField("internalPersistenceUnitManager");
				field.setAccessible(true);
				DefaultPersistenceUnitManager defaultPersistanceUnitManager =  (DefaultPersistenceUnitManager) field.get(emFactory);
				field = defaultPersistanceUnitManager.getClass().getDeclaredField("packagesToScan");
				field.setAccessible(true);
				String[] packageToScans = (String[]) field.get(defaultPersistanceUnitManager);
				List<String> newPackageToScans = new ArrayList<String>(Arrays.asList(packageToScans));
				newPackageToScans.add("com.prime.web.thumbnailer.domain");
				field.set(defaultPersistanceUnitManager, newPackageToScans.toArray(new String[]{}));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
