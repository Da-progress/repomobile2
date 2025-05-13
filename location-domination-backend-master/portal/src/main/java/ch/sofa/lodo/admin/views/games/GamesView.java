package ch.sofa.lodo.admin.views.games;

import ch.sofa.lodo.admin.views.MainView;
import ch.sofa.lodo.admin.views.games.components.GameDetail;
import ch.sofa.lodo.admin.views.games.components.GamesGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route(value = GamesView.ROUTE, layout = MainView.class)
public class GamesView extends HorizontalLayout implements BeforeEnterObserver {

	public static final String ROUTE = "games";
	private static final long serialVersionUID = 1L;
	@Autowired
	private GamesGrid gamesGrid;

	@Autowired
	private GameDetail gridDetail;

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		// TODO Auto-generated method stub

	}

	@PostConstruct
	public void init() {
		add(gamesGrid);
		add(gridDetail);
		gridDetail.getContent().setWidth("600px");
		setSizeFull();
	}
}
