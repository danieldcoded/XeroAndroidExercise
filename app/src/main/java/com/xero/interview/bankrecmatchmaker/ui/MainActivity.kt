package com.xero.interview.bankrecmatchmaker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

import com.xero.interview.bankrecmatchmaker.ui.screens.findmatch.FindMatchScreen
import com.xero.interview.bankrecmatchmaker.ui.theme.BankRecMatchmakerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankRecMatchmakerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    FindMatchScreen(onNavigateUp = { finish() })
                }
            }
        }
    }
}