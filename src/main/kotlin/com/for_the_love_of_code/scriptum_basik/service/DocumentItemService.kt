package com.for_the_love_of_code.scriptum_basik.service
import com.for_the_love_of_code.scriptum_basik.domain.DocumentItem
import com.for_the_love_of_code.scriptum_basik.repository.DocumentItemRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [DocumentItem].
 */
@Service
@Transactional
class DocumentItemService(
    private val documentItemRepository: DocumentItemRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a documentItem.
     *
     * @param documentItem the entity to save.
     * @return the persisted entity.
     */
    fun save(documentItem: DocumentItem): DocumentItem {
        log.debug("Request to save DocumentItem : {}", documentItem)
        return documentItemRepository.save(documentItem)
    }

    /**
     * Get all the documentItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<DocumentItem> {
        log.debug("Request to get all DocumentItems")
        return documentItemRepository.findAll(pageable)
    }

    /**
     * Get one documentItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<DocumentItem> {
        log.debug("Request to get DocumentItem : {}", id)
        return documentItemRepository.findById(id)
    }

    /**
     * Delete the documentItem by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete DocumentItem : {}", id)

        documentItemRepository.deleteById(id)
    }
}
