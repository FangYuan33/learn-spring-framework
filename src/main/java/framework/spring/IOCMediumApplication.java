package framework.spring;

import framework.spring.config.BasePackageClassConfiguration;
import framework.spring.moduleimport.TavernConfiguration;
import framework.spring.pojo.Person;
import framework.spring.properties.JdbcProperties;
import framework.spring.properties.JdbcYaml;
import framework.spring.resolver.DogProtocolResolver;
import framework.spring.service.impl.RegisterService;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class IOCMediumApplication {

    public static void main(String[] args) throws IOException {
        propertySource();
    }

    private static void propertySource() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JdbcProperties.class);

        System.out.println(context.getBean(JdbcProperties.class));

        System.out.println(context.getBean(JdbcYaml.class));
    }

    private static void protocolResolver() throws IOException {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        DogProtocolResolver dogProtocolResolver = new DogProtocolResolver();
        defaultResourceLoader.addProtocolResolver(dogProtocolResolver);

        Resource resource = defaultResourceLoader.getResource("dog:dog.txt");
        InputStream inputStream = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String readLine;
        while ((readLine = bufferedReader.readLine()) != null) {
            System.out.println(readLine);
        }
        bufferedReader.close();
    }

    private static void componentScan() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BasePackageClassConfiguration.class);

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

    private static void moduleImport() {
        // ????????????profile-city?????????????????????????????????????????????????????????????????????????????????????????????????????????refresh????????????????????????profile????????????
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TavernConfiguration.class);
//        context.getEnvironment().setActiveProfiles("city");

        // ?????????ok???
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.getEnvironment().setActiveProfiles("city");
        context.register(TavernConfiguration.class);
        context.refresh();

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

    private static void listener() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                "framework.spring.listener",
                "framework.spring.service.impl");
        System.out.println("?????????????????????...");

        RegisterService registerService = context.getBean(RegisterService.class);
        registerService.register("FangYuan");

        context.close();
        System.out.println("????????????...");
    }

    /**
     * listableBeanFactory??????????????????????????????Bean
     */
    private static void listableBeanFactoryCurrentBeanFactory() {
        ClassPathResource resource = new ClassPathResource("listable-container.xml");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
        definitionReader.loadBeanDefinitions(resource);

        // ??????????????????????????????Bean
        System.out.println("??????xml?????????????????????Bean???");
        Stream.of(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        beanFactory.registerSingleton("person3", new Person());
        // ???????????????????????????Bean
        System.out.println("?????????????????????Bean?????????????????????Bean???");
        Stream.of(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        System.out.println("????????????????????????person3???" + beanFactory.getBean("person3"));
    }
}
