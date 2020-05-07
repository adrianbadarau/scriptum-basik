package com.for_the_love_of_code.scriptum_basik.service.google

import com.for_the_love_of_code.scriptum_basik.config.ApplicationProperties
import com.google.api.services.docs.v1.Docs
import com.google.api.services.docs.v1.model.ParagraphElement
import com.google.api.services.docs.v1.model.StructuralElement
import org.springframework.stereotype.Service

@Service
class DocsService(applicationProperties: ApplicationProperties) : GoogleApiService(applicationProperties) {

    private val docs: Docs by lazy {
        Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials()).setApplicationName("Scriptum-Basik").build()
    }

    private fun readParagraphElement(element: ParagraphElement): String {
        return element?.textRun?.content ?: ""
    }

    private fun readStructuralElements(elements: List<StructuralElement>): String {
        val builder = StringBuilder()
        elements.forEach { structuralElement ->
            structuralElement?.paragraph?.elements?.forEach {
                builder.append(readParagraphElement(it))
            }
            structuralElement?.table?.tableRows?.forEach { tableRow ->
                tableRow.tableCells.forEach {
                    builder.append(it.content)
                }
            }
            structuralElement?.tableOfContents?.let {
                builder.append(readStructuralElements(it.content))
            }
        }
        return builder.toString()
    }

    fun readDocumentById(docId: String): String {
        val document = docs.documents().get(docId).execute()
        return readStructuralElements(document.body.content)
    }
}
