
import com.example.pokectcollection.data.LocalDataSource
import com.example.pokectcollection.data.card.CardRepositoryImp
import com.example.pokectcollection.data.card.FirebaseCardDataSource
import com.example.pokectcollection.data.user.FirebaseUserDataSource
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.cards.model.CardAbilities
import com.example.pokectcollection.domain.cards.model.CardAttack
import com.example.pokectcollection.domain.cards.model.CardImages
import com.example.pokectcollection.domain.cards.model.CardLegalities
import com.example.pokectcollection.domain.cards.model.CardResistance
import com.example.pokectcollection.domain.cards.model.CardWeakness
import com.example.pokectcollection.domain.setlist.model.Set
import com.example.pokectcollection.domain.setlist.model.SetImages
import com.example.pokectcollection.domain.setlist.model.SetLegalities
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [CardRepositoryImp].
 *
 * This class contains unit tests for various methods of the [CardRepositoryImp] class,
 * verifying the expected behavior when interacting with the local and remote data sources.
 */
class CardRepositoryImpTest {

    private val localDataSource = mockk<LocalDataSource>()
    private val firebaseCardDataSource = mockk<FirebaseCardDataSource>()
    private val firebaseUserDataSource = mockk<FirebaseUserDataSource>()

    private val cardRepository = CardRepositoryImp(
        localDataSource,
        firebaseCardDataSource,
        firebaseUserDataSource
    )

    @After
    fun clearMocks() {
        clearAllMocks()
    }

    /**
     * Test for [CardRepositoryImp.getCardById].
     *
     * Verifies that the correct card is returned when a valid card ID is provided.
     */
    @Test
    fun `when getting card by id, given data source, then it should return domain data card`() =
        runTest {
            // Given
            val card = _card.copy(
                id = "Test id",
                name = "Test name",
                rarity = "Test rarity",
                artist = "Test artist",
                evolvesFrom = "Test evolvesFrom"
            )
            every { localDataSource.getCardById(any(), any()) } returns flowOf(card)

            // When
            val result = cardRepository.getCardById("Test id", "Test id").first()

            // Then
            assertEquals(expected = card, actual = result)
        }


    /**
     * Test for [CardRepositoryImp.getCardList].
     *
     * Verifies that the correct list of cards is returned when a valid set ID is provided.
     */
    @Test
    fun `when getting card list by set id, given data source, then it should return list of domain data cards`() = runTest {
        // Given
        val cards = listOf(
            _card.copy(
                id = "Test id 1",
                name = "Test name 1",
                rarity = "Test rarity 1",
                artist = "Test artist 1",
                evolvesFrom = "Test evolvesFrom 1"
            ),
            _card.copy(
                id = "Test id 2",
                name = "Test name 2",
                rarity = "Test rarity 2",
                artist = "Test artist 2",
                evolvesFrom = "Test evolvesFrom 2"
            )
        )
        every { localDataSource.getSetCardList(any()) } returns flowOf(cards)

        // When
        val result = cardRepository.getCardList("Test set id").first()

        // Then
        assertEquals(expected = cards, actual = result)
    }

    //GetCustomCardById
    @Test
    fun `when getting custom card by id, given data source, then it should return domain data card`() = runTest {
        // Given
        val card = _card.copy(
            id = "Test id",
            name = "Test name",
            rarity = "Test rarity",
            artist = "Test artist",
            evolvesFrom = "Test evolvesFrom"
        )
        every { localDataSource.getCustomCardById(any(), any()) } returns card

        // When
        val result = cardRepository.getCustomCardById("Test set id", "Test id")

        // Then
        assertEquals(expected = card, actual = result)
    }

    //UpdateLocalCardOwnership
    @Test
    fun `when updating local card ownership, given data source, then it should update ownership in data source`() = runTest {

        // Given
        val card = _card.copy(
            id = "Test id",
            name = "Test name",
            rarity = "Test rarity",
            artist = "Test artist",
            evolvesFrom = "Test evolvesFrom",

        )

        every { localDataSource.updateCardOwnership(any(), any()) } returns flowOf(listOf(card))

        // When
        val result =  cardRepository.updateLocalCardOwnership("Test set id", "Test card id").first()

        // Then
        assertEquals(expected =listOf(card), actual = result )
    }

    /**
     * Test for [CardRepositoryImp.loadUserAchievements].
     *
     * Verifies that the user's achievements are loaded and updated correctly.
     */
    @Test
    fun `when loading user achievements, given achievement list, then it should update achievements list correctly`() = runTest {
        // Given
        val achievementList = listOf("Achievement 1", "Achievement 2", "Achievement 3")
        val repository = CardRepositoryImp(localDataSource, firebaseCardDataSource, firebaseUserDataSource)

        // Mock the achievements list in localDataSource
        val achievements = mutableListOf<String>()
        every { localDataSource.loadUserAchievements(any()) } answers {
            achievements.clear()
            achievements.addAll(firstArg())
        }

        // When
        repository.loadUserAchievements(achievementList)

        // Then
        assertEquals(expected = achievementList, actual = achievements)
    }




