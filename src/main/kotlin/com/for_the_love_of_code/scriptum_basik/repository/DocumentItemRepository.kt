package com.for_the_love_of_code.scriptum_basik.repository

import com.for_the_love_of_code.scriptum_basik.domain.DocumentItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [DocumentItem] entity.
 */
@Suppress("unused")
@Repository
interface DocumentItemRepository : JpaRepository<DocumentItem, Long>, JpaSpecificationExecutor<DocumentItem> {
}
