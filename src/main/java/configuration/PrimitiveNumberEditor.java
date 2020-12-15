package configuration;

import java.text.NumberFormat;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

public class PrimitiveNumberEditor extends CustomNumberEditor {
	private final boolean allowEmpty;
	public PrimitiveNumberEditor(Class<? extends Number> numberClass,
			boolean allowEmpty)	throws IllegalArgumentException {
		this(numberClass, null, allowEmpty);
	}
	public PrimitiveNumberEditor(Class<? extends Number> numberClass,
			NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
		super(numberClass, numberFormat, allowEmpty);
		this.allowEmpty = allowEmpty;
	}
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(allowEmpty && (text==null || text.length()==0)) {
			super.setAsText("0");
		} else {
			super.setAsText(text);
		}
	}
}
