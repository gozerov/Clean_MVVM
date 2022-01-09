package foundation.side_effect.resource

import androidx.annotation.StringRes
import foundation.side_effect.SideEffect

abstract class Resource(@StringRes val resId: Int, val formatArgs: Any? = null) : SideEffect