package posting.job.internal.crawled

import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class FakeCrawledJobPostingProjection {
    private val crawledJobPostings = mutableMapOf<String, CrawledJobPosting>()

    init {
        crawledJobPostings["id1"] = CrawledJobPosting().apply {
            this.jobCompany = "company1"
            this.jobTitle = "title1"
            this.jobUrl = "url1"
            this.jobOptionalInformation = mapOf("key1" to "value1")
            this.crawledJobStatus = CrawledJobStatus.PENDING
            this.crawledDate = LocalDate.now().minusDays(1)
            this.jobId = "id1"
        }
        crawledJobPostings["id2"] = CrawledJobPosting().apply {
            this.jobCompany = "company2"
            this.jobTitle = "title2"
            this.jobUrl = "url2"
            this.jobOptionalInformation = mapOf("key2" to "value2")
            this.crawledJobStatus = CrawledJobStatus.PENDING
            this.crawledDate = LocalDate.now().plusDays(4)
            this.jobId = "id2"
        }
        crawledJobPostings["id3"] = CrawledJobPosting().apply {
            this.jobCompany = "company3"
            this.jobTitle = "title3"
            this.jobUrl = "url3"
            this.jobOptionalInformation = mapOf("key3" to "value3")
            this.crawledJobStatus = CrawledJobStatus.PENDING
            this.crawledDate = LocalDate.now()
            this.jobId = "id3"
        }
    }

    fun getPendingCrawledJobPostings(): CrawledJobPostings {
        return CrawledJobPostings(crawledJobPostings.values.filter {
            it.crawledJobStatus == CrawledJobStatus.PENDING
        }.toList())
    }

    fun getCrawledJobPosting(jobId: String): CrawledJobPosting? {
        return crawledJobPostings[jobId]
    }

    fun save(crawledJobPosting: CrawledJobPosting) {
        crawledJobPostings[crawledJobPosting.jobId] = crawledJobPosting
    }
}
