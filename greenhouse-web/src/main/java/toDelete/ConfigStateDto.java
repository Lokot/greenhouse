package toDelete;

/**
 * Текущие настройки на устройстве.
 * 
 * @author Lokot
 *
 */
public class ConfigStateDto {

	private float tempMax;
	private float tempMin;
	private float humMax;
	private float humMin;
	private int illumMin;

	public float getTempMax() {
		return tempMax;
	}

	public void setTempMax(float tempMax) {
		this.tempMax = tempMax;
	}

	public float getTempMin() {
		return tempMin;
	}

	public void setTempMin(float tempMin) {
		this.tempMin = tempMin;
	}

	public float getHumMax() {
		return humMax;
	}

	public void setHumMax(float humMax) {
		this.humMax = humMax;
	}

	public float getHumMin() {
		return humMin;
	}

	public void setHumMin(float humMin) {
		this.humMin = humMin;
	}

	public int getIllumMin() {
		return illumMin;
	}

	public void setIllumMin(int illumMin) {
		this.illumMin = illumMin;
	}

	@Override
	public String toString() {
		return "ConfigStateDto [tempMax=" + tempMax + ", tempMin=" + tempMin
				+ ", humMax=" + humMax + ", humMin=" + humMin + ", illumMin="
				+ illumMin + "]";
	}

}
