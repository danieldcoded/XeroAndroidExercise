package com.xero.interview.bankrecmatchmaker.ui.screens.findmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xero.interview.bankrecmatchmaker.R
import com.xero.interview.bankrecmatchmaker.ui.screens.FindMatchViewModel
import com.xero.interview.bankrecmatchmaker.ui.screens.findmatch.components.MatchList
import com.xero.interview.bankrecmatchmaker.ui.theme.BankRecMatchmakerTheme
import kotlinx.coroutines.launch

@Composable
fun FindMatchScreen(
    viewModel: FindMatchViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setInitialTotal(249f + 250f) // Set default initial total
        viewModel.selectMatchingItems()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_find_match)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Navigate Up")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.select_matches, state.formattedTotal),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(16.dp),
                color = MaterialTheme.colors.onPrimary
            )
            MatchList(
                accountingRecords = state.accountingRecords,
                selectedRecords = state.selectedRecords,
                onItemCheckedListener = { record, isChecked ->
                    viewModel.handleItemCheck(record, isChecked)
                },
                canSelectItem = { record ->
                    viewModel.canSelectItem(record)
                }
            )
        }
    }

    // Error handling with Snackbar
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = "Dismiss"
                )
                viewModel.clearError()
            }
        }
    }
}