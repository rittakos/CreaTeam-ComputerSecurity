#include <iostream>
#include "caff.h"
#include "bitmap.h"
#include "error.h"

#include <crtdbg.h>

#define _CRTDBG_MAP_ALLOC


void readCaff(std::string filePath, std::string mode, std::string out = "")
{
	Caff caff;

	Error succes = caff.load(filePath);

	if (succes != ErrorType::OK)
		succes.writeErrorMessage(std::cout);
	else
		std::cout << "CAFF File has been read succesfully!" << std::endl;

	if (mode == std::string("preview"))
	{
		Bitmap bitmap;
		Error err = bitmap.save(caff.animations[0].ciff, "C:\\Projects\\KreaTeam-ComputerSecurity\\ExampleFiles\\out.bmp");
		if (err == OK)
			std::cout << "Bitmap generated succesfully!" << std::endl;
		else
			err.writeErrorMessage(std::cout);
	}
	else if (mode == std::string("data"))
	{
		std::string data = caff.dataToString();
		std::cout << data;
	}
}


int main(int argc, char* argv[])
{
	readCaff(argv[1], argv[2], argv[3]);

	//_CrtDumpMemoryLeaks();

	return 0;
}