package com.example.clean_mvvm.core.side_effect.toast

import androidx.annotation.StringRes
import com.example.clean_mvvm.core.side_effect.toast.Toast

class ToastImpl(@StringRes message: Int) : Toast(message)