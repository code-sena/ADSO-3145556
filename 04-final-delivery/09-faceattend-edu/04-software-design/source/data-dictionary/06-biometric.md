# Domain: Biometric

> **Owner:** Biometric bounded context
> **DB Engine:** MongoDB 7 for embeddings + one SQL workflow table (`biometric_update_case` hosted in `configuration`)
>
> **Engine justification:**
> - Facial and fingerprint embeddings are variable-length vectors — document storage is natural.
> - MongoDB supports vector search indexes for similarity matching.
> - Schema flexibility accommodates different embedding model versions.

---

## Design Principles

- Biometric does **NOT** know Person — `person_id` represents an external identity.
- Embeddings persist in **NoSQL** (MongoDB); the re-enrollment workflow persists in SQL.
- ID references are resolved at the application level.
- `biometric_update_case` is logically owned by Biometric (DBML v7) and references these collections by logical ID (`current_embedding_ref`, cross-paradigm, no FK). It is still physically hosted in the `configuration` schema transiently for Liquibase/service compatibility — moving it requires a compatibility migration.

---

## 1. Collection: facial_embeddings

**Purpose:** Storage of facial embeddings (numeric vectors representing a face).

```javascript
// MongoDB Collection: facial_embeddings
{
  _id: ObjectId,

  // Business fields
  person_id: UUID,             // External identity (→ Identity.person)
  encoding: [Float],           // Facial embedding vector
  model_version: String,       // AI model version used
  template_version: Int,       // Version counter for this person's facial templates
  enrolled_at: Date,           // When this embedding was captured

  // Status fields
  is_active: Boolean,

  // Audit (in all documents)
  created_at: Date,
  updated_at: Date,
  deleted_at: Date,
  created_by: UUID,
  updated_by: UUID,
  deleted_by: UUID
}
```

**Data dictionary:**

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| _id | ObjectId | MongoDB document identifier | `507f1f77bcf86cd799439011` |
| person_id | UUID | External identity (→ Identity.person) | `550e8400-e29b-41d4-a716-446655440001` |
| encoding | Array[Float] | Facial embedding vector (512/1024 dimensions) | `[0.12, -0.45, ...]` |
| model_version | String | AI model version used | `face_net_v2.1` |
| template_version | Int | Version counter for this person | `1` |
| enrolled_at | Date | When this embedding was captured | `2024-01-15T10:30:00Z` |
| is_active | Boolean | Whether this is the current active embedding | `true` |
| created_at | Date | When the document was created | `2024-01-15T10:30:00Z` |
| updated_at | Date | When the document was last updated | `2024-01-15T10:30:00Z` |
| deleted_at | Date | NULL = active; with value = soft-deleted | `null` |
| created_by | UUID | User who created this document | `550e8400-...` |
| updated_by | UUID | User who last updated this document | `550e8400-...` |
| deleted_by | UUID | User who soft-deleted this document | `null` |

**Indexes:**

| Name | Fields | Type | Justification |
|------|--------|------|---------------|
| idx_embedding_person_active | (person_id, is_active) | Compound | Find active embeddings per person |
| idx_embedding_deleted | deleted_at | Sparse | Filter out soft-deleted documents |

---

## 2. Collection: fingerprint_embeddings

**Purpose:** Storage of fingerprint biometric templates.

```javascript
// MongoDB Collection: fingerprint_embeddings
{
  _id: ObjectId,

  // Business fields
  person_id: UUID,             // External identity (→ Identity.person)
  finger_number: Int,          // 1..10 (finger identifier)
  encoding: [Float],           // Fingerprint template vector
  model_version: String,       // AI model version used
  template_version: Int,       // Version counter for this person's finger templates
  enrolled_at: Date,           // When this template was captured

  // Status fields
  is_active: Boolean,

  // Audit (in all documents)
  created_at: Date,
  updated_at: Date,
  deleted_at: Date,
  created_by: UUID,
  updated_by: UUID,
  deleted_by: UUID
}
```

**Data dictionary:**

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| _id | ObjectId | MongoDB document identifier | `507f1f77bcf86cd799439012` |
| person_id | UUID | External identity (→ Identity.person) | `550e8400-...` |
| finger_number | Int | Finger identifier (1..10) | `1` |
| encoding | Array[Float] | Fingerprint template vector | `[0.23, -0.67, ...]` |
| model_version | String | AI model version used | `fingerprint_v1.3` |
| template_version | Int | Version counter for this person+finger | `1` |
| enrolled_at | Date | When this template was captured | `2024-01-15T10:30:00Z` |
| is_active | Boolean | Whether this is the current active template | `true` |
| created_at | Date | When the document was created | `2024-01-15T10:30:00Z` |
| updated_at | Date | When the document was last updated | `2024-01-15T10:30:00Z` |
| deleted_at | Date | NULL = active; with value = soft-deleted | `null` |
| created_by | UUID | User who created this document | `550e8400-...` |
| updated_by | UUID | User who last updated this document | `550e8400-...` |
| deleted_by | UUID | User who soft-deleted this document | `null` |

**Indexes:**

| Name | Fields | Type | Justification |
|------|--------|------|---------------|
| idx_fingerprint_person_finger_active | (person_id, finger_number, is_active) | Compound | Find active template by person and finger |
| idx_fingerprint_deleted | deleted_at | Sparse | Filter out soft-deleted documents |

---

## 3. Cross-Context Relationships

| From | To | Reference Type | Mechanism |
|------|----|----------------|-----------|
| Facial_Embedding.person_id | Identity.Person.person_id | External ID (no FK) | Cross-context reference by UUID |
| Fingerprint_Embedding.person_id | Identity.Person.person_id | External ID (no FK) | Cross-context reference by UUID |
| Configuration.Biometric_Update_Case.current_embedding_ref | Facial/Fingerprint_Embedding._id | External ID (no FK) | Cross-paradigm reference (SQL → NoSQL) |

> **Note:** Embeddings persist in MongoDB with no physical FKs. The `biometric_update_case` SQL workflow table is logically owned by Biometric and physically hosted in `configuration`; all references are resolved at the application level.

---

## 4. Correlations

- Domain entities that map to these collections → `02-domain/entities-and-rules.md`
- Saga and Outbox pattern for distributed consistency → `05-architecture/pattern-guide.md`
- Data for each service in detail → `09-microservices/services/biometric-service/data-model.md`
- How data is accessed via API → `07-api/contracts/openapi/`
