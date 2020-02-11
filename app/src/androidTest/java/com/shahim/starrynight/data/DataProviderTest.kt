package com.shahim.starrynight.data

import android.widget.Toast
import androidx.test.platform.app.InstrumentationRegistry
import io.reactivex.Emitter
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Test

import org.junit.Assert.*

class DataProviderTest {

    @Test
    fun getImageObservable_shouldParseJson() {
        DataProvider.getImageObservable(InstrumentationRegistry.getInstrumentation().targetContext)
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