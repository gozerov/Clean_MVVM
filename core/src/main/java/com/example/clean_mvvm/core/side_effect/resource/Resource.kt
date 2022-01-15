package com.example.clean_mvvm.core.side_effect.resource

import androidx.annotation.StringRes
import com.example.clean_mvvm.core.side_effect.SideEffect

abstract class Resource(@StringRes val resId: Int, val formatArgs: Any? = null) : SideEffect