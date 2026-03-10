package com.vonfly.read.ui.screen.booklist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.Primary
import com.vonfly.read.ui.theme.Radius

@Composable
fun DeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "删除",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "取消",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ForegroundSecondary
                )
            }
        },
        title = {
            Text(
                text = "确认删除",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Foreground
            )
        },
        text = {
            Text(
                text = "确定要从书架移除这本书吗？",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = ForegroundSecondary
            )
        },
        shape = RoundedCornerShape(Radius.Md),
        containerColor = Card
    )
}

@Preview(showBackground = true)
@Composable
private fun DeleteDialogPreview() {
    AppTheme {
        DeleteDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
