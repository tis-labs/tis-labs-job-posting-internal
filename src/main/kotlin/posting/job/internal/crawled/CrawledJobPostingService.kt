package posting.job.internal.crawled

import org.springframework.stereotype.Service

@Service
class CrawledJobPostingService(
    private val crawledJobPostingProjection: FakeCrawledJobPostingProjection,
) {
    fun getCrawledJobPostings() = crawledJobPostingProjection.getPendingCrawledJobPostings()

    fun getCrawledJobPosting(jobId: String) =
        crawledJobPostingProjection.getCrawledJobPosting(jobId)

    fun updateAndAccept(crawledJobPosting: CrawledJobPosting) {
        crawledJobPosting.update()
        crawledJobPostingProjection.save(crawledJobPosting)
        crawledJobPosting.accept()
        crawledJobPostingProjection.save(crawledJobPosting)
    }

    fun accept(crawledJobPostingId: String) {
        val crawledJobPosting = crawledJobPostingProjection.getCrawledJobPosting(crawledJobPostingId)
            ?: throw IllegalArgumentException("Invalid job id")
        crawledJobPosting.accept()
        crawledJobPostingProjection.save(crawledJobPosting)
    }

    fun reject(crawledJobPostingId: String) {
        val crawledJobPosting = crawledJobPostingProjection.getCrawledJobPosting(crawledJobPostingId)
            ?: throw IllegalArgumentException("Invalid job id")
        crawledJobPosting.reject()
        crawledJobPostingProjection.save(crawledJobPosting)
    }
}
