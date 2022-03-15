package com.xisvaldo.quartz.poc.usecases

import com.xisvaldo.quartz.poc.adapters.job.SimpleJob
import org.quartz.*

class UpsertScheduler(private val scheduler: Scheduler) {

    operator fun invoke(schedulerInput: UpsertSchedulerInput) {
        val job = newJob(
            identity = schedulerInput.identity,
            group = schedulerInput.group,
            description = schedulerInput.description
        )
        scheduler.scheduleJob(
            job,
            setOf(triggerJob(jobDetail = job, cron = schedulerInput.cron)),
            true
        )
    }

    private fun newJob(identity: String, group: String, description: String): JobDetail =
        JobBuilder.newJob()
            .ofType(SimpleJob::class.java)
            .storeDurably()
            .withIdentity(identity, group)
            .requestRecovery(true)
            .withDescription(description)
            .build()

    private fun triggerJob(jobDetail: JobDetail, cron: String): CronTrigger {

        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.key.name, jobDetail.key.group)
            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
            .build()
    }
}
