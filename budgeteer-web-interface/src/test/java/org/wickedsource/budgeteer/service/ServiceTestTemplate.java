package org.wickedsource.budgeteer.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = {"classpath:spring-service.xml", "classpath:spring-repository-mock.xml"})
public abstract class ServiceTestTemplate {

    @Autowired
    private ApplicationContext context;

    @Before
    public void resetMocks() {
        for (String name : context.getBeanDefinitionNames()) {
            if (!"workRecordDatabaseImporter".equals(name) && !"planRecordDatabaseImporter".equals(name)) {
                // excluding prototype beans with constructor arguments
                Object bean = context.getBean(name);
                if (MockUtil.isMock(bean)) {
                    Mockito.reset(bean);
                }
            }
        }
    }

}
