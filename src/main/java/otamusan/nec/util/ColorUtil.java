package otamusan.nec.util;

import java.awt.Color;

public class ColorUtil {
	public static float getHue(Color source) {
		float[] hsb;
		hsb = Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), null);
		return hsb[0];
	}

	public static float getSaturation(Color source) {
		float[] hsb;
		hsb = Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), null);
		return hsb[1];
	}

	public static float getBrightness(Color source) {
		float[] hsb;
		hsb = Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), null);
		return hsb[2];
	}

	public static Color getColor(Color source, float h, float s, float b) {
		if (h == -1)
			h = getHue(source);
		if (s == -1)
			h = getSaturation(source);
		if (b == -1)
			h = getBrightness(source);
		Color color = Color.getHSBColor(h, s, b);
		return color;
	}

	public static Color getMultiplid(Color source, float h, float s, float b) {
		return getColor(source, getHue(source) * h, getSaturation(source) * s, getBrightness(source) * b);
	}

	public static Color getCompressedColor(Color source, int time) {
		float multi = 1.0f / (float) time;
		return getMultiplid(source, 1, 1, multi);
	}

	public static Color getCompressedColor(int time) {
		return getCompressedColor(new Color(1f, 1f, 1f), time);
	}
}
