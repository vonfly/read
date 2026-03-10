package com.vonfly.read.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object BookListRoute
@Serializable data class ReaderRoute(val bookId: String)
