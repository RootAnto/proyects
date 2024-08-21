package com.example.pokectcollection.ui.carddetail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import com.example.pokectcollection.domain.cards.model.CardAttack
import com.example.pokectcollection.ui.carddetail.CardDetailViewModel
import com.example.pokectcollection.utils.getLegalityColor

/**
 * The screen for displaying card details.
 *
 * @param onClickBack the callback for navigating back to the previous screen.
 * @param viewModel the [CardDetailViewModel] instance.
 */
@Composable
fun CardDetailScreen(
    onClickBack: () -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.captura_de_pantalla_2024_04_30_134600),
                contentDescription = stringResource(R.string.background_image),
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.flecha_hacia_atras),
                            contentDescription = stringResource(
                                R.string.flecha_para_retroceder_volver_a_la_lista_de_cartas
                            ),
                            modifier = Modifier
                                .size(35.dp)
                                .clickable { onClickBack() }
                        )
                        // Texto centrado
                        Text(
                            text = uiState.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )

                        // Espacio a la derecha para equilibrar la flecha a la izquierda
                        Spacer(modifier = Modifier.size(35.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AsyncImage(
                    model = uiState.images.large,
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            width = (maxWidth * 0.8).dp.coerceAtMost(350.dp),
                            height = (maxHeight * 0.4).dp.coerceAtMost(350.dp)
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.abilities.name.isNotEmpty()) {
                    Titles(title = stringResource(R.string.habilidades))
                    CustomDivider()

                    AbilityDetail(
                        name = uiState.abilities.name,
                        description = uiState.abilities.text
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.attacks.isNotEmpty()) {
                    Titles(title = stringResource(R.string.ataques))
                    CustomDivider()

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (attack in uiState.attacks) {
                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                            AttackDetail(attack)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.rules.isNotEmpty() || uiState.retreatCost.isNotEmpty() ||
                    uiState.resistances.isNotEmpty() || uiState.weaknesses.isNotEmpty()
                ) {
                    Titles(title = stringResource(R.string.normas))
                    CustomDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    if (uiState.rules.isNotEmpty()) {
                        for (rules in uiState.rules) {
                            RuleDetail(rules)
                            Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }

                    if (uiState.weaknesses.isNotEmpty()) {
                        for (weakness in uiState.weaknesses) {
                            if (weakness.type.isNotEmpty() && weakness.value.isNotEmpty()) {
                                StatDetail(
                                    name = stringResource(R.string.weakness),
                                    value = weakness.value,
                                    energia = weakness.type
                                )
                            } else {
                                Text(stringResource(R.string.no_weakness_details_available))
                            }
                        }
                    }

                    if (uiState.resistances.isNotEmpty()) {
                        for (resistance in uiState.resistances) {
                            StatDetail(
                                name = stringResource(R.string.resistencia),
                                value = resistance.value,
                                energia = resistance.type
                            )
                        }
                    }

                    if (uiState.retreatCost.isNotEmpty()) {
                        Retreat(uiState.retreatCost)
                        Spacer(modifier = Modifier.padding(vertical = 1.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Titles(title = stringResource(R.string.legalidad))
                CustomDivider()

                LegalitiesComponent(
                    standard = uiState.legalities.standard,
                    expanded = uiState.legalities.expanded,
                    unlimited = uiState.legalities.unlimited
                )

                Spacer(modifier = Modifier.height(16.dp))
                Titles(title = stringResource(R.string.informaci_n_adicional))
                CustomDivider()

                ExtraInfo(titulo = stringResource(R.string.artist), info = uiState.artist)
                ExtraInfo(titulo = stringResource(R.string.set_id), info = uiState.set.id)
                ExtraInfo(titulo = stringResource(R.string.set_serie), info = uiState.set.series)
                ExtraInfo(titulo = stringResource(R.string.set), info = uiState.set.name)
                ExtraInfo(titulo = "Rareza:", info = uiState.rarity)
                ExtraInfo(titulo = stringResource(R.string.number), info = uiState.number)
                ExtraInfo(
                    titulo = stringResource(R.string.regulation_mark),
                    info = uiState.legalities.standard
                )
            }
        }
    }
}

/**
 * Custom divider component.
 */
@Composable
fun CustomDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = Color.Black
    )
}

/**
 * Composable for displaying ability details.
 *
 * @param name the name of the ability.
 * @param description the description of the ability.
 */
@Composable
fun AbilityDetail(name: String, description: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(70.dp),
                painter = painterResource(R.drawable.ability),
                contentDescription = stringResource(R.string.icono_que_indica_las_habilidades)
            )
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Blue
            )
        }
        Text(
            text = description,
            fontSize = 14.sp
        )
    }
}

/**
 * Composable for displaying attack details.
 *
 * @param attack the [CardAttack] data.
 */
