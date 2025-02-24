package posting.job.internal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InternalApplication

fun main(args: Array<String>) {
	runApplication<InternalApplication>(*args)
}
