package posting.job.internal.dashboard

import com.vaadin.flow.component.html.Main
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.lineawesome.LineAwesomeIconUrl

@PageTitle("대시보드")
@Route("")
@Menu(order = 0.0, icon = LineAwesomeIconUrl.CHART_AREA_SOLID)
class DashboardView : Main() {
    init {
        addClassNames("dashboard-view")
    }
}
