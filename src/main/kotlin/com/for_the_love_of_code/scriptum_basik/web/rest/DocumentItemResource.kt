package com.for_the_love_of_code.scriptum_basik.web.rest

import com.for_the_love_of_code.scriptum_basik.domain.DocumentItem
import com.for_the_love_of_code.scriptum_basik.service.DocumentItemService
import com.for_the_love_of_code.scriptum_basik.web.rest.errors.BadRequestAlertException
import com.for_the_love_of_code.scriptum_basik.service.dto.DocumentItemCriteria
import com.for_the_love_of_code.scriptum_basik.service.DocumentItemQueryService

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid
import java.net.URI
import java.net.URISyntaxException

private const val ENTITY_NAME = "documentItem"
/**
 * REST controller for managing [com.for_the_love_of_code.scriptum_basik.domain.DocumentItem].
 */
@RestController
@RequestMapping("/api")
class DocumentItemResource(
    private val documentItemService: DocumentItemService,
    private val documentItemQueryService: DocumentItemQueryService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /document-items` : Create a new documentItem.
     *
     * @param documentItem the documentItem to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new documentItem, or with status `400 (Bad Request)` if the documentItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-items")
    fun createDocumentItem(@Valid @RequestBody documentItem: DocumentItem): ResponseEntity<DocumentItem> {
        log.debug("REST request to save DocumentItem : {}", documentItem)
        if (documentItem.id != null) {
            throw BadRequestAlertException(
                "A new documentItem cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = documentItemService.save(documentItem)
        return ResponseEntity.created(URI("/api/document-items/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /document-items` : Updates an existing documentItem.
     *
     * @param documentItem the documentItem to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated documentItem,
     * or with status `400 (Bad Request)` if the documentItem is not valid,
     * or with status `500 (Internal Server Error)` if the documentItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-items")
    fun updateDocumentItem(@Valid @RequestBody documentItem: DocumentItem): ResponseEntity<DocumentItem> {
        log.debug("REST request to update DocumentItem : {}", documentItem)
        if (documentItem.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = documentItemService.save(documentItem)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     documentItem.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /document-items` : get all the documentItems.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of documentItems in body.
     */
    @GetMapping("/document-items")        fun getAllDocumentItems(
        criteria: DocumentItemCriteria,
        pageable: Pageable
        
    ): ResponseEntity<MutableList<DocumentItem>> {
        log.debug("REST request to get DocumentItems by criteria: {}", criteria)
        val page = documentItemQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /document-items/count}` : count all the documentItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/document-items/count")
    fun countDocumentItems(criteria: DocumentItemCriteria): ResponseEntity<Long> {
        log.debug("REST request to count DocumentItems by criteria: {}", criteria)
        return ResponseEntity.ok().body(documentItemQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /document-items/:id` : get the "id" documentItem.
     *
     * @param id the id of the documentItem to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the documentItem, or with status `404 (Not Found)`.
     */
    @GetMapping("/document-items/{id}")
    fun getDocumentItem(@PathVariable id: Long): ResponseEntity<DocumentItem> {
        log.debug("REST request to get DocumentItem : {}", id)
        val documentItem = documentItemService.findOne(id)
        return ResponseUtil.wrapOrNotFound(documentItem)
    }
    /**
     *  `DELETE  /document-items/:id` : delete the "id" documentItem.
     *
     * @param id the id of the documentItem to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/document-items/{id}")
    fun deleteDocumentItem(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete DocumentItem : {}", id)
        documentItemService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
