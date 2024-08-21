package com.example.pokectcollection.ui.setlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import com.example.pokectcollection.domain.setlist.model.SetLegalities

/**
 * Displays the details of a card set including images, name, release date, legalities,
 * number of cards, and progress indicators.
 *
 * @param imagePainter URL of the main image to be displayed.
 * @param symbolPainter URL of the symbol image to be displayed.
 * @param setName Name of the card set.
 * @param releaseDate Release date of the card set.
 * @param legalities Object containing the legalities of the card set.
 * @param numCardHave Number of cards the user has from the set.
 * @param numTotalSet Total number of cards in the set.
 * @param onSetClicked Lambda function to handle click on the set.
 */
@Composable
fun CardSetInfo(
    imagePainter: String,
    symbolPainter: String,
    setName: String,
    releaseDate: String,
    legalities: SetLegalities,
    numCardHave: Int,
    numTotalSet: Int,
    onSetClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onSetClicked() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 150.dp) // Desired height for the image)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = symbolPainter,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(8.dp)) // Space between the image and text

                Text(
                    text = setName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = stringResource(R.string.fecha_de_salida, releaseDate),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.tiene, numCardHave, numTotalSet),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Progress bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .background(color = Color.LightGray.copy(alpha = 0.5f))
                ) {
                    LinearProgressIndicator(
                        progress = numCardHave / numTotalSet.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize(),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Text(
                    text = "${(numCardHave / numTotalSet.toFloat() * 100).toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LegalStatusRow(
                    label = stringResource(R.string.expanded),
                    status = legalities.expanded
                )
                LegalStatusRow(
                    label = stringResource(R.string.standard),
                    status = legalities.standard
                )
                LegalStatusRow(
                    label = stringResource(R.string.unlimited),
                    status = legalities.unlimited
                )
            }
        }
    }
}

/**
 * Displays a row showing the legal status of the card set.
 *
 * @param label The label for the legal status.
 * @param status The current legal status.
 */
@Composable
fun LegalStatusRow(label: String, status: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = status.ifEmpty { "ilegal" },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Previews the CardSetInfo composable with sample data.
 */
@Preview(showBackground = true)
@Composable
fun PreviewCardSetInfo() {
    CardSetInfo(
        imagePainter = "https://images.pokemontcg.io/swsh1/logo.png",
        symbolPainter = "https://images.pokemontcg.io/swsh1/symbol.png",
        setName = "Temporal Forces",
        releaseDate = "2015/02/04",
        legalities = SetLegalities.getEmptySetLegalities(),
        numCardHave = 10,
        numTotalSet = 20,
        onSetClicked = {}
    )
}
