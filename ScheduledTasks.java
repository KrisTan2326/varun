package com.socio.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    /**
     * This scheduler runs every day at midnight.
     * In a real application, it would query the database for users whose birthday is today
     * and create a "Happy Birthday!" post on their behalf.
     */
    @Scheduled(cron = "0 0 0 * * *") // Runs at 00:00:00 every day
    public void createBirthdayPosts() {
        log.info("Checking for birthdays on: {}", LocalDate.now());
        // 1. Find users with today's birthday from the database.
        // 2. For each user, create a new Post object.
        // 3. Save the new post to the database.
        log.info("Birthday post job finished.");
    }
}