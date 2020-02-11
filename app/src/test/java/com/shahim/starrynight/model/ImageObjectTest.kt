package com.shahim.starrynight.model

import org.junit.Assert.assertTrue
import org.junit.Test
import org.threeten.bp.LocalDate

class ImageObjectTest {

    @Test
    fun getDate_shouldReturnParsedDate() {
        val image = ImageObject("NASA","2019-01-25","Lorem Ipsum","","image","Stars","")
        assertTrue("Image object did not parse date correctly",
            image.date == LocalDate.of(2019,1,25)
        )
    }
}