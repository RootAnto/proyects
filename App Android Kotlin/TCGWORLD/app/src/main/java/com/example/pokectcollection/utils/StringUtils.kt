package com.example.pokectcollection.utils

import androidx.compose.ui.graphics.Color
import com.example.pokectcollection.ui.theme.GreenLegal
import com.example.pokectcollection.ui.theme.RedIllegal

/**
 * Extension function [String.getLegalityColor] to get the [Color] of a legality based on [String] value.
 */
fun String.getLegalityColor() = if (this.equals("legal", true)) GreenLegal else RedIllegal
