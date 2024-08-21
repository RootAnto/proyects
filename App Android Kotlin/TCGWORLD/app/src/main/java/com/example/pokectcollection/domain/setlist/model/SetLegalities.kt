package com.example.pokectcollection.domain.setlist.model

/**
 * Data class [SetLegalities] representing the legalities of a card [Set].
 *
 * @param unlimited Legal status for the unlimited format.
 * @param standard Legal status for the standard format.
 * @param expanded Legal status for the expanded format.
 */
data class SetLegalities(
    val unlimited: String,
    val standard: String,
    val expanded: String
) {
    companion object {
        /**
         * Provides an empty instance of [SetLegalities].
         *
         * @return An instance of [SetLegalities] with empty values.
         */
        fun getEmptySetLegalities() = SetLegalities(
            unlimited = "",
            standard = "",
            expanded = ""
        )
    }
}
