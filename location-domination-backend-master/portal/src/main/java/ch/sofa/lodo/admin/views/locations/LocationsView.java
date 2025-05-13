package ch.sofa.lodo.admin.views.locations;

import ch.sofa.lodo.admin.views.MainView;
import ch.sofa.lodo.admin.views.locations.components.LocationDetail;
import ch.sofa.lodo.admin.views.locations.components.LocationsGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route(value = LocationsView.ROUTE, layout = MainView.class)
public class LocationsView extends HorizontalLayout implements BeforeEnterObserver {

	public static final String ROUTE = "locations";
	private static final long serialVersionUID = 1L;
	@Autowired
	private LocationsGrid locationsGrid;

	@Autowired
	private LocationDetail locationDetail;

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		// TODO Auto-generated method stub

	}

	@PostConstruct
	public void init() {
		add(locationsGrid);
		add(locationDetail);
		locationDetail.getContent().setWidth("600px");
		setSizeFull();
	}
}
