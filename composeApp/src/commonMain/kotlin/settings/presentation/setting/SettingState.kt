package settings.presentation.setting

import settings.domain.model.SettingMenu

data class SettingState(
    val settings:List<SettingMenu> = emptyList()
)
