package com.yjfei.antibot.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static class ApplicationContextHolder {
        private static final InnerContextResource CONTEXT_PROV = new InnerContextResource();

        private ApplicationContextHolder() {
        }
    }

    private static final class InnerContextResource {
        private ApplicationContext context;

        private InnerContextResource() {
            super();
        }

        private void setContext(ApplicationContext context) {
            this.context = context;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.CONTEXT_PROV.context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.CONTEXT_PROV.setContext(applicationContext);
    }
}
