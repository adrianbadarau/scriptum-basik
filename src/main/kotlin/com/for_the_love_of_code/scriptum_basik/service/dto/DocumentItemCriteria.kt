package com.for_the_love_of_code.scriptum_basik.service.dto

import java.io.Serializable
import io.github.jhipster.service.Criteria
import io.github.jhipster.service.filter.BooleanFilter
import io.github.jhipster.service.filter.DoubleFilter
import io.github.jhipster.service.filter.Filter
import io.github.jhipster.service.filter.FloatFilter
import io.github.jhipster.service.filter.IntegerFilter
import io.github.jhipster.service.filter.LongFilter
import io.github.jhipster.service.filter.StringFilter
import io.github.jhipster.service.filter.InstantFilter

/**
 * Criteria class for the [com.for_the_love_of_code.scriptum_basik.domain.DocumentItem] entity. This class is used in
 * [com.for_the_love_of_code.scriptum_basik.web.rest.DocumentItemResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/document-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
data class DocumentItemCriteria(

    var id: LongFilter? = null,

    var name: StringFilter? = null,

    var googleDocsId: StringFilter? = null,

    var documentText: StringFilter? = null,

    var createdAt: InstantFilter? = null,

    var updatedAt: InstantFilter? = null
) : Serializable, Criteria {

    constructor(other: DocumentItemCriteria) :
        this(
            other.id?.copy(),
            other.name?.copy(),
            other.googleDocsId?.copy(),
            other.documentText?.copy(),
            other.createdAt?.copy(),
            other.updatedAt?.copy()
        )

    override fun copy() = DocumentItemCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
