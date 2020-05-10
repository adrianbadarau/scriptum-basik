package com.for_the_love_of_code.scriptum_basik.service

import javax.persistence.criteria.JoinType

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import io.github.jhipster.service.QueryService

import com.for_the_love_of_code.scriptum_basik.domain.DocumentItem
import com.for_the_love_of_code.scriptum_basik.domain.DocumentItem_
import com.for_the_love_of_code.scriptum_basik.repository.DocumentItemRepository
import com.for_the_love_of_code.scriptum_basik.service.dto.DocumentItemCriteria

/**
 * Service for executing complex queries for [DocumentItem] entities in the database.
 * The main input is a [DocumentItemCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [DocumentItem] or a [Page] of [DocumentItem] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class DocumentItemQueryService(
    private val documentItemRepository: DocumentItemRepository
) : QueryService<DocumentItem>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [DocumentItem] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DocumentItemCriteria?): MutableList<DocumentItem> {
        log.debug("find by criteria : {}", criteria)
        val specification = createSpecification(criteria)
        return documentItemRepository.findAll(specification)
    }

    /**
     * Return a [Page] of [DocumentItem] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DocumentItemCriteria?, page: Pageable): Page<DocumentItem> {
        log.debug("find by criteria : {}, page: {}", criteria, page)
        val specification = createSpecification(criteria)
        return documentItemRepository.findAll(specification, page)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: DocumentItemCriteria?): Long {
        log.debug("count by criteria : {}", criteria)
        val specification = createSpecification(criteria)
        return documentItemRepository.count(specification)
    }

    /**
     * Function to convert [DocumentItemCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: DocumentItemCriteria?): Specification<DocumentItem?> {
        var specification: Specification<DocumentItem?> = Specification.where(null)
        if (criteria != null) {
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, DocumentItem_.id))
            }
            if (criteria.name != null) {
                specification = specification.and(buildStringSpecification(criteria.name, DocumentItem_.name))
            }
            if (criteria.googleDocsId != null) {
                specification = specification.and(buildStringSpecification(criteria.googleDocsId, DocumentItem_.googleDocsId))
            }
            if (criteria.documentText != null) {
                specification = specification.and(buildStringSpecification(criteria.documentText, DocumentItem_.documentText))
            }
            if (criteria.createdAt != null) {
                specification = specification.and(buildRangeSpecification(criteria.createdAt, DocumentItem_.createdAt))
            }
            if (criteria.updatedAt != null) {
                specification = specification.and(buildRangeSpecification(criteria.updatedAt, DocumentItem_.updatedAt))
            }
        }
        return specification
    }
}
