import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

abstract class AbstractFactoryCar {
    interface Vehicle

    class Truck : Vehicle
    class Sedan : Vehicle

    class SportTruck : Vehicle
    class MediumTruck : Vehicle
    class LowTruck : Vehicle

    class SportSedan : Vehicle
    class MediumSedan : Vehicle
    class LowSedan : Vehicle

    enum class TypeVehicle {
        SPORT, MEDIUM, LOW
    }

    abstract class FactoryVehicle {
        abstract fun createVehicle(type: TypeVehicle): Vehicle

        companion object {
            inline fun <reified T : Vehicle> createFactory(): FactoryVehicle =
                    when (T::class) {
                        Truck::class -> TruckFactory()
                        Sedan::class -> SedanFactory()
                        else -> throw IllegalArgumentException()
                    }
        }
    }

    class TruckFactory : FactoryVehicle() {
        override fun createVehicle(typeVehicle: TypeVehicle): Vehicle {
            return when (typeVehicle) {
                TypeVehicle.SPORT -> SportTruck()
                TypeVehicle.MEDIUM -> MediumTruck()
                TypeVehicle.LOW -> LowTruck()
            }
        }
    }

    class SedanFactory : FactoryVehicle() {
        override fun createVehicle(typeVehicle: TypeVehicle): Vehicle {
            return when (typeVehicle) {
                TypeVehicle.SPORT -> SportSedan()
                TypeVehicle.MEDIUM -> MediumSedan()
                TypeVehicle.LOW -> LowSedan()
            }
        }
    }

    class AbstractFactoryTest {

        @Test
        fun `Abstract Factory`() {
            val carFactory = FactoryVehicle.createFactory<Truck>()
            val vehicle = carFactory.createVehicle(TypeVehicle.SPORT)
            println("Created Truck: $vehicle")

            Assertions.assertThat(vehicle).isInstanceOf(SportTruck::class.java)
        }
    }

}

