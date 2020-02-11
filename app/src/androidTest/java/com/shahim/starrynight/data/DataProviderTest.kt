package com.shahim.starrynight.data

import androidx.test.platform.app.InstrumentationRegistry
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Test

import org.junit.Assert.*

class DataProviderTest {

    @Test
    fun fetchImages_shouldParseJson() {
        DataProvider.fetchImages(InstrumentationRegistry.getInstrumentation().targetContext)
            .subscribeBy(
                onSuccess = {
                    assertTrue(it.size>0)
                },
                onError = {
                   fail()
                }
            )
    }
}