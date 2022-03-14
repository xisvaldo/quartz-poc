package com.xisvaldo.quartz.poc.entities

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "scheduling")
@Entity
data class Scheduling(
    @Id @Column(length = 64) val identity: String,
    @Column val description: String?,
    @Column val startTime: LocalDateTime,
    @Column val intervalInSeconds: Int
)
