package dev.ayupi.pimodoro

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import dev.ayupi.designsystem.theme.PiTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ayupi.pimodoro.MainActivityUiState.Loading
import dev.ayupi.pimodoro.MainActivityUiState.Success
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.service.Actions
import dev.ayupi.pimodoro.core.service.TimerService
import dev.ayupi.pimodoro.ui.PimodoroApp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        requestPermissions()
        var uiState: MainActivityUiState by mutableStateOf(Loading)

        // hier sollte der service gestartet werden nach dem inject mit dem timerservice
        
        lifecycleScope.launch { 
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { 
                        uiState = it
                    }
                    .collect()
            }
        }
        
        // SplashScreen until data received for the UiState
        splashScreen.setKeepOnScreenCondition {
            when(uiState) {
                Loading -> true
                is Success -> false
            }
        }
        
        enableEdgeToEdge()
        
        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)
            PiTheme(
                darkTheme = darkTheme,
                disableDynamicTheming = shouldDisableDynamicTheming(uiState)
            ){
                PimodoroApp()
            }
        }
    }

    private fun startTimerService() {
        val intent = Intent(this, TimerService::class.java).also {
            it.action = Actions.START.toString()
        }
        startForegroundService(intent);
    }

    private fun stopTimerService() {
        val intent = Intent(this, TimerService::class.java).also {
            it.action = Actions.STOP.toString()
        }
        startForegroundService(intent)
    }

    override fun onStart() {
        super.onStart()
        startTimerService()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimerService()
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if(!hasPermissionForegroundService()) permissionsToRequest.add(android.Manifest.permission.FOREGROUND_SERVICE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(!hasPermissionNotifications()) permissionsToRequest.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        if(permissionsToRequest.isNotEmpty()) ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(),0)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasPermissionNotifications() = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)== PackageManager.PERMISSION_GRANTED
    private fun hasPermissionForegroundService() =ActivityCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED
}


@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.SYSTEM_DEFAULT -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> false
    is Success -> !uiState.userData.useDynamicColor
}

