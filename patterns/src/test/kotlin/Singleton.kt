import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

object KotlinObject {
    init {
        println("Initializing with object: $this")
    }

    fun print(): KotlinObject = apply { println("Printing with object: $this") }
}

class LogEvents private constructor(from: String) {
    companion object {

        private var INSTANCE: LogEvents? = null

        private lateinit var data: String

        fun getInstance(from: String): LogEvents = INSTANCE
                ?: synchronized(this) {
                    INSTANCE ?: LogEvents(from).also { INSTANCE = it }
                }
    }

    init {
        data = from
        println("Initializing $data with object: $this")
    }

    fun logEvent(): LogEvents = apply {
        println("$data on $this")
    }
}

class SingletonTest {

    @Test
    fun `Singleton`() {
        println("Object")
        val first = KotlinObject.print()
        val second = KotlinObject.print()

        assertThat(first).isSameAs(KotlinObject)
        assertThat(second).isSameAs(KotlinObject)
    }

    @Test
    fun `Double checked locking`() {
        println("Double checked locking")
        val first = LogEvents.getInstance("First").logEvent()
        val second = LogEvents.getInstance("Second").logEvent()

        assertThat(first).isSameAs(LogEvents.getInstance("First"))
        assertThat(second).isSameAs(LogEvents.getInstance("First"))
    }
}
