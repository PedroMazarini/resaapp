package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mazarini.resa.R
import com.mazarini.resa.ui.commoncomponents.dialogs.AppActionButton
import com.mazarini.resa.ui.commoncomponents.dialogs.isVasttrafikInstalled
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun JourneyDisclaimers(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 32.dp),
    ) {
        HorizontalDivider(
            thickness = 2.dp,
            color = MTheme.colors.graph.minimal,
        )
        TicketsDisclaimer(Modifier.padding(top = 24.dp))
        LiveTrackingDisclaimer(modifier = Modifier.padding(top = 24.dp))
        AffiliationDisclaimer(Modifier.padding(top = 24.dp))
    }
}

@Composable
fun TicketsDisclaimer(
    modifier: Modifier,
) {
    val isAppInstalled = isVasttrafikInstalled(LocalContext.current)

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.tickets),
            style = MTheme.type.highlightTitle,
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.find_tickets),
            style = MTheme.type.secondaryText.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Justify
            ),
        )
        AppActionButton(
            modifier = Modifier.padding(top = 8.dp),
            appInstalled = isAppInstalled,
        )
    }
}

@Composable
fun LiveTrackingDisclaimer(
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.live_tracking),
            style = MTheme.type.highlightTitle,
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.disclaimer_live_tracking),
            style = MTheme.type.secondaryText.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Justify
            ),
        )
    }
}

@Composable
fun AffiliationDisclaimer(
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.disclaimer),
            style = MTheme.type.highlightTitle,
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.disclaimer_desc),
            style = MTheme.type.secondaryText.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Justify
            ),
        )
    }
}

@Composable
@Previews
fun JourneyDisclaimersPreview() {
    ResaTheme {
        JourneyDisclaimers()
    }
}
