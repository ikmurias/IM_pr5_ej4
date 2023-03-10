/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import android.provider.ContactsContract.Data
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.*

// TODO: Screen enum
enum class ScreensApp {
    Start,
    Side,
    Entree,
    Checkout,
    Basem,
    Accompaniment
}
// TODO: AppBar

@Composable
fun LunchTrayApp(modifier: Modifier = Modifier) {
    // TODO: Create Controller and initialization
    val navController = rememberNavController()
    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            // TODO: AppBar
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        // TODO: Navigation host
        val navHost = NavHost(
            navController = navController,
            startDestination = ScreensApp.Start.name,
            modifier = modifier.padding(innerPadding)
        ) {

            composable(route = ScreensApp.Start.name) {
                StartOrderScreen(onStartOrderButtonClicked = {navController.navigate(ScreensApp.Entree.name)},)
            }

            composable(route = ScreensApp.Entree.name){
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = { resetAllProgress(navController, viewModel) },
                    onNextButtonClicked = { navController.navigate(ScreensApp.Side.name) },
                    onSelectionChanged = { viewModel.updateEntree(it)}
                )
            }

            composable(route = ScreensApp.Side.name){
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { resetAllProgress(navController, viewModel) },
                    onNextButtonClicked = { navController.navigate(ScreensApp.Accompaniment.name) },
                    onSelectionChanged = {viewModel.updateSideDish(it)}
                )
            }

            composable(route = ScreensApp.Accompaniment.name){
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = { resetAllProgress(navController, viewModel)  },
                    onNextButtonClicked = { navController.navigate(ScreensApp.Checkout.name) },
                    onSelectionChanged = {viewModel.updateAccompaniment(it)}
                )
            }

            composable(route = ScreensApp.Checkout.name){
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = { resetAllProgress(navController, viewModel) },
                    onCancelButtonClicked = { /*TODO*/ })
            }
        }
    }
}

fun resetAllProgress(navController: NavController,viewModel: OrderViewModel){
    viewModel.resetOrder()
    navController.popBackStack(ScreensApp.Start.name, inclusive = false)
}
