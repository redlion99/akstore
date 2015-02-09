package me.smartco.akstore.biz.spring;

import me.smartco.akstore.common.task.BackgroundTaskStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by libin on 15-1-9.
 */
@Configuration
@ComponentScan(basePackages="me.smartco.akstore")
public class BizConfiguration {
    @Autowired(required = false)
    private BackgroundTaskStarter backgroundTaskStarter;
}