    /**
     * Test for [CardRepositoryImp.updateLocalCardOwnership].
     *
     * Verifies that the ownership of the card is updated correctly in the data source.
     */
    @Test
    fun `when getting completed sets, then it should return the number of completed sets`() = runTest {
        // Given
        val completedSets = 5
        coEvery { localDataSource.getCompletedSets() } returns flowOf(completedSets)

        // When
        val result = cardRepository.getCompletedSets().first()

        // Then
        assertEquals(completedSets, result)
    }


    /**
     * Test for [CardRepositoryImp.getAllCards].
     *
     * Verifies that the correct list of all cards is returned.
     */
    @Test
    fun `when getting all cards, then it should return the list of all cards`() = runTest {
        // Given
        val set = Set(
            id = "1",
            name = "Base Set",
            series = "Series 1",
            total = 102,
            legalities = SetLegalities(
                expanded = "Legal",
                unlimited = "Legal",
                standard = "Legal"
            ),
            releaseDate = "1999-01-09",
            images = SetImages(symbol = "url_to_symbol", logo = "url_to_logo"),
            ownedCards = 100
        )

        val abilities = CardAbilities(name = "Ability 1", text = "Ability text 1", type = "Ability type 1")
        val images = CardImages("small_image_url", "large_image_url")
        val legalities = CardLegalities("Legal", "Legal", "Legal")
        val resistances = listOf(CardResistance("type", "value"))
        val weaknesses = listOf(CardWeakness("type", "value"))
        val attacks = listOf(CardAttack(convertedEnergyCost = "3", cost = listOf("Energy"), damage = "50", name = "Attack 1", text = "Attack text"))

        val card1 = Card(
            id = "1",
            abilities = abilities,
            artist = "Artist 1",
            attacks = attacks,
            evolvesFrom = "None",
            evolvesTo = emptyList(),
            flavorText = "Flavor text 1",
            images = images,
            hp = "100",
            legalities = legalities,
            name = "Card 1",
            number = "001",
            rarity = "Common",
            resistances = resistances,
            retreatCost = emptyList(),
            rules = emptyList(),
            set = set,
            supertype = "Pokémon",
            subtypes = listOf("Basic"),
            types = listOf("Fire"),
            weaknesses = weaknesses,
            owned = true
        )

        val card2 = Card(
            id = "2",
            abilities = abilities,
            artist = "Artist 2",
            attacks = attacks,
            evolvesFrom = "Card 1",
            evolvesTo = emptyList(),
            flavorText = "Flavor text 2",
            images = images,
            hp = "200",
            legalities = legalities,
            name = "Card 2",
            number = "002",
            rarity = "Rare",
            resistances = resistances,
            retreatCost = emptyList(),
            rules = emptyList(),
            set = set,
            supertype = "Pokémon",
            subtypes = listOf("Stage 1"),
            types = listOf("Water"),
            weaknesses = weaknesses,
            owned = true
        )

        val allCards = listOf(card1, card2)
        coEvery { localDataSource.getCallCards() } returns flowOf(allCards)

        // When
        val result = cardRepository.getAllCards().first()

        // Then
        assertEquals(allCards, result)
    }


    /**
     * Test for [CardRepositoryImp.areLocalResourcesLoaded].
     *
     * Verifies that the correct status of local resources being loaded is returned.
     */
    @Test
    fun `when checking if local resources are loaded, then it should return the correct status`() {
        // Given
        val expectedStatus = true
        every { localDataSource.areLocalResourcesLoaded() } returns expectedStatus

        // When
        val result = cardRepository.areLocalResourcesLoaded()

        // Then
        assertEquals(expectedStatus, result)
    }

    /**
     * Test for [CardRepositoryImp.getObtainedAchievements].
     *
     * Verifies that the correct list of obtained achievements is returned.
     */
    @Test
    fun `when getting obtained achievements, then it should return the list of achievements`() {
        // Given
        val expectedAchievements = listOf("Achievement1", "Achievement2", "Achievement3")
        every { localDataSource.getUserAchievements() } returns expectedAchievements

        // When
        val result = cardRepository.getObtainedAchievements()

        // Then
        assertEquals(expectedAchievements, result)
    }



    /**
     * Test for [CardRepositoryImp.addObtainedAchievement].
     *
     * Verifies that the new achievement is added correctly.
     */
    @Test
    fun `when adding a new obtained achievement, then it should add the achievement`() {
        // Given
        val newAchievement = "New Achievement"
        val initialAchievements = mutableListOf<String>()

        // Mock localDataSource to manipulate and check achievements
        every { localDataSource.addObtainedAchievement(any()) } answers {
            initialAchievements.add(newAchievement)
        }

        // When
        cardRepository.addObtainedAchievement(newAchievement)

        // Then
        assertEquals(listOf(newAchievement), initialAchievements)
    }


    private val _card = Card.getEmptyCard()
    private val _set = Set.getEmptySet()
}