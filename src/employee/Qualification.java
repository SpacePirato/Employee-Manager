package employee;

public class Qualification {

	private String title;
	private String dateCompleted;
	private String dateExpiry;
	
	public Qualification() {
	}
	
	public Qualification(String title, String dateCompleted, String dateExpiry) {
		this.setTitle(title);
		this.setDateCompleted(dateCompleted);
		this.setDateExpiry(dateExpiry);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(String dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public String getDateExpiry() {
		return dateExpiry;
	}

	public void setDateExpiry(String dateExpiry) {
		this.dateExpiry = dateExpiry;
	}
	
	
}
