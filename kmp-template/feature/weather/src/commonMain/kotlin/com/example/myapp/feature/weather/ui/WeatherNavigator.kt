package com.example.myapp.feature.weather.ui

import com.example.myapp.core.ui.navigation.Navigator

sealed interface WeatherNavEvent

interface WeatherNavigator : Navigator<WeatherNavEvent>
