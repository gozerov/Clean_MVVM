package com.example.clean_mvvm.core.side_effect.toast

import androidx.annotation.StringRes
import com.example.clean_mvvm.core.side_effect.SideEffect

abstract class Toast(@StringRes val message: Int): SideEffect