package toDelete;

/**
 * Ответ об установке настроек.
 * 
 * @author Lokot
 *
 */
public class ResponceDto {

	private String parametr;
	private String value;

	public String getParametr() {
		return parametr;
	}

	public void setParametr(String parametr) {
		this.parametr = parametr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ResponceDto [parametr=" + parametr + ", value=" + value + "]";
	}

}
