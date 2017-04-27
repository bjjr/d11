
package forms;

import org.hibernate.validator.constraints.Range;

public class CacheForm {

	private int	hours;
	private int	minutes;
	private int	seconds;


	public CacheForm() {
		super();
	}

	public CacheForm(final long totalSeconds) {
		this.hours = (int) (totalSeconds / 3600L);
		this.minutes = (int) ((totalSeconds % 3600L) / 60L);
		this.seconds = (int) ((totalSeconds % 3600L) % 60L);
	}

	public Long getTtl(final CacheForm cacheForm) {
		Long res;
		Integer hours, minutes, seconds;
		Long secsFromHours, secsFromMinutes;

		/*
		 * Integer wrapper can be casted to long. But not primitve int.
		 */

		hours = new Integer(cacheForm.getHours());
		minutes = new Integer(cacheForm.getMinutes());
		seconds = new Integer(cacheForm.getSeconds());

		secsFromHours = hours * 3600L;
		secsFromMinutes = minutes * 60L;

		res = secsFromHours + secsFromMinutes + seconds;

		return res;
	}

	@Range(min = 0)
	public int getHours() {
		return this.hours;
	}

	public void setHours(final int hours) {
		this.hours = hours;
	}

	@Range(min = 0)
	public int getMinutes() {
		return this.minutes;
	}

	public void setMinutes(final int minutes) {
		this.minutes = minutes;
	}

	@Range(min = 0)
	public int getSeconds() {
		return this.seconds;
	}

	public void setSeconds(final int seconds) {
		this.seconds = seconds;
	}

}
