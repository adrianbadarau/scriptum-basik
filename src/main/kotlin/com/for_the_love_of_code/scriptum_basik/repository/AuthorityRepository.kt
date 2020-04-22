package com.for_the_love_of_code.scriptum_basik.repository

import com.for_the_love_of_code.scriptum_basik.domain.Authority
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository for the [Authority] entity.
 */

interface AuthorityRepository : JpaRepository<Authority, String>
