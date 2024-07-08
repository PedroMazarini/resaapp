package com.mazarini.resa.ui.commoncomponents.dialogs

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mazarini.resa.R
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.global.extensions.asCardBackground
import com.mazarini.resa.global.extensions.asCardElevation
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.showMessage


@Composable
fun ContactDialog(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

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
                    text = stringResource(id = R.string.contact_title),
                    style = MTheme.type.highlightTitle,
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.contact_message),
                    style = MTheme.type.secondaryText.copy(
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Justify,
                        fontSize = 14.sp,
                    ),
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            sendEmail(context, clipboardManager)
                        }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.ic_exit),
                        contentDescription = null,
                        tint = MTheme.colors.graph.normal,
                    )

                    Text(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .padding(start = 8.dp),
                        text = CONTACT_EMAIL,
                        style = MTheme.type.secondaryText.copy(
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline,
                        ),
                    )
                }
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


private fun sendEmail(
    context: Context,
    clipboardManager: ClipboardManager,
) {

    val version = context.packageManager.getPackageInfo("com.mazarini.resa", 0).versionName.orEmpty()
    clipboardManager.setText(AnnotatedString(CONTACT_EMAIL))

    try {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.setData(Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(CONTACT_EMAIL))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "v$version - Contact Resa")
        context.startActivity(emailIntent)

    } catch (e: ActivityNotFoundException) {
        context.showMessage(context.getString(R.string.contact_fail))
        loge("EMAIL_FAIL", mapOf("CONTACT" to "Unable send email"))
    }
}

private const val CONTACT_EMAIL = "contact.resaapp@gmail.com"

@Composable
@Preview
fun TContactDialogPreview() {
    ResaTheme {
        ContactDialog(
            onDismiss = { /*TODO*/ }
        )
    }
}
