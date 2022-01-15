package com.example.clean_mvvm.core.side_effect.resource

import androidx.annotation.StringRes
import com.example.clean_mvvm.core.side_effect.resource.Resource

class ResourceImpl(@StringRes resId: Int, formatArgs: Any? = null): Resource(resId, formatArgs) {
}