import androidx.lifecycle.SavedStateHandle
import com.example.pokectcollection.domain.cards.model.Card
import com.example.pokectcollection.domain.cards.model.CardAbilities
import com.example.pokectcollection.domain.cards.model.CardImages
import com.example.pokectcollection.domain.cards.model.CardLegalities
import com.example.pokectcollection.domain.cards.usecase.GetCardListUseCase
import com.example.pokectcollection.domain.setlist.model.Set
import com.example.pokectcollection.domain.user.usecase.CreateCustomCardListUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateCardOwnershipUseCase
import com.example.pokectcollection.domain.user.usecase.UpdateSetAchievementUseCase
import com.example.pokectcollection.rule.MainCoroutineRule
import com.example.pokectcollection.ui.carddetail.model.toUiState
import com.example.pokectcollection.ui.cardlist.CardListViewModel
import com.example.pokectcollection.ui.cardlist.model.CardListUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CardListViewModelTest {

    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private val getCardListUseCase = mockk<GetCardListUseCase>()
    private val updateCardOwnershipUseCase = mockk<UpdateCardOwnershipUseCase>()
    private val createCustomCardListUseCase = mockk<CreateCustomCardListUseCase>()
    private val updateSetAchievementUseCase = mockk<UpdateSetAchievementUseCase>()

    private fun createViewModel(savedStateHandle: SavedStateHandle) = CardListViewModel(
        getCardListUseCase = getCardListUseCase,
        updateCardOwnershipUseCase = updateCardOwnershipUseCase,
        createCustomCardListUseCase = createCustomCardListUseCase,
        signOutUseCase = mockk(),
        updateSetAchievementUseCase = updateSetAchievementUseCase,
        savedStateHandle = savedStateHandle
    )

    private val _card = Card(
        id = "",
        name = "",
        hp = "",
        artist = "",
        evolvesFrom = "",
        evolvesTo = listOf(),
        number = "",
        rarity = "",
        attacks = listOf(),
        abilities = CardAbilities.getEmptyCardAbilities(),
        images = CardImages.getEmptyCardImages(),
        legalities = CardLegalities.getEmptyCardLegalities(),
        resistances = listOf(),
        weaknesses = listOf(),
        retreatCost = listOf(),
        rules = listOf(),
        set = Set.getEmptySet(),
        supertype = "",
        subtypes = listOf(),
        types = listOf(),
        flavorText = "",
        owned = false
    )


}
