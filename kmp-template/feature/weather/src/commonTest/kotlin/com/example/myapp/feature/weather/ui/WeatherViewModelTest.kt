package com.example.myapp.feature.weather.ui

import com.example.myapp.core.domain.model.Location
import com.example.myapp.core.domain.repository.LocationPreferencesRepository
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import com.example.myapp.feature.weather.domain.usecase.GetCurrentLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.GetSelectedLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.GetWeatherUseCase
import com.example.myapp.feature.weather.domain.usecase.ReverseGeocodeUseCase
import com.example.myapp.feature.weather.domain.usecase.SaveSelectedLocationUseCase
import com.example.myapp.feature.weather.domain.usecase.SearchLocationsUseCase
import com.example.myapp.libs.location.LocationProvider
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val weatherRepository = mock<WeatherRepository>()
    private val locationPreferences = mock<LocationPreferencesRepository>()
    private val locationProvider = mock<LocationProvider>()

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Real use cases over mocked repositories; the test dispatcher keeps the
    // use cases' withContext deterministic under runTest.
    private fun createViewModel() = WeatherViewModel(
        GetWeatherUseCase(weatherRepository, testDispatcher),
        SearchLocationsUseCase(weatherRepository, testDispatcher),
        ReverseGeocodeUseCase(weatherRepository, testDispatcher),
        GetSelectedLocationUseCase(locationPreferences),
        SaveSelectedLocationUseCase(locationPreferences, testDispatcher),
        GetCurrentLocationUseCase(locationProvider, testDispatcher),
    )

    private val weather = WeatherInfo(
        temperature = 12.0, condition = "Clear sky", city = "Paris",
        latitude = 48.85, longitude = 2.35
    )

    @Test
    fun `init fetches weather for the saved location`() = runTest {
        every { locationPreferences.getSelectedLocation() } returns
            flowOf(Location(name = "Paris", latitude = 48.85, longitude = 2.35))
        everySuspend { weatherRepository.getWeather(any(), any(), any()) } returns Result.success(weather)

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(weather, viewModel.uiState.value.weatherInfo)
        verifySuspend { weatherRepository.getWeather(48.85, 2.35, "Paris") }
    }

    @Test
    fun `search debounces and updates results`() = runTest {
        every { locationPreferences.getSelectedLocation() } returns flowOf(null)
        everySuspend { weatherRepository.getWeather(any(), any(), any()) } returns Result.success(weather)
        val results = listOf(Location("Paris", 48.85, 2.35))
        everySuspend { weatherRepository.searchLocations("Par") } returns Result.success(results)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(WeatherUiEvent.OnSearchQueryChange("Par"))
        advanceUntilIdle()

        assertEquals(results, viewModel.uiState.value.searchResults)
        verifySuspend { weatherRepository.searchLocations("Par") }
    }

    @Test
    fun `short query does not trigger a search`() = runTest {
        every { locationPreferences.getSelectedLocation() } returns flowOf(null)
        everySuspend { weatherRepository.getWeather(any(), any(), any()) } returns Result.success(weather)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(WeatherUiEvent.OnSearchQueryChange("P"))
        advanceUntilIdle()

        verifySuspend(VerifyMode.not) { weatherRepository.searchLocations(any()) }
    }
}
