package ins.marianao.sailing.fxml.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import cat.institutmarianao.sailing.ws.model.User;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class Formatters {
	public static final int CC_NUM_SIZE = 16;
	public static final int PHONE_NUM_SIZE = 9;
	public static final int EXT_NUM_SIZE = 4;
	public static final int CP_NUM_SIZE = 5;
	
	public static StringConverter<User> getUserConverter() {
		
		StringConverter<User> converter = new StringConverter<User>()	{
			@Override
			public String toString(User user) {
				if (user == null) return "";
				
				return user.isAdmin()?user.getUsername():user.getInfo();
			}

			// Unnecessary
			@Override
			public User fromString(String string) { return null; }
		};
		return converter;
	}
	
	public static StringConverter<Pair<String,String>> getStringPairConverter(String source) {
		StringConverter<Pair<String,String>> converter = new StringConverter<Pair<String,String>>()	{
			private ResourceBundle resource;
			{
				this.resource = ResourceManager.getInstance().getTranslationBundle();
			}
			private String findString(String text) {
    			if (text == null || "".equals(text)) return null;
    			for (String key : this.resource.keySet()) {
					if (text.equals(this.resource.getString(key))) return key;
				}
    			return null;
    		}
			
    		@Override
			public Pair<String,String> fromString(String text)
			{
    			String key = findString(text);
    			if (key == null) return null;
    			key = key.replaceAll("text."+source+".", key);
    			return new Pair<String, String>(key, this.resource.getString("text."+source+"."+key));
			}

			@Override
			public String toString(Pair<String,String> pair)
			{
				String key = pair == null?"ALL":pair.getKey();
				return this.resource.getString("text."+source+"."+key);
			}
		};
		return converter;
	}
	
	public static StringConverter<Integer> getPostalCodeFormatter() {
		StringConverter<Integer> formatter = new StringConverter<Integer>()
		{
			@Override
			public Integer fromString(String string)
			{
				NumberFormat nf = NumberFormat.getIntegerInstance();
				int value;
				try {
					value = nf.parse(string).intValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0;
				}
				return value;
			}

			@Override
			public String toString(Integer object)
			{
				if (object == null) return "N/F";

				return StringUtils.leftPad(StringUtils.abbreviate(String.valueOf(object), "", CP_NUM_SIZE), CP_NUM_SIZE, "0");
			}
		};
		return formatter;
	}
	
	// 633151523 => "633 151 523"
	public static StringConverter<Number> getPhoneFormatter() {
		StringConverter<Number> formatter = new StringConverter<Number>()
		{
			@Override
			public Integer fromString(String string)
			{
				if (string == null || "".equals(string)) return null;
				
				string = string.replace(" ", "");
				NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("CA","ES"));
				Integer value;
				try {
					value = nf.parse(string).intValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = null;
				}
				return value;
			}

			@Override
			public String toString(Number object)
			{
				if (object == null || 0 == object.intValue()) return null;

				return StringUtils.leftPad(StringUtils.abbreviate(String.valueOf(object), "", PHONE_NUM_SIZE), PHONE_NUM_SIZE, "0").replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1 $2 $3");
			}
		};
		return formatter;
	}

	
	public static StringConverter<Integer> getExtensioFormatter() {
		StringConverter<Integer> formatter = new StringConverter<Integer>()
		{
			@Override
			public Integer fromString(String string)
			{
				string = string.replace(" ", "");
				NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("CA","ES"));
				int value;
				try {
					value = nf.parse(string).intValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0;
				}
				return value;
			}

			@Override
			public String toString(Integer object)
			{
				if (object == null) return "N/F";

				return StringUtils.leftPad(StringUtils.abbreviate(String.valueOf(object), "", EXT_NUM_SIZE), EXT_NUM_SIZE, "0").replaceFirst("(\\d{2})(\\d+)", "$1 $2");
			}
		};
		return formatter;
	}

	public static StringConverter<Long> getCreditCardFormatter() {
		StringConverter<Long> formatter = new StringConverter<Long>()
		{
			@Override
			public Long fromString(String string)
			{
				string = string.replace(" ", "");
				NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("CA","ES"));
				Long value;
				try {
					value = nf.parse(string).longValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0L;
				}
				return value;
			}

			@Override
			public String toString(Long object)
			{
				if (object == null) return "N/F";

				return StringUtils.leftPad(StringUtils.abbreviate(String.valueOf(object), "", CC_NUM_SIZE), CC_NUM_SIZE, "0").replaceFirst("(\\d{4})(\\d{4})(\\d{4})(\\d+)", "$1 $2 $3 $4");
			}
		};
		return formatter;
	}

	public static StringConverter<LocalDate> getCreditCardDateFormatter(String pattern) {
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(pattern);

		StringConverter<LocalDate> formatter = new StringConverter<LocalDate>()
		{
			@Override
			public LocalDate fromString(String string)
			{
				string = string.replace(" ", "");

				return LocalDateTime.parse(string, dtFormatter).toLocalDate();
			}

			@Override
			public String toString(LocalDate object)
			{
				if (object == null) return "N/F";

				return object.format(dtFormatter);
			}
		};
		return formatter;
	}

	public static StringConverter<Integer> getTwoDigitFormatter() {
		DecimalFormat df = new DecimalFormat( "00" );

		StringConverter<Integer> formatter = new StringConverter<Integer>()
		{
			@Override
			public Integer fromString(String string)
			{
				Integer value;
				try {
					value = df.parse(string).intValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0;
				}
				return value;
			}

			@Override
			public String toString(Integer object)
			{
				return (object == null)?df.format(0):df.format(object);
			}
		};
		return formatter;
	}
	
	
	public static StringConverter<Integer> getIntegerFormatter() {
		NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("CA","ES"));

		StringConverter<Integer> formatter = new StringConverter<Integer>()
		{
			@Override
			public Integer fromString(String string)
			{
				Integer value;
				try {
					value = nf.parse(string).intValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0;
				}
				return value;
			}

			@Override
			public String toString(Integer object)
			{
				return (object == null || object == 0)?"":nf.format(object);
			}
		};
		return formatter;
	}

	public static StringConverter<Double> getDecimalFormatter() {
		DecimalFormat df = new DecimalFormat( "#0.0" );

		StringConverter<Double> formatter = new StringConverter<Double>()
		{
			@Override
			public Double fromString(String string)
			{
				Double value;
				try {
					//value = nf.parse(string).doubleValue();
					value = df.parse(string).doubleValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0.0;
				}
				return value;
			}

			@Override
			public String toString(Double object)
			{
				return (object == null || object == 0)?"":df.format(object);
			}
		};
		return formatter;
	}

	public static StringConverter<Double> getMonedaFormatter() {
		StringConverter<Double> formatter = new StringConverter<Double>()
		{
			@Override
			public Double fromString(String string)
			{
				NumberFormat nf = NumberFormat.getInstance(new Locale("CA","ES"));

				Double value;
				try {
					value = nf.parse(string).doubleValue();
				} catch (ParseException e) {
					value = 0.0;
				}
				return value;
			}

			@Override
			public String toString(Double object)
			{
				DecimalFormat df = new DecimalFormat( "###,##0.00€" );

				return (object == null)?df.format(0.0):df.format(object);
			}
		};
		return formatter;
	}
}
