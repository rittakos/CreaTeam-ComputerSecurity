#ifndef PIXEL_H
#define PIXEL_H

struct Pixel
{
	int R;
	int G;
	int B;

	Pixel(int R = 0, int G = 0, int B = 0) : R(R), G(G), B(B) {}
	bool isValid()
	{
		if (R < 0 || R > 255 ||
			G < 0 || G > 255 ||
			B < 0 || B > 255)
		{
			return false;
		}

		return true;
	}
};

#endif