package com.xisvaldo.quartz.poc.usecases

import com.xisvaldo.quartz.poc.adapters.job.SimpleJob
import com.xisvaldo.quartz.poc.adapters.persistence.SchedulingRepository
import com.xisvaldo.quartz.poc.entities.Scheduling
import org.quartz.*
import org.quartz.impl.matchers.GroupMatcher
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

class UpsertScheduler(private val scheduler: Scheduler, private val schedulingRepository: SchedulingRepository) {

    operator fun invoke(schedulerInput: UpsertSchedulerInput) {
        val job = newJob(identity = schedulerInput.identity, description = schedulerInput.description)
        scheduler.scheduleJob(
            job,
            setOf(triggerJob(jobDetail = job, intervalInSeconds = schedulerInput.intervalInSeconds)),
            true
        )
        with(schedulerInput) {
            val scheduling = Scheduling(
                identity = identity,
                description = description,
                startTime = LocalDateTime.now(),
                intervalInSeconds = intervalInSeconds
            )
            schedulingRepository.save(scheduling)
        }
    }

    private fun newJob(identity: String, description: String): JobDetail =
        JobBuilder.newJob()
            .ofType(SimpleJob::class.java)
            .storeDurably()
            .withIdentity(JobKey.jobKey(identity))
            .withDescription(description)
            .build()

    /**
     * TODO: split it into a dedicate class (scheduler/job creation/trigger creation) or even at kotlin-commons
     */
    private fun triggerJob(jobDetail: JobDetail, intervalInSeconds: Int): SimpleTrigger {
        val existentJobNextTriggerDate = getExistentJobNextTriggerDate()
        val startTime: Date
        if (existentJobNextTriggerDate == null) {
            startTime = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        } else {
            startTime = existentJobNextTriggerDate
            startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .plus(intervalInSeconds.toLong(), ChronoUnit.SECONDS)
        }

        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.key.name, jobDetail.key.group)
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(intervalInSeconds))
            .startAt(startTime) // TODO test if this method can prevent that the new cron be triggered immediately
            .build()
    }

    private fun getExistentJobNextTriggerDate(): Date? {
        for (group in scheduler.jobGroupNames) {
            for (jobKey in scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
                return scheduler.getTriggersOfJob(jobKey).first().nextFireTime
            }
        }
        return null
    }
}