@Composable
fun AttackDetail(attack: CardAttack) {
    val energyTypeToImage = mapOf(
        "Colorless" to R.drawable.energetynormal,
        "Darkness" to R.drawable.energy_darkness,
        "Dragon" to R.drawable.energy_dragon,
        "Fairy" to R.drawable.energy_fairy,
        "Fighting" to R.drawable.energy_fighting,
        "Fire" to R.drawable.energy_fire,
        "Grass" to R.drawable.energy_grass,
        "Lightning" to R.drawable.energetylight,
        "Metal" to R.drawable.energy_metal,
        "Psychic" to R.drawable.energy_psychic,
        "Water" to R.drawable.energywater
    )

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            attack.cost.forEach { energyType ->
                energyTypeToImage[energyType]?.let { energyImage ->
                    Image(
                        painter = painterResource(id = energyImage),
                        contentDescription = "Energía $energyType",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                }
            }

            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(
                text = attack.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Text(
                text = attack.damage,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = attack.text,
            fontSize = 14.sp
        )
    }
}

/**
 * Composable for displaying rule details.
 *
 * @param description the rule description.
 */
@Composable
fun RuleDetail(description: String) {
    Text(
        text = description,
        fontSize = 14.sp,
        color = Color.Black
    )
}

/**
 * Composable for displaying stat details such as weaknesses or resistances.
 *
 * @param name the name of the stat.
 * @param value the value of the stat.
 * @param energia the energy type.
 */
@Composable
fun StatDetail(name: String, value: String, energia: String) {
    val energyTypeToImage = mapOf(
        "Colorless" to R.drawable.energetynormal,
        "Darkness" to R.drawable.energy_darkness,
        "Dragon" to R.drawable.energy_dragon,
        "Fairy" to R.drawable.energy_fairy,
        "Fighting" to R.drawable.energy_fighting,
        "Fire" to R.drawable.energy_fire,
        "Grass" to R.drawable.energy_grass,
        "Lightning" to R.drawable.energetylight,
        "Metal" to R.drawable.energy_metal,
        "Psychic" to R.drawable.energy_psychic,
        "Water" to R.drawable.energywater
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(8.dp))

        energyTypeToImage[energia]?.let { energyImage ->
            Image(
                painter = painterResource(id = energyImage),
                contentDescription = "Energía $energia",
                modifier = Modifier.size(20.dp)
            )
        } ?: Text(
            text = "N/A",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

/**
 * Composable for displaying retreat cost details.
 *
 * @param energies the list of energy types required for retreat.
 */
@Composable
fun Retreat(energies: List<String>) {
    val energyTypeToImage = mapOf(
        "Colorless" to R.drawable.energetynormal,
        "Darkness" to R.drawable.energy_darkness,
        "Dragon" to R.drawable.energy_dragon,
        "Fairy" to R.drawable.energy_fairy,
        "Fighting" to R.drawable.energy_fighting,
        "Fire" to R.drawable.energy_fire,
        "Grass" to R.drawable.energy_grass,
        "Lightning" to R.drawable.energetylight,
        "Metal" to R.drawable.energy_metal,
        "Psychic" to R.drawable.energy_psychic,
        "Water" to R.drawable.energywater
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(R.string.retirada),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(8.dp))

        energies.forEach { energia ->
            energyTypeToImage[energia]?.let { energyImage ->
                Image(
                    painter = painterResource(id = energyImage),
                    contentDescription = "Energía $energia",
                    modifier = Modifier.size(20.dp)
                )
            } ?: Text(
                text = "N/A",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

/**
 * Composable for displaying additional information.
 *
 * @param titulo the title of the information.
 * @param info the information content.
 */
@Composable
fun ExtraInfo(titulo: String, info: String) {
    Row {
        Text(
            text = titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        Text(
            text = info,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

/**
 * Composable for displaying rarity information.
 *
 * @param rarity the rarity description.
 * @param logo the resource id of the rarity logo.
 */
@Composable
fun RarityInfo(rarity: String, logo: Int) {
    Row(
        modifier = Modifier
            .padding(horizontal = 50.dp)
    ) {
        Text(
            text = rarity,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Image(
            painter = painterResource(id = logo),
            contentDescription = stringResource(R.string.icono_de_la_rareza)
        )
    }
}

/**
 * Composable for displaying section titles.
 *
 * @param title the title text.
 */
@Composable
fun Titles(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Black,
        fontSize = 18.sp
    )
}

/**
 * Composable for displaying a back button.
 *
 * @param onClick the callback to invoke when the button is clicked.
 * @param drawableId the resource id of the drawable to use for the button.
 */
@Composable
fun BackButton(onClick: () -> Unit, drawableId: Int) {
    Image(
        painter = painterResource(id = drawableId),
        contentDescription = stringResource(R.string.button_image),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
    )
}

/**
 * Composable for displaying the legalities of the card.
 *
 * @param standard the standard legality status.
 * @param expanded the expanded legality status.
 * @param unlimited the unlimited legality status.
 */
@Composable
fun LegalitiesComponent(standard: String, expanded: String, unlimited: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegalStatusBox(type = stringResource(R.string.standard), status = standard)
            LegalStatusBox(type = stringResource(R.string.expanded), status = expanded)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegalStatusBox(type = stringResource(R.string.unlimited), status = unlimited)
        }
    }
}

/**
 * Composable for displaying a legal status box.
 *
 * @param type the type of legality (e.g., Standard, Expanded, Unlimited).
 * @param status the legality status (e.g., Legal, Illegal).
 */
@Composable
fun LegalStatusBox(type: String, status: String) {
    val backgroundColor = status.getLegalityColor()
    val displayStatus = if (status.equals("legal", ignoreCase = true)) "Legal" else "Ilegal"
    val textColor = if (status.equals("legal", ignoreCase = true)) Color.White else Color.White

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = Color.Black, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .height(40.dp)
    ) {
        Text(
            text = type,
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 4.dp)
        )
        Box(
            modifier = Modifier
                .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = displayStatus,
                color = textColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
