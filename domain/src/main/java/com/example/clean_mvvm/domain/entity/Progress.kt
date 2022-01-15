package foundation.model

sealed class Progress

object EmptyProgress : Progress()

data class PercentageProgress(
    val percentage: Int
) : Progress() {

    companion object {
        val START = PercentageProgress(0)
    }

}

fun Progress.isInProgress() : Boolean {
    return this !is EmptyProgress
}

fun Progress.getPercentage() : Int {
    return (this as? PercentageProgress)?.percentage ?: PercentageProgress.START.percentage
}
