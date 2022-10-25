#include <iostream>
#include "caff.h"
#include "bitmap.h"
#include "error.h"
#include "parser.hpp"

//#include <crtdbg.h>
//
//#define _CRTDBG_MAP_ALLOC

enum Mode { Data, Preview, None };

Error preview(const Caff& caff, const std::string& out)
{
	Error pathErr = checkExtension(out, "bmp");
	if (pathErr != OK)
		return pathErr;

	Bitmap bitmap;
	if (caff.animations.size() > 0)
	{
		Error err = bitmap.save(caff.animations[0].ciff, out);
		if (err == OK)
			std::cout << "Bitmap generated succesfully!" << std::endl;
		else
			err.writeErrorMessage(std::cout);
	}
	else
		std::cout << "No ciff to generate preview!" << std::endl;

	return OK;
}

void data(const Caff& caff)
{
	std::string data = caff.dataToString();
	std::cout << data;
}

void processCaff(std::string filePath, Mode mode, std::string out = "")
{
	Caff caff;

	Error succes = caff.load(filePath);

	if (succes != ErrorType::OK)
		succes.writeErrorMessage(std::cout);
	else
		std::cout << "CAFF File has been read succesfully!" << std::endl;

	switch (mode)
	{
		case Data:
		{
			data(caff);
			break;
		}
		case Preview:
		{
			Error err = preview(caff, out);
			if (err != OK)
				err.writeErrorMessage(std::cout);
			break;
		}
		case None:
		default:
		{
			std::cout << "Invalid Mode!" << std::endl;
		}
	}
}


int main(int argc, char* argv[])
{
	Mode mode = None;
	if (argv[2] == std::string("data"))
		mode = Data;
	else if (argv[2] == std::string("preview"))
		mode = Preview;

	if (argc == 4)
		processCaff(argv[1], mode, argv[3]);
	else if (argc == 3)
		processCaff(argv[1], mode);
	else
		std::cout << "Invalid Parameters!" << std::endl;

	//_CrtDumpMemoryLeaks();

	return 0;
}