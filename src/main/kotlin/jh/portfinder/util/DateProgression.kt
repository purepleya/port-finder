package jh.portfinder.util

import java.time.LocalDate

class DateProgression(override val start: LocalDate,
                      override val endInclusive: LocalDate,
                      val step: Long = 1L) : Iterable<LocalDate>, ClosedRange<LocalDate> {
    override fun iterator(): Iterator<LocalDate> {
        return object: Iterator<LocalDate> {
            var current = start
            override fun hasNext() = current <= endInclusive
            override fun next(): LocalDate {
                val next = current
                current = current.plusDays(step)
                return next
            }
        }
    }

    infix fun step(days: Long) = DateProgression(start, endInclusive, days)
}

