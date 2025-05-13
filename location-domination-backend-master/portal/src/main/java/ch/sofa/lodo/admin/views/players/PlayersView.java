package ch.sofa.lodo.admin.views.players;

import ch.sofa.lodo.admin.views.MainView;
import ch.sofa.lodo.admin.views.players.components.PlayerDetails;
import ch.sofa.lodo.admin.views.players.components.PlayersGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route(value = PlayersView.ROUTE, layout = MainView.class)
public class PlayersView extends HorizontalLayout implements BeforeEnterObserver {

	public static final String ROUTE = "players";
	private static final long serialVersionUID = 1L;
	@Autowired
	private PlayersGrid playersGrid;

	@Autowired
	private PlayerDetails playersDetail;

	@Override
	public void beforeEnter(BeforeEnterEvent arg0) {
		// TODO Auto-generated method stub

	}

	@PostConstruct
	public void init() {
		add(playersGrid);
		add(playersDetail);
		playersDetail.getContent().setWidth("600px");
		setSizeFull();
	}
}
