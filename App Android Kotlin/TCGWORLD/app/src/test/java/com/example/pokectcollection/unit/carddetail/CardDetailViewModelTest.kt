package com.example.pokectcollection.unit.carddetail

import androidx.lifecycle.SavedStateHandle
import com.example.pokectcollection.domain.cards.model.*
import com.example.pokectcollection.domain.cards.usecase.GetCardUseCase
import com.example.pokectcollection.domain.setlist.model.Set
import com.example.pokectcollection.domain.setlist.model.SetImages
import com.example.pokectcollection.domain.setlist.model.SetLegalities
import com.example.pokectcollection.rule.MainCoroutineRule
import com.example.pokectcollection.ui.carddetail.CardDetailViewModel
import com.example.pokectcollection.ui.carddetail.model.CardUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CardDetailViewModelTest {

    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private val getCardUseCase = mockk<GetCardUseCase>()

    private fun createViewModel(savedStateHandle: SavedStateHandle) = CardDetailViewModel(
        getCardUseCase = getCardUseCase,
        savedStateHandle = savedStateHandle
    )

    private val _card = Card.getEmptyCard()
    private val _set = Set.getEmptySet()
    private val _setLegalities = SetLegalities.getEmptySetLegalities()
    private val _setImages = SetImages.getEmptySetImages()
    private val _cardAbilities = CardAbilities.getEmptyCardAbilities()
    private val _cardAttack = CardAttack.getEmptyCardAttack()
    private val _cardImages = CardImages.getEmptyCardImages()
    private val _cardLegalities = CardLegalities.getEmptyCardLegalities()
    private val _cardResistance = CardResistance.getEmpyCardReistance()
    private val _cardWeakness = CardWeakness.getEmpyCardWeakness()

    // Test para comprobar que el estado inicial es vacío cuando no se proporciona un cardId
    @Test
    fun `when getting state, given no cardId, then state must be empty`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        val expected = CardUiState.getEmptyState()
        assertEquals(expected = expected.id, actual = result.id)
        assertEquals(expected = expected.name, actual = result.name)
        assertEquals(expected = expected.hp, actual = result.hp)
        assertEquals(expected = expected.artist, actual = result.artist)
        assertEquals(expected = expected.evolvesFrom, actual = result.evolvesFrom)
        assertEquals(expected = expected.evolvesTo, actual = result.evolvesTo)
        assertEquals(expected = expected.number, actual = result.number)
        assertEquals(expected = expected.rarity, actual = result.rarity)
    }

    // Test para comprobar que el estado se mapea correctamente cuando se proporciona un cardId
    @Test
    fun `when getting state, given cardId, then state have to be properly mapped`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val card = _card.copy(
            name = "Test name",
            id = "Test id",
            hp = "Test hp",
            artist = "Test artist",
            evolvesFrom = "Test evolvesFrom",
            number = "Test number",
            rarity = "Test rarity"
        )
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = "Test id", actual = result.id)
        assertEquals(expected = "Test name", actual = result.name)
        assertEquals(expected = "Test hp", actual = result.hp)
        assertEquals(expected = "Test artist", actual = result.artist)
        assertEquals(expected = "Test evolvesFrom", actual = result.evolvesFrom)
        assertEquals(expected = "Test number", actual = result.number)
        assertEquals(expected = "Test rarity", actual = result.rarity)
    }

    // Test para comprobar que el estado tiene la información correcta del set
    @Test
    fun `when getting state, given cardId, then state have the right set info`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val setLegalities = _setLegalities.copy(
            unlimited = "Test unlimited",
            standard = "Test standard",
            expanded = "Test expanded"
        )
        val setImages = _setImages.copy(symbol = "Test symbol", logo = "Test logo")
        val set = _set.copy(
            id = "Test id",
            name = "Test name",
            series = "Test series",
            total = 120,
            legalities = setLegalities,
            releaseDate = "Test date",
            images = setImages,
            ownedCards = 47
        )
        val card = _card.copy(set = set)
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = set, actual = result.set)
    }

    // Test para comprobar que el estado tiene la información correcta de las habilidades
    @Test
    fun `when getting state, given cardId, then state have the right abilities info`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val abilities = _cardAbilities.copy(name = "Test ability", text = "Test text", type = "Test type")
        val card = _card.copy(abilities = abilities)
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = abilities, actual = result.abilities)
    }

    // Test para comprobar que el estado tiene la información correcta de los ataques
    @Test
    fun `when getting state, given cardId, then state have the right attacks info`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val attacks = listOf(_cardAttack.copy(name = "Test attack", damage = "50"))
        val card = _card.copy(attacks = attacks)
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = attacks, actual = result.attacks)
    }

    // Test para comprobar que el estado tiene la información correcta de las imágenes
    @Test
    fun `when getting state, given cardId, then state have the right images info`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val images = CardImages(large = "large.jpg", small = "small.jpg")
        val card = _card.copy(images = images)
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = images, actual = result.images)
    }

    // Test para comprobar que el estado tiene la información correcta de las legalidades
    @Test
    fun `when getting state, given cardId, then state have the right legalities info`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val legalities = _cardLegalities.copy(expanded = "Legal expanded", standard = "Legal standard", unlimited = "Legal unlimited")
        val card = _card.copy(legalities = legalities)
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = legalities, actual = result.legalities)
    }

    // Test para comprobar que el estado tiene la información correcta de las resistencias
    @Test
    fun `when getting state, given cardId, then state have the right resistances info`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val resistances = listOf(_cardResistance.copy(type = "Fire", value = "-20"))
        val card = _card.copy(resistances = resistances)
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = resistances, actual = result.resistances)
    }

    // Test para comprobar que el estado tiene la información correcta de las debilidades
    @Test
    fun `when getting state, given cardId, then state have the right weaknesses info`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle().apply {
            set("cardId", "Test id")
        }
        val weaknesses = listOf(_cardWeakness.copy(type = "Water", value = "x2"))
        val card = _card.copy(weaknesses = weaknesses)
        every { getCardUseCase(any()) } returns flowOf(card)

        // When
        val viewModel = createViewModel(savedStateHandle)
        advanceUntilIdle()
        val result = viewModel.uiState.value

        // Then
        assertEquals(expected = weaknesses, actual = result.weaknesses)
    }
}
