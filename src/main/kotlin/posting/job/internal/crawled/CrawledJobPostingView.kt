package posting.job.internal.crawled

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.lineawesome.LineAwesomeIconUrl

@PageTitle("수집된 채용공고")
@Route("crawled-job-posting")
@Menu(order = 1.0, icon = LineAwesomeIconUrl.TH_SOLID)
class CrawledJobPostingView : BeforeEnterObserver, Div() {

    init {
        addClassNames("crawled-job-posting-view")

    }

    override fun beforeEnter(event: BeforeEnterEvent) {
    }
}
