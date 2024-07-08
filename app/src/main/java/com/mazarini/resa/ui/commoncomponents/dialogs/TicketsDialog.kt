package com.mazarini.resa.ui.commoncomponents.dialogs

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme


@Composable
fun TicketsDialog(
    onDismiss: () -> Unit,
) {
    val isAppInstalled = isVasttrafikInstalled(LocalContext.current)

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = 2.dp.asCardElevation(),
            modifier = Modifier
                .padding(horizontal = 4.dp),
            colors = MTheme.colors.surface.asCardBackground(),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
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
                    modifier = Modifier.padding(vertical = 24.dp),
                    appInstalled = isAppInstalled,
                )
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.disclaimer),
                    style = MTheme.type.secondaryText,
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.disclaimer_desc),
                    style = MTheme.type.secondaryText.copy(
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Justify
                    ),
                )
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.End)
                        .clickable { onDismiss() },
                    text = stringResource(R.string.ok),
                    style = MTheme.type.secondaryText.copy(fontSize = 16.sp),
                )
            }
        }
    }
}

@Composable
fun AppActionButton(
    modifier: Modifier = Modifier,
    appInstalled: Boolean,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { executeAction(appInstalled, context) },
    ) {
        Image(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(id = R.drawable.ic_togo_app),
            contentDescription = null,
        )
        if (appInstalled) {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_exit),
                contentDescription = null,
                tint = MTheme.colors.graph.normal,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.open_app),
                style = MTheme.type.secondaryText,
            )
        } else {
            Image(
                modifier = Modifier
                    .padding(start = 8.dp),
                painter = painterResource(id = R.drawable.get_on_playstore),
                contentDescription = null,
            )
        }
    }
}

fun executeAction(
    appInstalled: Boolean,
    context: Context,
) {
    if (appInstalled) {
        try {
            context.packageManager.getLaunchIntentForPackage(VASTTRAFIK_PACKAGE_NAME)?.let {
                startActivity(context, it, null)
            }
        } catch (e: ActivityNotFoundException) {
            loge("Unable to open Västtrafik App")
        }
    } else {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$VASTTRAFIK_PACKAGE_NAME"))
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$VASTTRAFIK_PACKAGE_NAME"))
            )
        }
    }
}

fun isVasttrafikInstalled(context: Context): Boolean {
    return try {
        context.packageManager.getPackageInfo(VASTTRAFIK_PACKAGE_NAME, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

private const val VASTTRAFIK_PACKAGE_NAME = "com.vaesttrafik.vaesttrafik"

@Composable
@Preview
fun TicketsDialogPreview() {
    ResaTheme {
        TicketsDialog(
            onDismiss = { /*TODO*/ }
        )
    }
}
