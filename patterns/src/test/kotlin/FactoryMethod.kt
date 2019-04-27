import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

sealed class Country {
    object Mexico : Country()
}

object UnitedStatesOfAmerica : Country()
class Germany(val someProperty: String = "") : Country()
data class Canada(val someProperty: String) : Country()
//object France : Country()

class Beer(
        val type: String
)

class BeerFactory {

    fun beerForCountry(country: Country): Beer =
            when (country) {
                is Country.Mexico -> Beer("Corona")
                is UnitedStatesOfAmerica -> Beer("Budlight")
                is Germany -> Beer("Paulaner")
                is Canada -> Beer("Molson")
            }
}



class FactoryMethodTest {

    @Test
    fun `FactoryMethod`() {
        val mexicanBeer = BeerFactory().beerForCountry(Country.Mexico).type
        println("Mexican beer: $mexicanBeer")

        val germanBeer = BeerFactory().beerForCountry(Germany()).type
        println("German beer: $germanBeer")

        assertThat(germanBeer).isEqualTo("Paulaner")
        assertThat(mexicanBeer).isEqualTo("Corona")
    }
}

