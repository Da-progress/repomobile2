package ch.sofa.lodo.admin.views.gameevents;

import ch.sofa.lodo.admin.views.MainView;
import ch.sofa.lodo.admin.views.gameevents.components.EventDetail;
import ch.sofa.lodo.admin.views.gameevents.components.EventGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route(value = EventsView.ROUTE, layout = MainView.class)
public class EventsView extends HorizontalLayout implements BeforeEnterObserver {

	public static final String ROUTE = "events";
	private static final long serialVersionUID = 1L;
	@Autowired
	private EventGrid eventsGrid;

	@Autowired
	private EventDetail eventDetail;

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// TODO Auto-generated method stub
	}

	@PostConstruct
	public void init() {
		add(eventsGrid);
		add(eventDetail);
		eventDetail.getContent().setWidth("600px");
		setSizeFull();
	}
}
