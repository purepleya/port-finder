package jh.portfinder

import jh.portfinder.service.DailyZeroSpeedPointCollector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDate

@SpringBootApplication
class PortFinderApplication

fun main(args: Array<String>) {
	val context = runApplication<PortFinderApplication>(*args)

	val dailyZeroSpeedPointCollector = context.getBean(DailyZeroSpeedPointCollector::class.java)
	collectDailyZeroSpeedPoint(dailyZeroSpeedPointCollector)
}

fun collectDailyZeroSpeedPoint(dailyZeroSpeedPointCollector: DailyZeroSpeedPointCollector) {
	val year = 2024
	val month = 1
	val from = LocalDate.of(year, month, 1)
	val to = from.plusMonths(1).minusDays(1)

	val logger = logger<PortFinderApplication>()

	for (date in from.dayOfMonth .. to.dayOfMonth) {
		logger.info("Collecting daily zero speed point for $year-$month-$date")
		dailyZeroSpeedPointCollector.collect(LocalDate.of(year, month, date))
	}
}


inline fun <reified T> logger() : Logger = LoggerFactory.getLogger(T::class.java)