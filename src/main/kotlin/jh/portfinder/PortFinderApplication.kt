package jh.portfinder

import jh.portfinder.data.repository.geofence.GeoFenceRepository
import jh.portfinder.service.DailyZeroSpeedPointCollector
import jh.portfinder.service.GeoFenceGenerator
import jh.portfinder.service.ZeroSpeedPointGrouper
import jh.portfinder.util.DateProgression
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@SpringBootApplication
class PortFinderApplication

fun main(args: Array<String>) {
	val context = runApplication<PortFinderApplication>(*args)

//	collectDailyZeroSpeedPoint(context.getBean(DailyZeroSpeedPointCollector::class.java))
//	groupZeroSpeedPoint(context.getBean(ZeroSpeedPointGrouper::class.java))
	generateGeoFence(context.getBean(GeoFenceGenerator::class.java))
	printGeoFence(context.getBean(GeoFenceRepository::class.java))

//	debugging(context.getBean(GeoFenceGenerator::class.java), "group-525")
}

fun debugging(geoFenceGenerator: GeoFenceGenerator, groupName: String) {
	geoFenceGenerator.generateGeoFence(groupName)
		.forEach {
			println("{ lat: ${it.second}, lng: ${it.first} },")
		}
}

fun printGeoFence(geoFenceRepository: GeoFenceRepository) {
	val geoFences = geoFenceRepository.read()
	println("[")
	geoFences.forEach {
		println("\t[")
		it.forEach {
			println("\t\t{ lat: ${it.second}, lng: ${it.first} },")
		}
		println("\t\t{ lat: ${it.get(0).second}, lng: ${it.get(0).first} },")
		println("\t],")
	}
	println("]")
}

fun collectDailyZeroSpeedPoint(dailyZeroSpeedPointCollector: DailyZeroSpeedPointCollector) {
	operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

	val year = 2023
	val month = 10
	val from = LocalDate.of(year, month, 1)
	val to = from.plusMonths(4).minusDays(1)

	val logger = logger<PortFinderApplication>()

	for (date in from..to) {
		logger.info("Collecting daily zero speed point for $date")
		dailyZeroSpeedPointCollector.collect(date)
	}
}

fun groupZeroSpeedPoint(zeroSpeedPointGrouper: ZeroSpeedPointGrouper) {
	val year = 2023
	val month = 10
	val from = LocalDate.of(year, month, 1)
//	val to = from
	val to = from.plusMonths(4).minusDays(1)

	val logger = logger<PortFinderApplication>()

	logger.info("Grouping zero speed point from $from to $to")
	zeroSpeedPointGrouper.group(from, to)
}


fun generateGeoFence(geoFenceGenerator: GeoFenceGenerator) {
	val logger = logger<PortFinderApplication>()
	logger.info("Generating geo fence")
	geoFenceGenerator.generateGeoFence()
}


inline fun <reified T> logger() : Logger = LoggerFactory.getLogger(T::class.java)