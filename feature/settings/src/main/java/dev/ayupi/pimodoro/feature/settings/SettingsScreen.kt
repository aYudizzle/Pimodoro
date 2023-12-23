package dev.ayupi.pimodoro.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ayupi.designsystem.icon.PiIcons
import dev.ayupi.designsystem.theme.supportsDynamicTheming
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig.DARK
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig.LIGHT
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig.SYSTEM_DEFAULT
import dev.ayupi.pimodoro.core.model.data.SoundData
import dev.ayupi.pimodoro.feature.settings.R.string
import dev.ayupi.pimodoro.feature.settings.SettingsUiState.Loading
import dev.ayupi.pimodoro.feature.settings.SettingsUiState.Success
import java.time.format.TextStyle

@Composable
fun SettingsRoot(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsDialog(
        settingsUiState = settingsUiState,
        onDismiss = onDismiss,
        onPreferredBreakTimeChanged = viewModel::updatePreferredBreakTime,
        onDynamicColorPreferenceChanged = viewModel::updateDynamicColorPreference,
        onThemeConfigChanged = viewModel::updateDarkThemeConfig,
        onPauseSoundChanged = viewModel::updatePreferredPauseSound,
        onResumeSoundChanged = viewModel::updatePreferredResumeSound
    )
}

@Composable
private fun SettingsDialog(
    settingsUiState: SettingsUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDismiss: () -> Unit,
    onPreferredBreakTimeChanged: (breakTimeConfig: BreakTimeConfig) -> Unit,
    onDynamicColorPreferenceChanged: (useDynamicColor: Boolean) -> Unit,
    onThemeConfigChanged: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onResumeSoundChanged: (soundData: SoundData) -> Unit,
    onPauseSoundChanged: (soundData: SoundData) -> Unit,
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 40.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(string.settings_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            when (settingsUiState) {
                Loading -> {
                    CircularProgressIndicator()
                }

                is Success -> {
                    Divider()
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        SettingsPanel(
                            settings = settingsUiState.settings,
                            supportDynamicColor = supportDynamicColor,
                            onChangeDynamicColorPreference = onDynamicColorPreferenceChanged,
                            onChangeDarkThemeConfig = onThemeConfigChanged,
                            onPreferredBreakTimeChanged = onPreferredBreakTimeChanged,
                            onPauseSoundChanged = onPauseSoundChanged,
                            onResumeSoundChanged = onResumeSoundChanged,
                        )
                        Divider(Modifier.padding(top = 8.dp))
                        LinksPanel()
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = stringResource(string.dismiss_dialog_button_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },

        )
}

@Composable
private fun ColumnScope.SettingsPanel(
    settings: EditableSettings,
    supportDynamicColor: Boolean,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onPreferredBreakTimeChanged: (breakTimeConfig: BreakTimeConfig) -> Unit,
    onPauseSoundChanged: (soundData: SoundData) -> Unit,
    onResumeSoundChanged: (soundData: SoundData) -> Unit,
) {
    SettingsDialogSectionTitle(text = stringResource(string.pause))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(string.time_quarter),
            selected = settings.preferredBreakTime == BreakTimeConfig.QUARTER,
            onClick = { onPreferredBreakTimeChanged(BreakTimeConfig.QUARTER) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.time_half),
            selected = settings.preferredBreakTime == BreakTimeConfig.HALF,
            onClick = { onPreferredBreakTimeChanged(BreakTimeConfig.HALF) },
        )
    }
    SettingsDialogSectionTitle(text = stringResource(string.sound))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SoundDropDownMenu(
            title = stringResource(id = string.sound_pause),
            onClick = { soundData -> onPauseSoundChanged(soundData) },
            selectedItem = settings.preferredPauseSound.label
        )
        SoundDropDownMenu(
            title = stringResource(id = string.sound_resume),
            onClick = { soundData -> onResumeSoundChanged(soundData) },
            selectedItem = settings.preferredResumeSound.label
        )
    }
//    SettingsDialogSectionTitle(text = stringResource(string.theme))
    AnimatedVisibility(visible = supportDynamicColor) {
        Column {
            SettingsDialogSectionTitle(text = stringResource(string.dynamic_color_preference))
            Column(Modifier.selectableGroup()) {
                SettingsDialogThemeChooserRow(
                    text = stringResource(string.dynamic_color_yes),
                    selected = settings.useDynamicColor,
                    onClick = { onChangeDynamicColorPreference(true) },
                )
                SettingsDialogThemeChooserRow(
                    text = stringResource(string.dynamic_color_no),
                    selected = !settings.useDynamicColor,
                    onClick = { onChangeDynamicColorPreference(false) },
                )
            }
        }
    }
    SettingsDialogSectionTitle(text = stringResource(string.dark_mode_preference))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(string.dark_mode_config_system_default),
            selected = settings.darkThemeConfig == SYSTEM_DEFAULT,
            onClick = { onChangeDarkThemeConfig(SYSTEM_DEFAULT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.dark_mode_config_light),
            selected = settings.darkThemeConfig == LIGHT,
            onClick = { onChangeDarkThemeConfig(LIGHT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.dark_mode_config_dark),
            selected = settings.darkThemeConfig == DARK,
            onClick = { onChangeDarkThemeConfig(DARK) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoundDropDownMenu(
    title: String,
    onClick: (soundData: SoundData) -> Unit,
    selectedItem: String
) {
    var expanded by remember { mutableStateOf(false) }

    val shape = if (expanded) RoundedCornerShape(8.dp).copy(
        bottomEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    else RoundedCornerShape(8.dp)
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            value = selectedItem,
            readOnly = true,
            label = { Text(title) },
            onValueChange = {},
            shape = shape,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SoundData.entries.forEach {
                DropdownMenuItem(
                    trailingIcon = {
                        if (it.label == selectedItem) Icon(
                            imageVector = PiIcons.Checkmark,
                            contentDescription = null
                        )
                    },
                    text = { Text(it.label) },
                    onClick = {
                        onClick(it)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LinksPanel() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        val uriHandler = LocalUriHandler.current
        TextButton(
            onClick = { uriHandler.openUri(AYUPIDEV) },
        ) {
            Text(text = stringResource(string.ayupi))
        }
    }
}

const val AYUPIDEV = "https://www.ayupi.dev"
