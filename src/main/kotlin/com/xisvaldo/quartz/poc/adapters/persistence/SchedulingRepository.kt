package com.xisvaldo.quartz.poc.adapters.persistence

import com.xisvaldo.quartz.poc.entities.Scheduling
import org.springframework.data.jpa.repository.JpaRepository

interface SchedulingRepository : JpaRepository<Scheduling, String>
