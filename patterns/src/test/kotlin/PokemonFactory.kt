
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

sealed class TypePokemon(){
    object Agua : TypePokemon()
    object Fuego : TypePokemon()
    object Tierra : TypePokemon()
}

class Pokemon(val nombre:String)

class PokemonFactory{
    companion object{

        @JvmStatic
        fun createInstance(typePokemon: TypePokemon):Pokemon{
            val name = when(typePokemon){
                is TypePokemon.Agua-> "agua"
                is TypePokemon.Fuego-> "fuego"
                is TypePokemon.Tierra -> "tierra"
            }
            return Pokemon(name)
        }
    }
}

class FactoryPokenMethodTest {

    @Test
    fun `FactoryMethod`() {
        val pokemon = PokemonFactory.createInstance(TypePokemon.Agua)
        println("Type: ${pokemon.nombre}")

      assertThat(pokemon.nombre).isEqualTo("agua")
    }
}


