package com.vonfly.read.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object BookListRoute
@Serializable object StoreRoute
@Serializable data class BookDetailRoute(val bookId: String)
@Serializable data class ReaderRoute(val bookId: String)
