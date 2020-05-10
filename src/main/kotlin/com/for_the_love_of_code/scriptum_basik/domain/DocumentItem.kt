package com.for_the_love_of_code.scriptum_basik.domain



import javax.persistence.*
import javax.validation.constraints.*

import java.io.Serializable
import java.time.Instant

/**
 * A DocumentItem.
 */
@Entity
@Table(name = "document_item")
data class DocumentItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @get: NotNull
    @Column(name = "google_docs_id", nullable = false)
    var googleDocsId: String? = null,

    @get: NotNull
    @Column(name = "document_text", nullable = false)
    var documentText: String? = null,

    @Column(name = "created_at")
    var createdAt: Instant? = null,

    @get: NotNull
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DocumentItem) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "DocumentItem{" +
        "id=$id" +
        ", name='$name'" +
        ", googleDocsId='$googleDocsId'" +
        ", documentText='$documentText'" +
        ", createdAt='$createdAt'" +
        ", updatedAt='$updatedAt'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
