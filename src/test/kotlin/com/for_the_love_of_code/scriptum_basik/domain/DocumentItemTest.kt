package com.for_the_love_of_code.scriptum_basik.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import com.for_the_love_of_code.scriptum_basik.web.rest.equalsVerifier

class DocumentItemTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(DocumentItem::class)
        val documentItem1 = DocumentItem()
        documentItem1.id = 1L
        val documentItem2 = DocumentItem()
        documentItem2.id = documentItem1.id
        assertThat(documentItem1).isEqualTo(documentItem2)
        documentItem2.id = 2L
        assertThat(documentItem1).isNotEqualTo(documentItem2)
        documentItem1.id = null
        assertThat(documentItem1).isNotEqualTo(documentItem2)
    }
}
