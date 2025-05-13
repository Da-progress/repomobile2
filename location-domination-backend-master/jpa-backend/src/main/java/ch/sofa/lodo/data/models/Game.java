package ch.sofa.lodo.data.models;

import ch.sofa.lodo.data.constants.GameState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
@Table(name = "game")
public class Game extends SuperEntity {

	@Column(name = "execution_datetime")
	private LocalDateTime executionDateTime;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "host_player_id")
	private User hostPlayer;

	@ManyToOne
	@JoinColumn(name = "guest_player_id")
	private User guestPlayer;

	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;

	@ManyToOne
	@JoinColumn(name = "event_sport_id")
	private EventSport eventSport;

	@Column(name = "host_score")
	private Integer hostScore;

	@Column(name = "guest_score")
	private Integer guestScore;

	@Enumerated(EnumType.STRING)
	@Column(name = "game_state", length = 12)
	private GameState gameState;

	@NotNull
	@Column(name = "creation_datetime")
	private LocalDateTime creationDateTime;

	@Column(name = "accept_datetime")
	private LocalDateTime acceptDateTime;

	@Column(name = "result_record_datetime")
	private LocalDateTime resultRecordDateTime;

	@Column(name = "verified_datetime")
	private LocalDateTime verifiedDateTime;

	@Column(name = "appointment_1_datetime")
	private LocalDateTime appointment1;

	@Column(name = "appointment_2_datetime")
	private LocalDateTime appointment2;

	@Column(name = "appointment_3_datetime")
	private LocalDateTime appointment3;

	@Column(name = "count_for_leader_board")
	private boolean countForLeaderBoard;

	@Column(name = "has_host_made_appointments")
	private boolean hasHostMadeAppointments;

	@Column(name = "has_host_suggested_result")
	private boolean hasHostSuggestedResult;

	public LocalDateTime getExecutionDateTime() {
		return executionDateTime;
	}

	public void setExecutionDateTime(LocalDateTime executionDateTime) {
		this.executionDateTime = executionDateTime;
	}

	public User getHostPlayer() {
		return hostPlayer;
	}

	public void setHostPlayer(User hostPlayer) {
		this.hostPlayer = hostPlayer;
	}

	public User getGuestPlayer() {
		return guestPlayer;
	}

	public void setGuestPlayer(User guestPlayer) {
		this.guestPlayer = guestPlayer;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getHostScore() {
		return hostScore;
	}

	public void setHostScore(Integer hostScore) {
		this.hostScore = hostScore;
	}

	public Integer getGuestScore() {
		return guestScore;
	}

	public void setGuestScore(Integer guestScore) {
		this.guestScore = guestScore;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public LocalDateTime getAcceptDateTime() {
		return acceptDateTime;
	}

	public void setAcceptDateTime(LocalDateTime acceptDateTime) {
		this.acceptDateTime = acceptDateTime;
	}

	public LocalDateTime getResultRecordDateTime() {
		return resultRecordDateTime;
	}

	public void setResultRecordDateTime(LocalDateTime resultRecordDateTime) {
		this.resultRecordDateTime = resultRecordDateTime;
	}

	public LocalDateTime getVerifiedDateTime() {
		return verifiedDateTime;
	}

	public void setVerifiedDateTime(LocalDateTime verifiedDateTime) {
		this.verifiedDateTime = verifiedDateTime;
	}

	public LocalDateTime getAppointment1() {
		return appointment1;
	}

	public void setAppointment1(LocalDateTime appointment1) {
		this.appointment1 = appointment1;
	}

	public LocalDateTime getAppointment2() {
		return appointment2;
	}

	public void setAppointment2(LocalDateTime appointment2) {
		this.appointment2 = appointment2;
	}

	public LocalDateTime getAppointment3() {
		return appointment3;
	}

	public void setAppointment3(LocalDateTime appointment3) {
		this.appointment3 = appointment3;
	}

	public boolean isCountForLeaderBoard() {
		return countForLeaderBoard;
	}

	public void setCountForLeaderBoard(boolean countForLeaderBoard) {
		this.countForLeaderBoard = countForLeaderBoard;
	}

	public boolean isHasHostMadeAppointments() {
		return hasHostMadeAppointments;
	}

	public void setHasHostMadeAppointments(boolean hasHostMadeAppointments) {
		this.hasHostMadeAppointments = hasHostMadeAppointments;
	}

	public EventSport getEventSport() {
		return eventSport;
	}

	public void setEventSport(EventSport eventSport) {
		this.eventSport = eventSport;
	}

	public boolean isHasHostSuggestedResult() {
		return hasHostSuggestedResult;
	}

	public void setHasHostSuggestedResult(boolean hasHostSuggestedResult) {
		this.hasHostSuggestedResult = hasHostSuggestedResult;
	}
}
