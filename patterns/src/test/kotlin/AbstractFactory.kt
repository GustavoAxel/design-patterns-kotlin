import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

interface Shape

class Circle : Shape
class Square : Shape

class RedCircle : Shape
class BlueCircle : Shape
class RedSquare : Shape
class BlueSquare : Shape

enum class Colors {
    RED, BLUE
}

abstract class ShapeFactory {

    abstract fun makeShape(color: Colors): Shape

    companion object {
        inline fun <reified T : Shape> createFactory(): ShapeFactory =
                when (T::class) {
                    Circle::class -> CircleFactory()
                    Square::class -> SquareFactory()
                    else -> throw IllegalArgumentException()
                }
    }
}

class CircleFactory : ShapeFactory() {
    override fun makeShape(color: Colors): Shape = when (color) {
        Colors.RED -> RedCircle()
        Colors.BLUE -> BlueCircle()
    }
}

class SquareFactory : ShapeFactory() {
    override fun makeShape(color: Colors): Shape = when (color) {
        Colors.RED -> RedSquare()
        Colors.BLUE -> BlueSquare()
    }
}

class AbstractFactoryTest {

    @Test
    fun `Abstract Factory`() {
        val shapeFactory = ShapeFactory.createFactory<Circle>()
        val shape = shapeFactory.makeShape(Colors.RED)
        println("Created shape: $shape")

        assertThat(shape).isInstanceOf(RedCircle::class.java)
    }
}
