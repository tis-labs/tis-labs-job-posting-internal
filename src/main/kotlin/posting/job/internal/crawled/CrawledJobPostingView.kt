package posting.job.internal.crawled

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.lineawesome.LineAwesomeIconUrl

private const val JOB_ID = "jobId"
private const val CRAWLED_JOB_POSTING_EDIT_ROUTE_TEMPLATE = "crawled-job-posting/%s/edit"

@PageTitle("수집된 채용공고")
@Route("crawled-job-posting/:jobId?/:action?(edit)")
@Uses(Icon::class)
@Menu(order = 1.0, icon = LineAwesomeIconUrl.TH_SOLID)
class CrawledJobPostingView(
    private val crawledJobPostingService: CrawledJobPostingService,
) : BeforeEnterObserver, Div() {
    private val grid = Grid(CrawledJobPosting::class.java, false)
    private var crawledJobPosting: CrawledJobPosting? = null
    private var binder: BeanValidationBinder<CrawledJobPosting>

    private val rejectButton = Button("REJECT")
    private val acceptButton = Button("ACCEPT")
    private val updateAndAcceptButton = Button("UPDATE")

    private lateinit var jobCompany: TextField
    private lateinit var jobTitle: TextField
    private lateinit var jobUrl: TextField
    private lateinit var crawledJobStatus: ComboBox<CrawledJobStatus>
    private lateinit var crawledDate: DatePicker
    private lateinit var jobId: TextField

    init {
        addClassNames("crawled-job-posting-view")

        // Create UI
        val splitLayout = SplitLayout()

        createGridLayout(splitLayout)
        createEditorLayout(splitLayout)

        add(splitLayout)


        // Configure Grid
        grid.addColumn("jobCompany").setAutoWidth(true)
        grid.addColumn("jobTitle").setAutoWidth(true)
        grid.addColumn("jobUrl").setAutoWidth(true)
        grid.addColumn("jobOptionalInformation").setAutoWidth(true)
        grid.addColumn("crawledJobStatus").setAutoWidth(true)
        grid.addColumn("crawledDate").setAutoWidth(true)
        grid.addColumn("jobId").setAutoWidth(true)

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER)
        refreshGrid()

        grid.asSingleSelect()
            .addValueChangeListener { event: AbstractField.ComponentValueChangeEvent<Grid<CrawledJobPosting>, CrawledJobPosting> ->
                if (event.value != null) {
                    UI.getCurrent()
                        .navigate(java.lang.String.format(CRAWLED_JOB_POSTING_EDIT_ROUTE_TEMPLATE, event.value.jobId))
                } else {
                    clearForm()
                    UI.getCurrent().navigate(CrawledJobPostingView::class.java)
                }
            }

        binder = BeanValidationBinder(CrawledJobPosting::class.java)
        binder.bindInstanceFields(this)

        rejectButton.addClickListener {
            if (this.crawledJobPosting != null) {
                val jobPosting = this.crawledJobPosting
                crawledJobPostingService.reject(jobPosting!!.jobId)
                clearForm()
                refreshGrid()
            }
        }

        acceptButton.addClickListener {
            if (this.crawledJobPosting != null) {
                val jobPosting = this.crawledJobPosting
                crawledJobPostingService.accept(jobPosting!!.jobId)
                clearForm()
                refreshGrid()
            }
        }

        updateAndAcceptButton.addClickListener {
            try {
                if (this.crawledJobPosting != null) {
                    val jobPosting = this.crawledJobPosting
                    binder.writeBean(jobPosting)
                    crawledJobPostingService.updateAndAccept(jobPosting!!)
                    clearForm()
                    refreshGrid()
                    Notification.show("Data updated")
                }
                UI.getCurrent().navigate(CrawledJobPostingView::class.java)
            } catch (exception: IllegalArgumentException) {
                val n = Notification.show(
                    "Error updating the data. Somebody else has updated the record while you were making changes."
                )
                n.position = Notification.Position.MIDDLE
                n.addThemeVariants(NotificationVariant.LUMO_ERROR)
            } catch (validationException: ValidationException) {
                Notification.show("Failed to update the data. Check again that all values are valid")
            }
        }
    }

    private fun updateDataInGrid() {
        grid.setItems(
            ListDataProvider(
                crawledJobPostingService.getCrawledJobPostings().crawledJobPostings
            )
        )
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        val jobId = event.routeParameters[JOB_ID]

        if (jobId.isPresent) {
            val currentJobId = jobId.get()
            val crawledJobPosting = crawledJobPostingService.getCrawledJobPosting(currentJobId)
            if (crawledJobPosting != null) {
                populateForm(crawledJobPosting)
            } else {
                Notification.show(
                    String.format("The requested samplePerson was not found, ID = %s", currentJobId), 3000,
                    Notification.Position.BOTTOM_START
                )
                refreshGrid()
                event.forwardTo(CrawledJobPostingView::class.java)
            }
        }
    }

    private fun createEditorLayout(splitLayout: SplitLayout) {
        val editorLayoutDiv = Div()
        editorLayoutDiv.className = "editor-layout"

        val editorDiv = Div()
        editorDiv.className = "editor"
        editorLayoutDiv.add(editorDiv)

        val formLayout = FormLayout()

        jobCompany = TextField("Job Company")
        jobTitle = TextField("Job Title")
        jobUrl = TextField("Job Url")
        crawledJobStatus = ComboBox("Crawled Job Status")
        crawledDate = DatePicker("Crawled Date")
        jobId = TextField("Job Id")

        crawledJobStatus.setItems(CrawledJobStatus.entries)

        formLayout.add(
            jobCompany,
            jobTitle,
            jobUrl,
            crawledJobStatus,
            crawledDate,
            jobId
        )

        editorDiv.add(formLayout)
        createButtonLayout(editorLayoutDiv)
        splitLayout.addToSecondary(editorLayoutDiv)
    }

    private fun createButtonLayout(editorLayoutDiv: Div) {
        val buttonLayout = HorizontalLayout()
        buttonLayout.className = "button-layout"
        acceptButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS)
        rejectButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
        updateAndAcceptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        buttonLayout.add(acceptButton, rejectButton, updateAndAcceptButton)
        editorLayoutDiv.add(buttonLayout)
    }

    private fun createGridLayout(splitLayout: SplitLayout) {
        val wrapper = Div()
        wrapper.className = "grid-wrapper"
        splitLayout.addToPrimary(wrapper)
        wrapper.add(grid)
    }

    private fun refreshGrid() {
        grid.select(null)
        grid.dataProvider.refreshAll()
        updateDataInGrid()
    }

    private fun populateForm(value: CrawledJobPosting?) {
        this.crawledJobPosting = value
        binder.readBean(this.crawledJobPosting)
    }

    private fun clearForm() {
        populateForm(null)
    }
}
