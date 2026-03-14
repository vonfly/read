package com.vonfly.read.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object BookListRoute
@Serializable object StoreRoute
@Serializable object ProfileRoute
@Serializable data class BookDetailRoute(val bookId: String)
