package com.example.flowtesting.utils

import com.example.flowtesting.Data
import com.example.flowtesting.DataService
import com.example.flowtesting.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
fun createRepository(
    mockWebServerUrl: String,
    useInstantOkHttpDispatcher: Boolean = false
): Repository {
    return Repository(
        dataService = createRetrofitClient(mockWebServerUrl, useInstantOkHttpDispatcher),
        dispatcher = UnconfinedTestDispatcher()
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun createSucceedingStubBackedRepository(): Repository {
    val dataService = mock(DataService::class.java)
    runBlocking {
        doAnswer {
            Thread.sleep(50)
            Data("value1")
        }
            .`when`(dataService).loadData(anyString())
    }
    return Repository(
        dataService = dataService,
        dispatcher = UnconfinedTestDispatcher()
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun createFailingStubBackedRepository(): Repository {
    val dataService = mock(DataService::class.java)
    runBlocking {
        doAnswer {
            Thread.sleep(50)
            @Suppress("TooGenericExceptionThrown")
            throw RuntimeException()
        }
            .`when`(dataService).loadData(anyString())
    }
    return Repository(
        dataService = dataService,
        dispatcher = UnconfinedTestDispatcher()
    )
}
