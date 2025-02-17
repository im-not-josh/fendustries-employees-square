package com.fendustries.employees

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Need app class for DI setup
@HiltAndroidApp
class FendustriesEmployeesApplication : Application()