package com.cvopa.peter.play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cvopa.peter.play.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  /*  @Inject
    lateinit var detailVMFactory: DetailVM.Factory
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()

        /*    CompositionLocalProvider(
                localDetailVMFactory provides detailVMFactory,
            ) {
                ContactsApp()
            }*/
        }
    }
}

/*
val localDetailVMFactory = staticCompositionLocalOf<DetailVM.Factory> {
    error("No ViewModelFactory provided")
}*/
