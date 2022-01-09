package foundation.side_effect.toast

import androidx.annotation.StringRes

class ToastImpl(@StringRes message: Int) : Toast(message)