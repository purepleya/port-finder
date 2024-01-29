package jh.portfinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PortFinderApplication

fun main(args: Array<String>) {
	runApplication<PortFinderApplication>(*args)
}
