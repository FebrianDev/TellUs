package ui.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ui.components.BtnRounded
import ui.components.TextBodyMedium
import ui.components.TextSubtitleMedium
import ui.themes.colorPrimary

class VerificationScreen : Screen {

    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f).wrapContentHeight()
                    .border(width = 1.dp, colorPrimary, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                TextSubtitleMedium("Verification has been sent!", TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                TextBodyMedium(
                    "Verification information has been sent to email f@gmail.com",
                    TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                BtnRounded("Return to the Homepage") {

                }
            }
        }
    }

    @Composable
    fun VerificationSuccess(){
        Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f).wrapContentHeight()
                    .border(width = 1.dp, colorPrimary, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                TextSubtitleMedium("Verification success!", TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                TextBodyMedium(
                    "Congratulations, your verification was successful. Please log in to the app and tell your story without fear of your identity being discovered.",
                    TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                BtnRounded("Log in to the app") {

                }
            }
        }
    }

    @Composable
    fun VerificationProcess(){
        Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f).wrapContentHeight()
                    .border(width = 1.dp, colorPrimary, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                TextSubtitleMedium("Verification process!", TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                TextBodyMedium(
                    "Your account has not been activated, please check your email to activate your account.",
                    TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                BtnRounded("Log in to the app") {

                }
            }
        }
    }

}
