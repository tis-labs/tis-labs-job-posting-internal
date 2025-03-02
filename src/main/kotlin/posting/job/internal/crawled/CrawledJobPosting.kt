package posting.job.internal.crawled

import java.time.LocalDate

/**
 * 채용 공고 부가 정보(job optional information)  : 뽑는 인원, 경력 등 정규화하기 어려운 정보를 저장한다. json 형태로 저장되며 형식이 정해지지 않는다.
 * 채용 회사(job company) : 현재 채용 공고를 올린 채용 회사 이름이다.
 * 채용 공고명(job title) : 채용 공고에 작성된 타이틀 정보다.
 * 채용 url(job url) : 채용 공고 (채용공고가 없으면 채용공고 리스트로 사용)
 * 채용 공고 식별자(job identity) : 채용 회사 + 채용 공고명 식별자가 정해진다.
 */
class CrawledJobPosting {
    lateinit var jobCompany: String
    lateinit var jobTitle: String
    lateinit var jobUrl: String
    lateinit var jobOptionalInformation: Map<String, Any>
    lateinit var crawledJobStatus: CrawledJobStatus
    lateinit var crawledDate: LocalDate
    lateinit var jobId: String

    fun update() {
        crawledJobStatus = CrawledJobStatus.UPDATED
    }

    fun accept() {
        crawledJobStatus = CrawledJobStatus.ACCEPTED
    }

    fun reject() {
        crawledJobStatus = CrawledJobStatus.REJECTED
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CrawledJobPosting

        return jobId == other.jobId
    }

    override fun hashCode(): Int {
        return jobId.hashCode()
    }
}
