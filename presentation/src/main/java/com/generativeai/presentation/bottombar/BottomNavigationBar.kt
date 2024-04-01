package com.generativeai.presentation.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.generativeai.presentation.screen.NavGraphs
import com.generativeai.presentation.screen.appCurrentDestinationAsState
import com.generativeai.presentation.screen.destinations.Destination
import com.generativeai.presentation.screen.startAppDestination


@Composable
fun BottomNavigationBar(
    navController: NavController? = null
) {

    val currentDestination: Destination = navController?.appCurrentDestinationAsState()?.value
        ?: NavGraphs.root.startAppDestination


    NavigationBar {
        BottomBarDestination.entries.forEach { destination ->
            NavigationBarItem(
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIconColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                selected = currentDestination == destination.direction,
                onClick = {
                    navController?.navigate(destination.direction.route) {
                        launchSingleTop
                    }
                },
                icon = {
                    Icon(
                        painterResource(id = destination.icon),
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(destination.label)
                    )
                },
            )
        }
    }
}
