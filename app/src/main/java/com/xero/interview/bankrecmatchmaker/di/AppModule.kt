package com.xero.interview.bankrecmatchmaker.di

import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.accounting_records.repo.AccountingRecordsRepository
import com.xero.interview.bankrecmatchmaker.data.accounting_records.source.MockAccountingRecordsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Currency
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAccountingRecordsRepository(): AccountingRecordsRepository {
        return AccountingRecordsRepository(MockAccountingRecordsDataSource())
    }

    @Provides
    @Singleton
    fun provideCurrencyFormatter(): CurrencyFormatter {
        return CurrencyFormatter(Locale.getDefault(), Currency.getInstance(Locale.getDefault()))
    }
}