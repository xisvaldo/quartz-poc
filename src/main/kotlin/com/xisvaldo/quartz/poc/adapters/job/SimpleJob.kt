package com.xisvaldo.quartz.poc.adapters.job

import com.sun.istack.internal.logging.Logger
import org.quartz.Job
import org.quartz.JobExecutionContext

const val EXECUTION_TIME = 15000L;

class SimpleJob : Job {
    private val logger = Logger.getLogger(SimpleJob::class.java)

    override fun execute(context: JobExecutionContext?) {
        logger.info("------------------------")
        logger.info("Job '${context?.jobDetail?.key?.name}' fired at ${context?.fireTime?.toInstant()}")
        logger.info("Job description: '${context?.jobDetail?.description}'")
        Thread.sleep(EXECUTION_TIME)
        logger.info("Next job scheduled at ${context?.nextFireTime?.toInstant()}")
    }
}
