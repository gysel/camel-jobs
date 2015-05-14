package ch.hsr.camel.jobs.samples;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssLocation {

	@JsonProperty("iss_position")
	private Position issPosition;
	private String message;
	private Long timestamp;

	public class Position {
		private double latitude;
		private double longitude;

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Position getIssPosition() {
		return issPosition;
	}

	public void setIssPosition(Position issPosition) {
		this.issPosition = issPosition;
	}

}
