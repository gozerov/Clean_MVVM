package foundation.side_effect.resource

import androidx.annotation.StringRes

class ResourceImpl(@StringRes resId: Int, formatArgs: Any? = null): Resource(resId, formatArgs) {
}