package com.xisvaldo.quartz.poc.configuration

import org.quartz.Scheduler
import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SpringBeanJobFactory

@Configuration
class ApplicationConfiguration {

    class AutoWireCapableBeanJobFactory(private val beanFactory: AutowireCapableBeanFactory) : SpringBeanJobFactory() {
        override fun createJobInstance(bundle: TriggerFiredBundle): Any {
            val jobInstance = super.createJobInstance(bundle)
            beanFactory.autowireBean(jobInstance)
            beanFactory.initializeBean(jobInstance, bundle.jobDetail.key.name)
            return jobInstance
        }
    }

    @Bean
    fun getSchedulerFactory(applicationContext: ApplicationContext): SchedulerFactoryBean {
        val schedulerFactoryBean = SchedulerFactoryBean()
        schedulerFactoryBean.setJobFactory(AutoWireCapableBeanJobFactory(applicationContext.autowireCapableBeanFactory))
        return schedulerFactoryBean
    }

    @Bean
    @Qualifier("QuartzScheduler")
    fun getScheduler(schedulerFactory: SchedulerFactoryBean): Scheduler {
        val scheduler = schedulerFactory.scheduler
        scheduler.start()
        return scheduler
    }
}
