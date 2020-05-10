package com.for_the_love_of_code.scriptum_basik.web.rest

import com.for_the_love_of_code.scriptum_basik.ScriptumBasikApp
import com.for_the_love_of_code.scriptum_basik.domain.DocumentItem
import com.for_the_love_of_code.scriptum_basik.repository.DocumentItemRepository
import com.for_the_love_of_code.scriptum_basik.service.DocumentItemService
import com.for_the_love_of_code.scriptum_basik.web.rest.errors.ExceptionTranslator
import com.for_the_love_of_code.scriptum_basik.service.dto.DocumentItemCriteria
import com.for_the_love_of_code.scriptum_basik.service.DocumentItemQueryService

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.time.Instant
import java.time.temporal.ChronoUnit

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
 * Integration tests for the [DocumentItemResource] REST controller.
 *
 * @see DocumentItemResource
 */
@SpringBootTest(classes = [ScriptumBasikApp::class])
class DocumentItemResourceIT {

    @Autowired
    private lateinit var documentItemRepository: DocumentItemRepository

    @Autowired
    private lateinit var documentItemService: DocumentItemService

    @Autowired
    private lateinit var documentItemQueryService: DocumentItemQueryService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restDocumentItemMockMvc: MockMvc

    private lateinit var documentItem: DocumentItem

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val documentItemResource = DocumentItemResource(documentItemService, documentItemQueryService)
        this.restDocumentItemMockMvc = MockMvcBuilders.standaloneSetup(documentItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        documentItem = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createDocumentItem() {
        val databaseSizeBeforeCreate = documentItemRepository.findAll().size

        // Create the DocumentItem
        restDocumentItemMockMvc.perform(
            post("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(documentItem))
        ).andExpect(status().isCreated)

        // Validate the DocumentItem in the database
        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeCreate + 1)
        val testDocumentItem = documentItemList[documentItemList.size - 1]
        assertThat(testDocumentItem.name).isEqualTo(DEFAULT_NAME)
        assertThat(testDocumentItem.googleDocsId).isEqualTo(DEFAULT_GOOGLE_DOCS_ID)
        assertThat(testDocumentItem.documentText).isEqualTo(DEFAULT_DOCUMENT_TEXT)
        assertThat(testDocumentItem.createdAt).isEqualTo(DEFAULT_CREATED_AT)
        assertThat(testDocumentItem.updatedAt).isEqualTo(DEFAULT_UPDATED_AT)
    }

    @Test
    @Transactional
    fun createDocumentItemWithExistingId() {
        val databaseSizeBeforeCreate = documentItemRepository.findAll().size

        // Create the DocumentItem with an existing ID
        documentItem.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentItemMockMvc.perform(
            post("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(documentItem))
        ).andExpect(status().isBadRequest)

        // Validate the DocumentItem in the database
        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = documentItemRepository.findAll().size
        // set the field null
        documentItem.name = null

        // Create the DocumentItem, which fails.

        restDocumentItemMockMvc.perform(
            post("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(documentItem))
        ).andExpect(status().isBadRequest)

        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkGoogleDocsIdIsRequired() {
        val databaseSizeBeforeTest = documentItemRepository.findAll().size
        // set the field null
        documentItem.googleDocsId = null

        // Create the DocumentItem, which fails.

        restDocumentItemMockMvc.perform(
            post("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(documentItem))
        ).andExpect(status().isBadRequest)

        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkDocumentTextIsRequired() {
        val databaseSizeBeforeTest = documentItemRepository.findAll().size
        // set the field null
        documentItem.documentText = null

        // Create the DocumentItem, which fails.

        restDocumentItemMockMvc.perform(
            post("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(documentItem))
        ).andExpect(status().isBadRequest)

        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun checkUpdatedAtIsRequired() {
        val databaseSizeBeforeTest = documentItemRepository.findAll().size
        // set the field null
        documentItem.updatedAt = null

        // Create the DocumentItem, which fails.

        restDocumentItemMockMvc.perform(
            post("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(documentItem))
        ).andExpect(status().isBadRequest)

        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllDocumentItems() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList
        restDocumentItemMockMvc.perform(get("/api/document-items?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentItem.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].googleDocsId").value(hasItem(DEFAULT_GOOGLE_DOCS_ID)))
            .andExpect(jsonPath("$.[*].documentText").value(hasItem(DEFAULT_DOCUMENT_TEXT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
    }
    
    @Test
    @Transactional
    fun getDocumentItem() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        val id = documentItem.id
        assertNotNull(id)

        // Get the documentItem
        restDocumentItemMockMvc.perform(get("/api/document-items/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.googleDocsId").value(DEFAULT_GOOGLE_DOCS_ID))
            .andExpect(jsonPath("$.documentText").value(DEFAULT_DOCUMENT_TEXT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
    }

    @Test
    @Transactional
    fun getDocumentItemsByIdFiltering()  {
      // Initialize the database
      documentItemRepository.saveAndFlush(documentItem)
      val id = documentItem.id

      defaultDocumentItemShouldBeFound("id.equals=" + id)
      defaultDocumentItemShouldNotBeFound("id.notEquals=" + id)

      defaultDocumentItemShouldBeFound("id.greaterThanOrEqual=" + id)
      defaultDocumentItemShouldNotBeFound("id.greaterThan=" + id)

      defaultDocumentItemShouldBeFound("id.lessThanOrEqual=" + id)
      defaultDocumentItemShouldNotBeFound("id.lessThan=" + id)
    }

    @Test
    @Transactional
    fun getAllDocumentItemsByNameIsEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where name equals to DEFAULT_NAME
        defaultDocumentItemShouldBeFound("name.equals=$DEFAULT_NAME")

        // Get all the documentItemList where name equals to UPDATED_NAME
        defaultDocumentItemShouldNotBeFound("name.equals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByNameIsNotEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where name not equals to DEFAULT_NAME
        defaultDocumentItemShouldNotBeFound("name.notEquals=" + DEFAULT_NAME)

        // Get all the documentItemList where name not equals to UPDATED_NAME
        defaultDocumentItemShouldBeFound("name.notEquals=" + UPDATED_NAME)
    }

    @Test
    @Transactional
    fun getAllDocumentItemsByNameIsInShouldWork() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocumentItemShouldBeFound("name.in=$DEFAULT_NAME,$UPDATED_NAME")

        // Get all the documentItemList where name equals to UPDATED_NAME
        defaultDocumentItemShouldNotBeFound("name.in=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByNameIsNullOrNotNull() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where name is not null
        defaultDocumentItemShouldBeFound("name.specified=true")

        // Get all the documentItemList where name is null
        defaultDocumentItemShouldNotBeFound("name.specified=false")
    }
                @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByNameContainsSomething(){
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where name contains DEFAULT_NAME
        defaultDocumentItemShouldBeFound("name.contains=" + DEFAULT_NAME)

        // Get all the documentItemList where name contains UPDATED_NAME
        defaultDocumentItemShouldNotBeFound("name.contains=" + UPDATED_NAME)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByNameNotContainsSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where name does not contain DEFAULT_NAME
        defaultDocumentItemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME)

        // Get all the documentItemList where name does not contain UPDATED_NAME
        defaultDocumentItemShouldBeFound("name.doesNotContain=" + UPDATED_NAME)
    }


    @Test
    @Transactional
    fun getAllDocumentItemsByGoogleDocsIdIsEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where googleDocsId equals to DEFAULT_GOOGLE_DOCS_ID
        defaultDocumentItemShouldBeFound("googleDocsId.equals=$DEFAULT_GOOGLE_DOCS_ID")

        // Get all the documentItemList where googleDocsId equals to UPDATED_GOOGLE_DOCS_ID
        defaultDocumentItemShouldNotBeFound("googleDocsId.equals=$UPDATED_GOOGLE_DOCS_ID")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByGoogleDocsIdIsNotEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where googleDocsId not equals to DEFAULT_GOOGLE_DOCS_ID
        defaultDocumentItemShouldNotBeFound("googleDocsId.notEquals=" + DEFAULT_GOOGLE_DOCS_ID)

        // Get all the documentItemList where googleDocsId not equals to UPDATED_GOOGLE_DOCS_ID
        defaultDocumentItemShouldBeFound("googleDocsId.notEquals=" + UPDATED_GOOGLE_DOCS_ID)
    }

    @Test
    @Transactional
    fun getAllDocumentItemsByGoogleDocsIdIsInShouldWork() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where googleDocsId in DEFAULT_GOOGLE_DOCS_ID or UPDATED_GOOGLE_DOCS_ID
        defaultDocumentItemShouldBeFound("googleDocsId.in=$DEFAULT_GOOGLE_DOCS_ID,$UPDATED_GOOGLE_DOCS_ID")

        // Get all the documentItemList where googleDocsId equals to UPDATED_GOOGLE_DOCS_ID
        defaultDocumentItemShouldNotBeFound("googleDocsId.in=$UPDATED_GOOGLE_DOCS_ID")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByGoogleDocsIdIsNullOrNotNull() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where googleDocsId is not null
        defaultDocumentItemShouldBeFound("googleDocsId.specified=true")

        // Get all the documentItemList where googleDocsId is null
        defaultDocumentItemShouldNotBeFound("googleDocsId.specified=false")
    }
                @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByGoogleDocsIdContainsSomething(){
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where googleDocsId contains DEFAULT_GOOGLE_DOCS_ID
        defaultDocumentItemShouldBeFound("googleDocsId.contains=" + DEFAULT_GOOGLE_DOCS_ID)

        // Get all the documentItemList where googleDocsId contains UPDATED_GOOGLE_DOCS_ID
        defaultDocumentItemShouldNotBeFound("googleDocsId.contains=" + UPDATED_GOOGLE_DOCS_ID)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByGoogleDocsIdNotContainsSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where googleDocsId does not contain DEFAULT_GOOGLE_DOCS_ID
        defaultDocumentItemShouldNotBeFound("googleDocsId.doesNotContain=" + DEFAULT_GOOGLE_DOCS_ID)

        // Get all the documentItemList where googleDocsId does not contain UPDATED_GOOGLE_DOCS_ID
        defaultDocumentItemShouldBeFound("googleDocsId.doesNotContain=" + UPDATED_GOOGLE_DOCS_ID)
    }


    @Test
    @Transactional
    fun getAllDocumentItemsByDocumentTextIsEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where documentText equals to DEFAULT_DOCUMENT_TEXT
        defaultDocumentItemShouldBeFound("documentText.equals=$DEFAULT_DOCUMENT_TEXT")

        // Get all the documentItemList where documentText equals to UPDATED_DOCUMENT_TEXT
        defaultDocumentItemShouldNotBeFound("documentText.equals=$UPDATED_DOCUMENT_TEXT")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByDocumentTextIsNotEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where documentText not equals to DEFAULT_DOCUMENT_TEXT
        defaultDocumentItemShouldNotBeFound("documentText.notEquals=" + DEFAULT_DOCUMENT_TEXT)

        // Get all the documentItemList where documentText not equals to UPDATED_DOCUMENT_TEXT
        defaultDocumentItemShouldBeFound("documentText.notEquals=" + UPDATED_DOCUMENT_TEXT)
    }

    @Test
    @Transactional
    fun getAllDocumentItemsByDocumentTextIsInShouldWork() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where documentText in DEFAULT_DOCUMENT_TEXT or UPDATED_DOCUMENT_TEXT
        defaultDocumentItemShouldBeFound("documentText.in=$DEFAULT_DOCUMENT_TEXT,$UPDATED_DOCUMENT_TEXT")

        // Get all the documentItemList where documentText equals to UPDATED_DOCUMENT_TEXT
        defaultDocumentItemShouldNotBeFound("documentText.in=$UPDATED_DOCUMENT_TEXT")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByDocumentTextIsNullOrNotNull() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where documentText is not null
        defaultDocumentItemShouldBeFound("documentText.specified=true")

        // Get all the documentItemList where documentText is null
        defaultDocumentItemShouldNotBeFound("documentText.specified=false")
    }
                @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByDocumentTextContainsSomething(){
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where documentText contains DEFAULT_DOCUMENT_TEXT
        defaultDocumentItemShouldBeFound("documentText.contains=" + DEFAULT_DOCUMENT_TEXT)

        // Get all the documentItemList where documentText contains UPDATED_DOCUMENT_TEXT
        defaultDocumentItemShouldNotBeFound("documentText.contains=" + UPDATED_DOCUMENT_TEXT)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByDocumentTextNotContainsSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where documentText does not contain DEFAULT_DOCUMENT_TEXT
        defaultDocumentItemShouldNotBeFound("documentText.doesNotContain=" + DEFAULT_DOCUMENT_TEXT)

        // Get all the documentItemList where documentText does not contain UPDATED_DOCUMENT_TEXT
        defaultDocumentItemShouldBeFound("documentText.doesNotContain=" + UPDATED_DOCUMENT_TEXT)
    }


    @Test
    @Transactional
    fun getAllDocumentItemsByCreatedAtIsEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where createdAt equals to DEFAULT_CREATED_AT
        defaultDocumentItemShouldBeFound("createdAt.equals=$DEFAULT_CREATED_AT")

        // Get all the documentItemList where createdAt equals to UPDATED_CREATED_AT
        defaultDocumentItemShouldNotBeFound("createdAt.equals=$UPDATED_CREATED_AT")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByCreatedAtIsNotEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where createdAt not equals to DEFAULT_CREATED_AT
        defaultDocumentItemShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT)

        // Get all the documentItemList where createdAt not equals to UPDATED_CREATED_AT
        defaultDocumentItemShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT)
    }

    @Test
    @Transactional
    fun getAllDocumentItemsByCreatedAtIsInShouldWork() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultDocumentItemShouldBeFound("createdAt.in=$DEFAULT_CREATED_AT,$UPDATED_CREATED_AT")

        // Get all the documentItemList where createdAt equals to UPDATED_CREATED_AT
        defaultDocumentItemShouldNotBeFound("createdAt.in=$UPDATED_CREATED_AT")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByCreatedAtIsNullOrNotNull() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where createdAt is not null
        defaultDocumentItemShouldBeFound("createdAt.specified=true")

        // Get all the documentItemList where createdAt is null
        defaultDocumentItemShouldNotBeFound("createdAt.specified=false")
    }

    @Test
    @Transactional
    fun getAllDocumentItemsByUpdatedAtIsEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultDocumentItemShouldBeFound("updatedAt.equals=$DEFAULT_UPDATED_AT")

        // Get all the documentItemList where updatedAt equals to UPDATED_UPDATED_AT
        defaultDocumentItemShouldNotBeFound("updatedAt.equals=$UPDATED_UPDATED_AT")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByUpdatedAtIsNotEqualToSomething() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultDocumentItemShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT)

        // Get all the documentItemList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultDocumentItemShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT)
    }

    @Test
    @Transactional
    fun getAllDocumentItemsByUpdatedAtIsInShouldWork() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultDocumentItemShouldBeFound("updatedAt.in=$DEFAULT_UPDATED_AT,$UPDATED_UPDATED_AT")

        // Get all the documentItemList where updatedAt equals to UPDATED_UPDATED_AT
        defaultDocumentItemShouldNotBeFound("updatedAt.in=$UPDATED_UPDATED_AT")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDocumentItemsByUpdatedAtIsNullOrNotNull() {
        // Initialize the database
        documentItemRepository.saveAndFlush(documentItem)

        // Get all the documentItemList where updatedAt is not null
        defaultDocumentItemShouldBeFound("updatedAt.specified=true")

        // Get all the documentItemList where updatedAt is null
        defaultDocumentItemShouldNotBeFound("updatedAt.specified=false")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private fun defaultDocumentItemShouldBeFound(filter: String) {
        restDocumentItemMockMvc.perform(get("/api/document-items?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentItem.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].googleDocsId").value(hasItem(DEFAULT_GOOGLE_DOCS_ID)))
            .andExpect(jsonPath("$.[*].documentText").value(hasItem(DEFAULT_DOCUMENT_TEXT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restDocumentItemMockMvc.perform(get("/api/document-items/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private fun defaultDocumentItemShouldNotBeFound(filter: String) {
        restDocumentItemMockMvc.perform(get("/api/document-items?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restDocumentItemMockMvc.perform(get("/api/document-items/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

    @Test
    @Transactional
    fun getNonExistingDocumentItem() {
        // Get the documentItem
        restDocumentItemMockMvc.perform(get("/api/document-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateDocumentItem() {
        // Initialize the database
        documentItemService.save(documentItem)

        val databaseSizeBeforeUpdate = documentItemRepository.findAll().size

        // Update the documentItem
        val id = documentItem.id
        assertNotNull(id)
        val updatedDocumentItem = documentItemRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedDocumentItem are not directly saved in db
        em.detach(updatedDocumentItem)
        updatedDocumentItem.name = UPDATED_NAME
        updatedDocumentItem.googleDocsId = UPDATED_GOOGLE_DOCS_ID
        updatedDocumentItem.documentText = UPDATED_DOCUMENT_TEXT
        updatedDocumentItem.createdAt = UPDATED_CREATED_AT
        updatedDocumentItem.updatedAt = UPDATED_UPDATED_AT

        restDocumentItemMockMvc.perform(
            put("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(updatedDocumentItem))
        ).andExpect(status().isOk)

        // Validate the DocumentItem in the database
        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeUpdate)
        val testDocumentItem = documentItemList[documentItemList.size - 1]
        assertThat(testDocumentItem.name).isEqualTo(UPDATED_NAME)
        assertThat(testDocumentItem.googleDocsId).isEqualTo(UPDATED_GOOGLE_DOCS_ID)
        assertThat(testDocumentItem.documentText).isEqualTo(UPDATED_DOCUMENT_TEXT)
        assertThat(testDocumentItem.createdAt).isEqualTo(UPDATED_CREATED_AT)
        assertThat(testDocumentItem.updatedAt).isEqualTo(UPDATED_UPDATED_AT)
    }

    @Test
    @Transactional
    fun updateNonExistingDocumentItem() {
        val databaseSizeBeforeUpdate = documentItemRepository.findAll().size

        // Create the DocumentItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentItemMockMvc.perform(
            put("/api/document-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(documentItem))
        ).andExpect(status().isBadRequest)

        // Validate the DocumentItem in the database
        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteDocumentItem() {
        // Initialize the database
        documentItemService.save(documentItem)

        val databaseSizeBeforeDelete = documentItemRepository.findAll().size

        val id = documentItem.id
        assertNotNull(id)

        // Delete the documentItem
        restDocumentItemMockMvc.perform(
            delete("/api/document-items/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val documentItemList = documentItemRepository.findAll()
        assertThat(documentItemList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_GOOGLE_DOCS_ID = "AAAAAAAAAA"
        private const val UPDATED_GOOGLE_DOCS_ID = "BBBBBBBBBB"

        private const val DEFAULT_DOCUMENT_TEXT = "AAAAAAAAAA"
        private const val UPDATED_DOCUMENT_TEXT = "BBBBBBBBBB"

        private val DEFAULT_CREATED_AT: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_CREATED_AT: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_UPDATED_AT: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_UPDATED_AT: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): DocumentItem {
            val documentItem = DocumentItem(
                name = DEFAULT_NAME,
                googleDocsId = DEFAULT_GOOGLE_DOCS_ID,
                documentText = DEFAULT_DOCUMENT_TEXT,
                createdAt = DEFAULT_CREATED_AT,
                updatedAt = DEFAULT_UPDATED_AT
            )

            return documentItem
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): DocumentItem {
            val documentItem = DocumentItem(
                name = UPDATED_NAME,
                googleDocsId = UPDATED_GOOGLE_DOCS_ID,
                documentText = UPDATED_DOCUMENT_TEXT,
                createdAt = UPDATED_CREATED_AT,
                updatedAt = UPDATED_UPDATED_AT
            )

            return documentItem
        }
    }
}
