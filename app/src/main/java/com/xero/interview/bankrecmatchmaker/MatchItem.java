package com.xero.interview.bankrecmatchmaker;

import androidx.annotation.NonNull;

import java.util.Objects;

public record MatchItem(String paidTo, String transactionDate, Float total, String docType) {

    public MatchItem(@NonNull String paidTo, @NonNull String transactionDate, @NonNull Float total, @NonNull String docType) {
        this.paidTo = Objects.requireNonNull(paidTo, "paidTo must not be null");
        this.transactionDate = Objects.requireNonNull(transactionDate, "transactionDate must not be null");
        this.total = Objects.requireNonNull(total, "total must not be null");
        this.docType = Objects.requireNonNull(docType, "docType must not be null");
    }

    @Override
    @NonNull
    public String paidTo() {
        return paidTo;
    }

    @Override
    @NonNull
    public String transactionDate() {
        return transactionDate;
    }

    @Override
    @NonNull
    public Float total() {
        return total;
    }

    @Override
    @NonNull
    public String docType() {
        return docType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchItem matchItem = (MatchItem) o;
        return Float.compare(matchItem.total, total) == 0 &&
                paidTo.equals(matchItem.paidTo) &&
                transactionDate.equals(matchItem.transactionDate) &&
                docType.equals(matchItem.docType);
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchItem{" +
                "paidTo='" + paidTo + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", total=" + total +
                ", docType='" + docType + '\'' +
                '}';
    }
}