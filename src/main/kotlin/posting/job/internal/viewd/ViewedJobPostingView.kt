package posting.job.internal.viewd

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.lineawesome.LineAwesomeIconUrl

@PageTitle("노출된 채용공고")
@Route("viewed-job-posting")
@Menu(order = 2.0, icon = LineAwesomeIconUrl.TH_SOLID)
class ViewedJobPostingView : BeforeEnterObserver, Div() {

    init {
        addClassNames("viewed-job-posting-view")

    }

    override fun beforeEnter(event: BeforeEnterEvent) {
    }
}
