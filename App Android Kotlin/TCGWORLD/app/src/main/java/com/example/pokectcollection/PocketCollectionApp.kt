package com.example.pokectcollection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class [PocketCollectionApp] annotated with [HiltAndroidApp] to trigger Hilt's code generation.
 *
 * This class sets up the dependency injection framework for the application using Hilt.
 */
@HiltAndroidApp
class PocketCollectionApp : Application()
