package com.xisvaldo.quartz.poc.controller

import com.xisvaldo.quartz.poc.job.SimpleJob
import org.quartz.*
import org.quartz.impl.matchers.GroupMatcher
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@RestController
@RequestMapping("/scheduler")
class SchedulerController(@Qualifier("QuartzScheduler") private val scheduler:Scheduler) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSchedule(@RequestBody payload: SchedulerPayload) {
        val job = newJob(identity = payload.identity, description = payload.description)
        scheduler.scheduleJob(
            job,
            setOf(trigger(jobDetail = job, intervalInSeconds = payload.intervalInSeconds)),
            true
        )
    }

    fun newJob(identity: String, description: String): JobDetail =
        JobBuilder.newJob()
            .ofType(SimpleJob::class.java)
            .storeDurably()
            .withIdentity(JobKey.jobKey(identity))
            .withDescription(description)
            .build()

    /**
     * TODO: split it into a dedicate class (scheduler/job creation/trigger creation) or even at kotlin-commons
     */
    fun trigger(jobDetail: JobDetail, intervalInSeconds: Int): SimpleTrigger {
        val existentJobNextTriggerDate = getExistentJobNextTrigger()
        val startTime: Date
        if (existentJobNextTriggerDate == null) {
            startTime = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        } else {
            startTime = existentJobNextTriggerDate
            startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .plus(intervalInSeconds.toLong(), ChronoUnit.SECONDS)
        }
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.key.name, jobDetail.key.group)
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(intervalInSeconds))
            .startAt(startTime)// TODO test if this method can prevent that the new cron be triggered immediately
            .build()
    }

    fun getExistentJobNextTrigger(): Date? {
        for (group in scheduler.jobGroupNames) {
            for (jobKey in scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
                return scheduler.getTriggersOfJob(jobKey).first().nextFireTime
            }
        }
        return null
    }

    data class SchedulerPayload(val identity: String, val description: String, val intervalInSeconds: Int)
}
