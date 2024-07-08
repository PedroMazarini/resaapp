package com.mazarini.resa.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun AboutScreen(
    modifier: Modifier,
    navigateTo: (route: Route) -> Unit = {},
) {
    val techDescription = getDescription()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        CloseButton(onClick = { navigateTo(Route.Back) })
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.about),
                style = MTheme.type.highlightTitle.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 4.dp),
                text = stringResource(id = R.string.resa_app),
                style = MTheme.type.highlightTitle.copy(
                    color = MTheme.colors.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Image(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.about_paragraph_1),
            style = MTheme.type.secondaryText.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Justify,
            ),
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.about_paragraph_2),
            style = MTheme.type.secondaryText.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = techDescription,
            style = MTheme.type.textField.copy(textAlign = TextAlign.Justify),
        )
        Disclaimer()
        Spacer(modifier = Modifier.padding(bottom = 24.dp))
    }
}

@Composable
private fun Disclaimer() {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = stringResource(id = R.string.disclaimer),
        style = MTheme.type.secondaryText.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Justify,
        ),
    )
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = stringResource(id = R.string.disclaimer_desc),
        style = MTheme.type.secondaryText.copy(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Justify,
        ),
    )
}

@Composable
fun getDescription(): AnnotatedString {
    val titleStyle = MTheme.type.secondaryText.toSpanStyle().copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )
    val descStyle = MTheme.type.secondaryText.toSpanStyle().copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
    )
    return AnnotatedString(
        text = stringResource(id = R.string.about_compose),
        spanStyle = titleStyle,
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_compose_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_mvvm),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_mvvm_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_hilt),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_hilt_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_room),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_room_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_lottie),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_lottie_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_retrofit),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_retrofit_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_leak_canary),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_leak_canary_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_datastore),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_datastore_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_paging_compose),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_paging_compose_desc),
            spanStyle = descStyle,
        )
    ).plus(
        AnnotatedString(
            text = LINE + stringResource(id = R.string.about_google_maps),
            spanStyle = titleStyle,
        )
    ).plus(
        AnnotatedString(
            text = ", " + stringResource(id = R.string.about_google_maps_desc),
            spanStyle = descStyle,
        )
    )
}

private const val LINE = "\n"


@Composable
private fun ColumnScope.CloseButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier
            .padding(top = 24.dp)
            .align(Alignment.End)
            .clip(CircleShape)
            .background(MTheme.colors.btnBackground)
            .size(32.dp)
    ) {
        Icon(
            painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = MTheme.colors.textPrimary,
        )
    }
}

@Composable
@Preview
fun AboutScreenDarkPreview() {
    ResaTheme(darkTheme = true) {
        AboutScreen(
            modifier = Modifier.background(MTheme.colors.background),
            navigateTo = { }
        )
    }
}

@Composable
@Preview
fun AboutScreenPreview() {
    ResaTheme {
        AboutScreen(
            modifier = Modifier.background(MTheme.colors.background),
            navigateTo = { }
        )
    }
}